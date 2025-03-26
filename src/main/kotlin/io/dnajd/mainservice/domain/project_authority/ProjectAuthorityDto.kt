package io.dnajd.mainservice.domain.project_authority

import io.dnajd.mainservice.domain.authority.AuthorityType

data class ProjectAuthorityDto(
    val username: String? = null,
    val projectId: Long? = null,
    val authority: AuthorityType? = null,
)