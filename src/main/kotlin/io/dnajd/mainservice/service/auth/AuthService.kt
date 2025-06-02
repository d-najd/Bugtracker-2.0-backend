package io.dnajd.mainservice.service.auth

import io.dnajd.mainservice.domain.token.JwtUserTokenHolder
import io.dnajd.mainservice.domain.user.CreateUserDto
import io.dnajd.mainservice.infrastructure.exception.UserAlreadyExistsException
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface AuthService {
    @Throws(UserNotFoundException::class)
    fun googleSignIn(oauthToken: JwtAuthenticationToken): JwtUserTokenHolder

    @Throws(UserAlreadyExistsException::class)
    fun googleSignUp(
        oauthToken: JwtAuthenticationToken,
        userInfo: CreateUserDto
    ): JwtUserTokenHolder
}