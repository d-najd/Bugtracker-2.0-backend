package io.dnajd.mainservice.service.user

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val mapper: ShapeShift,
) : UserService {
    companion object {
        private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }

    override fun findAll(): List<User> {
        return userRepository.findAll()
    }

    override fun findByUsername(username: String): User {
        return userRepository.findByUsername(username).orElseThrow {
            log.error("Resource ${User::class.simpleName} with username $username not found")
            throw ResourceNotFoundException(User::class)
        }
    }

    override fun getByUsername(username: String): UserDto {
        return mapper.map(findByUsername(username))
    }
}