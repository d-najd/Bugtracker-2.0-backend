package io.dnajd.mainservice.service.jwt_refresh

import io.dnajd.mainservice.domain.token.JwtTokenHolder
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface JwtRefreshService {
    @Throws(UserNotFoundException::class)
    fun refreshAccessToken(username: String): JwtTokenHolder

    @Throws
    fun refreshRefreshToken(username: String, refreshToken: JwtAuthenticationToken): JwtTokenHolder
}
