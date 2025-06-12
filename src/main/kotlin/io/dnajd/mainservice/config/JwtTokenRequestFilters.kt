package io.dnajd.mainservice.config

import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
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
    val requestTokenHeader = request.getHeader("Authorization")
    var username: String? = null
    var jwtToken: String? = null

    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
        jwtToken = requestTokenHeader.substring(7);
        username = JwtUtil.getUsernameFromToken(jwtToken)
    } else {
        throw IllegalArgumentException("JWT Token does not begin with Bearer String")
    }

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

    filterChain.doFilter(request, response)

    // Disabling the filter
    val registration = FilterRegistrationBean(oncePerRequestFilter)
    registration.isEnabled = false
}