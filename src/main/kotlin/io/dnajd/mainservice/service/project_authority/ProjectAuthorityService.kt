package io.dnajd.mainservice.service.project_authority

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
import org.springframework.security.core.userdetails.UserDetails

interface ProjectAuthorityService {
    fun findByUsernameAndProjectId(username: String, projectId: Long): ProjectAuthorityDtoList

    /**
     * Manager and owner are allowed to call this
     */
    fun modifyUserAuthority(userDetails: UserDetails, projectAuthorityId: ProjectAuthorityIdentity, value: Boolean)

    /**
     * Only owner is able to call this
     */
    fun modifyManagerAuthority(userDetails: UserDetails, projectAuthorityId: ProjectAuthorityIdentity, value: Boolean)
}