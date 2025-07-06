package io.dnajd.mainservice.service.project_authority

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission

interface ProjectAuthorityService {
    fun findByUsernameAndProjectId(username: String, projectId: Long): ProjectAuthorityDtoList

    fun modifyPermission(username: String, projectId: Long, permission: PreAuthorizePermission, value: Boolean)
}