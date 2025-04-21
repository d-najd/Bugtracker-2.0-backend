package io.dnajd.mainservice.service.user

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class UserServiceImpl(
    private val repository: UserRepository,
    private val mapper: ShapeShift,
) : UserService {
    override fun findAll(): List<User> {
        return repository.findAll()
    }

    override fun findByUsername(username: String): User {
        return repository.findByUsername(username).orElseThrow {
            throw ResourceNotFoundException(User::class)
        }
    }

    override fun getByUsername(username: String): UserDto {
        return mapper.map(findByUsername(username))
    }
}