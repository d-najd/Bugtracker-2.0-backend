package io.dnajd.mainservice.service.user

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class UserServiceImpl(
    private val repository: UserRepository,
    private val mapper: ShapeShift,
) : UserService {
    override fun findAllTesting(): List<User> {
        return repository.findAll()
    }

    override fun findByUsername(username: String): User {
        return repository.findByUsername(username).orElseThrow {
            throw ResourceNotFoundException(User::class)
        }
    }

    override fun existsByGmail(gmail: String): Boolean {
        return repository.existsByGmail(gmail)
    }

    override fun findByGmail(gmail: String): Optional<User> {
        return repository.findByGmail(gmail)
    }

    override fun existsByUsername(username: String): Boolean {
        return repository.existsByUsername(username)
    }

    override fun getByUsername(username: String): UserDto {
        return mapper.map(findByUsername(username))
    }

    override fun createUser(username: String, gmail: String): UserDto {
        val user = User(
            username = username,
            gmail = gmail
        )

        val persistedUser = repository.save(user)
        return mapper.map(persistedUser)
    }
}