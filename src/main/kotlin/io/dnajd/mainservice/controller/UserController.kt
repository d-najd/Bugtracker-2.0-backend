package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Endpoints.USER)
class UserController(
    private val userService: UserService
) {
    @GetMapping("/testing/findAll")
    fun findAll(): List<User> {
        return userService.findAll()
    }

    @GetMapping("/{username}")
    fun getByUsername(
        @PathVariable username: String
    ): UserDto {
        return userService.getByUsername(username)
    }
}