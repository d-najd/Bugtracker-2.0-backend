package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.token.JwtTokenHolder
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import io.dnajd.mainservice.service.jwt_refresh.JwtRefreshService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Only accepts refresh tokens
 */
@RestController
@RequestMapping(Endpoints.JWT_REFRESH_AUTH)
class JwtRefreshAuthController(
    private val service: JwtRefreshService
) {
    @GetMapping("/access_token")
    fun refreshAccessToken(@AuthenticationPrincipal userDetails: UserDetails): JwtTokenHolder {
        return service.refreshAccessToken(userDetails.username)
    }

    @GetMapping("/access_and_refresh_tokens")
    fun refreshAccessAndRefreshTokens(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): JwtTokenHolder {
        return service.refreshAccessAndRefreshTokens(JwtUtil.extractTokenFromHeader(authorizationHeader))
    }
}
