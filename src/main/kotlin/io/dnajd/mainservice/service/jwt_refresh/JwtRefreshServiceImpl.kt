package io.dnajd.mainservice.service.jwt_refresh

import io.dnajd.mainservice.domain.token.JwtTokenHolder
import io.dnajd.mainservice.infrastructure.exception.UserNotFoundException
import io.dnajd.mainservice.infrastructure.jwt.JwtUtil
import io.dnajd.mainservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class JwtRefreshServiceImpl(
    private val userRepository: UserRepository,
) : JwtRefreshService {
    override fun refreshAccessToken(username: String): JwtTokenHolder {
        val user = userRepository.findByUsername(username)
        if (user.isEmpty) {
            throw UserNotFoundException()
        }

        return JwtUtil.refreshAccessToken(username)
    }

    override fun refreshAccessAndRefreshTokens(refreshToken: String): JwtTokenHolder {
        val username = JwtUtil.getUsernameFromToken(refreshToken)
        val user = userRepository.findByUsername(username)
        if (user.isEmpty) {
            throw UserNotFoundException()
        }

        return JwtUtil.refreshAccessAndRefreshTokens(refreshToken)
    }
}