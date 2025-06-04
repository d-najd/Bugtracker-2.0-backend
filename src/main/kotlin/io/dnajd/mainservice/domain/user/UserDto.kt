package io.dnajd.mainservice.domain.user

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@DefaultMappingTarget(User::class)
data class UserDto(
    var username: String? = null,
    var gmail: String? = null,
    var createdAt: Date? = null,
    var projectAuthorities: MutableList<ProjectAuthorityDto>? = null,
)