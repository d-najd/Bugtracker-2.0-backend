package io.dnajd.mainservice.domain.user

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import java.util.*

@DefaultMappingTarget(User::class)
data class UserDto(
    var username: String? = null,
    var createdAt: Date? = null,
)