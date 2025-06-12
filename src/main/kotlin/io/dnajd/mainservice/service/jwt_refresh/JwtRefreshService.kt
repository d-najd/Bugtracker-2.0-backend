package io.dnajd.mainservice.service.jwt_refresh

import io.dnajd.mainservice.domain.token.JwtTokenHolder
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException

interface JwtRefreshService {
    @Throws(UserNotFoundException::class)
    fun refreshAccessToken(username: String): JwtTokenHolder

    fun refreshRefreshToken(refreshToken: String): JwtTokenHolder
}
