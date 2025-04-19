package io.dnajd.mainservice.domain.project_authority

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(ProjectAuthority::class)
data class ProjectAuthorityDto(
    val username: String? = null,
    val projectId: Long? = null,
    val authority: String? = null,
)