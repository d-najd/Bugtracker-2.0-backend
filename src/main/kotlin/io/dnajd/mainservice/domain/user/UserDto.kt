package io.dnajd.mainservice.domain.user

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import io.dnajd.mainservice.domain.projectauthority.ProjectAuthorityDto
import java.util.*

@DefaultMappingTarget(User::class)
data class UserDto(
	val username: String? = null,
	val gmail: String? = null,
	val createdAt: Date? = null,
	val projectAuthorities: MutableList<ProjectAuthorityDto>? = null,
)
