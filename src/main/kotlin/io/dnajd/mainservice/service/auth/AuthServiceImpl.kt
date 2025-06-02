package io.dnajd.mainservice.service.auth

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.token.JwtUserTokenHolder
import io.dnajd.mainservice.domain.user.CreateUserDto
import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.infrastructure.exception.UserAlreadyExistsException
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException
import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import io.dnajd.mainservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
@Transactional
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val mapper: ShapeShift,
) : AuthService {
    override fun googleSignIn(oauthToken: JwtAuthenticationToken): JwtUserTokenHolder {
        val gmail = oauthToken.token.getClaimAsString("email")
        val user = userRepository.findByGmail(gmail)
        if (user.isEmpty) {
            throw UserNotFoundException()
        }

        return JwtUtil.generateUserTokens(username = user.get().username)
    }

    override fun googleSignUp(
        oauthToken: JwtAuthenticationToken,
        userInfo: CreateUserDto
    ): JwtUserTokenHolder {
        val gmail = oauthToken.token.getClaimAsString("email")
        val username = userInfo.username

        if (userRepository.existsByGmail(gmail)) {
            // User already registered with gmail? something is wrong.
            throw UserAlreadyExistsException("gmail taken")
        }

        if (userRepository.existsByUsername(username)) {
            // Username is already taken
            throw UserAlreadyExistsException("username taken")
        }

        val user = User(
            username = username,
            gmail = gmail
        )

        val persistedUser = userRepository.save(user)
        return JwtUtil.generateUserTokens(username = username)
    }
}