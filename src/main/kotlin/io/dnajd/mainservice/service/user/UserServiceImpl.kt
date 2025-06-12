package io.dnajd.mainservice.service.user

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.user.User
import io.dnajd.mainservice.domain.user.UserDto
import io.dnajd.mainservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
@Transactional
class UserServiceImpl(
    private val repository: UserRepository,
    private val mapper: ShapeShift,
) : UserService {
}