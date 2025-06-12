package io.dnajd.mainservice.controller

import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.user.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Not to be used for returning tokens
 * @see GoogleAuthController
 * @see JwtRefreshAuthController
 */
@RestController
@RequestMapping(Endpoints.USER)
class UserController(
    private val service: UserService
) {

}