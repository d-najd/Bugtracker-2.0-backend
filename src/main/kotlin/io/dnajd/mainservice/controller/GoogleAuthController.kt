package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.user.CreateUserDto
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.exception.UserAlreadyExistsException
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException
import io.dnajd.mainservice.service.google_auth.GoogleAuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

/**
 * Controller used for Google OAuth tokens
 */
@RestController
@RequestMapping(Endpoints.GOOGLE_AUTH)
class GoogleAuthController(
    private val service: GoogleAuthService
) {
    @GetMapping
    fun googleSignIn(
        oauthToken: JwtAuthenticationToken
    ): ResponseEntity<Any> {
        try {
            // User exists, return tokens
            val tokenHolder = service.googleSignIn(oauthToken)
            return ResponseEntity.status(200).body(tokenHolder)
        } catch (e: UserNotFoundException) {
            // user doesn't exist, prompt to choose username and sign up
            return ResponseEntity.status(204).build()
        }
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun googleSignUp(
        oauthToken: JwtAuthenticationToken,
        @RequestBody userInfo: CreateUserDto
    ): ResponseEntity<Any> {
        try {
            // User exists, return tokens
            val tokenHolder = service.googleSignUp(oauthToken, userInfo)
            return ResponseEntity.status(HttpStatus.CREATED).body(tokenHolder)
        } catch (e: UserAlreadyExistsException) {
            // User with gmail or username already exists, prompt user to pick another
            return ResponseEntity.status(403).build()
        }
    }
}