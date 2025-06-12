package io.dnajd.mainservice.service.user

import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface UserService: UserDetailsService {
}