package io.dnajd.mainservice.service.google_auth

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.token.JwtTokenHolder
import io.dnajd.mainservice.domain.user.CreateUserDto
import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.infrastructure.exception.UserAlreadyExistsException
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException
import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import io.dnajd.mainservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
@Transactional
class GoogleAuthServiceImpl(
    private val userRepository: UserRepository,
    private val mapper: ShapeShift,
) : GoogleAuthService {
    override fun googleSignIn(oauthToken: JwtAuthenticationToken): JwtTokenHolder {
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
    ): JwtTokenHolder {
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

        val persistedUser = userRepository.saveAndFlush(user)
        return JwtUtil.generateUserTokens(username = username)
    }
}