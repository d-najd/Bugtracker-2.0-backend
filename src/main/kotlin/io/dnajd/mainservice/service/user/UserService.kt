package io.dnajd.mainservice.service.user

import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import java.util.*

interface UserService {
    fun findAllTesting(): List<User>

    fun findByUsername(username: String): User

    fun existsByGmail(gmail: String): Boolean

    fun findByGmail(gmail: String): Optional<User>

    fun existsByUsername(username: String): Boolean

    fun getByUsername(username: String): UserDto

    fun createUser(username: String, gmail: String): UserDto
}