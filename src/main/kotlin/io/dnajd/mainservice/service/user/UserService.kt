package io.dnajd.mainservice.service.user

import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto

interface UserService {
    fun findAll(): List<User>

    fun findByUsername(username: String): User

    fun getByUsername(username: String): UserDto
}