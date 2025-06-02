package io.dnajd.mainservice.service.user

import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import java.util.*

interface UserService {
    fun findAllTesting(): List<User>

    fun getByUsername(username: String): UserDto
}