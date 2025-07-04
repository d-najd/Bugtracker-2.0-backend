package io.dnajd.mainservice.domain.user

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import kotlin.collections.HashSet

@DefaultMappingTarget(User::class)
data class UserDto(
    val username: String? = null,
    val gmail: String? = null,
    val createdAt: Date? = null,
    val projectAuthorities: MutableList<ProjectAuthorityDto>? = null,
)