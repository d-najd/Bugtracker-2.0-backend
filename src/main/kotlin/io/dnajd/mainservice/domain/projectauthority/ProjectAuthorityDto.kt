package io.dnajd.mainservice.domain.projectauthority

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(ProjectAuthority::class)
data class ProjectAuthorityDto(
    val username: String? = null,
    val projectId: Long? = null,
    val authority: String? = null,
)

class ProjectAuthorityDtoList(val data: List<ProjectAuthorityDto>)