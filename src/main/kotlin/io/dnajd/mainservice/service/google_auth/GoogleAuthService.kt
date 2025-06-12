package io.dnajd.mainservice.service.google_auth

import io.dnajd.mainservice.controller.GoogleAuthController
import io.dnajd.mainservice.domain.token.JwtUserTokenHolder
import io.dnajd.mainservice.domain.user.CreateUserDto
import io.dnajd.mainservice.infrastructure.exception.UserAlreadyExistsException
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

/**
 * @see GoogleAuthController
 */
interface GoogleAuthService {
    @Throws(UserNotFoundException::class)
    fun googleSignIn(oauthToken: JwtAuthenticationToken): JwtUserTokenHolder

    @Throws(UserAlreadyExistsException::class)
    fun googleSignUp(
        oauthToken: JwtAuthenticationToken,
        userInfo: CreateUserDto
    ): JwtUserTokenHolder
}