package io.dnajd.mainservice.config

import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
public class JwtRequestFilter(
    private val jwtUserDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
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
            val userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (JwtUtil.validateAccessToken(jwtToken, userDetails.username)) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, emptyList()
                )
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken;
            }
        }
        filterChain.doFilter(request, response)
    }
}