package io.dnajd.mainservice.service.projectauthority

import io.dnajd.mainservice.domain.projectauthority.ProjectAuthorityDtoList
import io.dnajd.mainservice.domain.projectauthority.ProjectAuthorityIdentity
import org.springframework.security.core.userdetails.UserDetails

interface ProjectAuthorityService {
    fun findAllByProjectId(projectId: Long): ProjectAuthorityDtoList

    /**
     * Manager and owner are allowed to call this
     */
    fun modifyUserAuthority(userDetails: UserDetails, projectAuthorityId: ProjectAuthorityIdentity, value: Boolean)

    /**
     * Only owner is able to call this
     */
    fun modifyManagerAuthority(userDetails: UserDetails, projectAuthorityId: ProjectAuthorityIdentity, value: Boolean)
}