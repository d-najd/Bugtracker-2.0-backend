package io.dnajd.mainservice.config

import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Component is not used since it will be automatically registered as default filter that way
 */
class JwtAccessTokenRequestFilter(
    private val jwtUserDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        jwtTokenRequestFilterBase(
            request,
            response,
            filterChain,
            jwtUserDetailsService,
            this,
            tokenValidator = { jwtToken, username ->
                JwtUtil.validateAccessToken(jwtToken, username)
            }
        )
    }
}

/**
 * Component is not used since it will be automatically registered as default filter that way
 */
class JwtRefreshTokenRequestFilter(
    private val jwtUserDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        jwtTokenRequestFilterBase(
            request,
            response,
            filterChain,
            jwtUserDetailsService,
            this,
            tokenValidator = { jwtToken, username ->
                JwtUtil.validateRefreshToken(jwtToken, username)
            }
        )
    }
}

/**
 * This is to avoid repeating of logic
 * @param tokenValidator outputs are {jwtToken, username} and requires true if the user is valid or invalid, use this
 * to check whether the access or refresh token is valid
 * <pre>
 * Example Implementation: { jwtToken, username ->
 * [JwtUtil.validateAccessToken] (jwtToken, username)
 * }
 * </pre>
 */
fun jwtTokenRequestFilterBase(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain,
    jwtUserDetailsService: UserDetailsService,
    oncePerRequestFilter: OncePerRequestFilter,
    tokenValidator: (String, String) -> Boolean
) {
    try {
        val requestTokenHeader = request.getHeader("Authorization") ?: throw org.springframework.security.access.AccessDeniedException("JWT token required")

        val jwtToken: String = JwtUtil.extractTokenFromHeader(requestTokenHeader)
        val username: String = JwtUtil.getUsernameFromToken(jwtToken)

        if (SecurityContextHolder.getContext().authentication == null) {
            val userDetails = jwtUserDetailsService.loadUserByUsername(username);

            if (tokenValidator.invoke(jwtToken, userDetails.username)) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, emptyList()
                )
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken;
            }
        }
    } catch (e: org.springframework.security.access.AccessDeniedException) {
        response.status = HttpStatus.FORBIDDEN.value()
    } catch (e: IllegalArgumentException) {
        response.status = HttpStatus.FORBIDDEN.value()
        LoggerFactory.getLogger("JwtTokenRequestFilters").warn(e.message)
    } catch (e: MalformedJwtException) {
        response.status = HttpStatus.FORBIDDEN.value()
        LoggerFactory.getLogger("JwtTokenRequestFilters").warn(e.message)
    }

    filterChain.doFilter(request, response)
}