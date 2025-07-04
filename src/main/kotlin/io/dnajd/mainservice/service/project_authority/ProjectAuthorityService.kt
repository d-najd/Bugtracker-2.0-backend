package io.dnajd.mainservice.service.project_authority

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList

interface ProjectAuthorityService {
    fun findByUsernameAndProjectId(username: String, projectId: Long): ProjectAuthorityDtoList
}