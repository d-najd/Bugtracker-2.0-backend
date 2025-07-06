package io.dnajd.mainservice.service.project_authority

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.repository.ProjectAuthorityRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjectAuthorityServiceImpl(
    private val repository: ProjectAuthorityRepository,
    private val mapper: ShapeShift
) : ProjectAuthorityService {
    override fun findByUsernameAndProjectId(username: String, projectId: Long): ProjectAuthorityDtoList {
        val persistedAuthorities = repository.findByUsernameAndProjectId(username, projectId)

        return ProjectAuthorityDtoList(mapper.mapCollection(persistedAuthorities))
    }

    fun validityCheck() {

    }

    override fun modifyViewPermission(userDetails: UserDetails, username: String, projectId: Long, value: Boolean) {
        if (value) {
            // modifyPermission(username, projectId, true, PreAuthorizePermission.View)
        }
    }

    override fun modifyPermission(
        userDetails: UserDetails,
        username: String,
        projectId: Long,
        value: Boolean,
        permission: PreAuthorizePermission,
    ) {
        val authority = ProjectAuthority(username, projectId, permission.value)

        if (value) {
            repository.save(authority)
        } else {
            repository.delete(authority)
        }
    }
}