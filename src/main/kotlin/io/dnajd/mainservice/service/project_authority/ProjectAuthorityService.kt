package io.dnajd.mainservice.service.project_authority

import io.dnajd.mainservice.domain.project_authority.ProjectAuthority

interface ProjectAuthorityService {
    fun findAll(): List<ProjectAuthority>
}