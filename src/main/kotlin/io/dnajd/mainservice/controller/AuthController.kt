package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.user.CreateUserDto
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import io.dnajd.mainservice.service.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.AUTH)
class AuthController(
    private val userService: UserService
) {
    @GetMapping
    fun googleSignIn(
        oauthToken: JwtAuthenticationToken
    ): ResponseEntity<Any> {
        val gmail = oauthToken.token.getClaimAsString("email")
        val user = userService.findByGmail(gmail)
        if (user.isPresent) {
            // user exists, return tokens
            val username = userService.findByGmail(gmail).get().username
            return ResponseEntity.status(200).body(JwtUtil.generateUserTokens(username))
        }

        // user doesn't exist, prompt to choose username
        return ResponseEntity.status(204).build()
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun googleSignUp(
        oauthToken: JwtAuthenticationToken,
        @RequestBody userInfo: CreateUserDto
    ): ResponseEntity<Any> {
        val gmail = oauthToken.token.getClaimAsString("email")
        val username = userInfo.username

        if (userService.existsByGmail(gmail)) {
            // User already registered with gmail? something is wrong.
            return ResponseEntity.status(418).body("User with this gmail already exists")
        }

        if (userService.existsByUsername(username)) {
            // Username is already taken
            return ResponseEntity.status(403).body("username taken")
        }

        userService.createUser(username, gmail)
        val tokenHolder = JwtUtil.generateUserTokens(username)

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenHolder)
    }
}