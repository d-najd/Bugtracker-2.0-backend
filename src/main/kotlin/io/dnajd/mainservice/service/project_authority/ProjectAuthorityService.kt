package io.dnajd.mainservice.service.project_authority

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import org.springframework.security.core.userdetails.UserDetails

interface ProjectAuthorityService {
    fun findByUsernameAndProjectId(username: String, projectId: Long): ProjectAuthorityDtoList

    fun modifyViewPermission(userDetails: UserDetails, username: String, projectId: Long, value: Boolean)

    fun modifyPermission(userDetails: UserDetails, username: String, projectId: Long, value: Boolean, permission: PreAuthorizePermission)
}