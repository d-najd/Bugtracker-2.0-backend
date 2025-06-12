package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.token.JwtTokenHolder
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.jwt_refresh.JwtRefreshService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Only accepts refresh tokens
 */
@RestController
@RequestMapping(Endpoints.JWT_AUTH)
class JwtRefreshAuthController(
    private val service: JwtRefreshService
) {
    @GetMapping
    fun refreshAccessToken(@AuthenticationPrincipal userDetails: UserDetails): JwtTokenHolder {
        return service.refreshAccessToken(userDetails.username)
    }
}
