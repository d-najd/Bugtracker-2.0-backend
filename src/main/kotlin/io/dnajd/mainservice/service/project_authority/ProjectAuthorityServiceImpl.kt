package io.dnajd.mainservice.service.project_authority

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
import io.dnajd.mainservice.infrastructure.ScopedPermission
import io.dnajd.mainservice.repository.ProjectAuthorityRepository
import io.dnajd.mainservice.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.AccessDeniedException

@Service
@Transactional
class ProjectAuthorityServiceImpl(
    private val repository: ProjectAuthorityRepository,
    private val userRepository: UserRepository,
    private val mapper: ShapeShift
) : ProjectAuthorityService {
    override fun findAllByProjectId(projectId: Long): ProjectAuthorityDtoList {
        val persistedUserAuthorities = repository.findByProjectId(projectId)

        return ProjectAuthorityDtoList(mapper.mapCollection(persistedUserAuthorities))
    }

    override fun modifyUserAuthority(
        userDetails: UserDetails,
        projectAuthorityId: ProjectAuthorityIdentity,
        value: Boolean
    ) {
        val projectAuthority = ProjectAuthority(
            username = projectAuthorityId.username,
            projectId = projectAuthorityId.projectId,
            authority = projectAuthorityId.authority
        )
        val userManagedAuthorities = repository.findByUsernameAndProjectId(
            projectAuthorityId.username,
            projectAuthorityId.projectId
        )

        if (userManagedAuthorities.any { it.authority == ScopedPermission.Manage.value || it.authority == ScopedPermission.Owner.value }) {
            throw AccessDeniedException("Can't modify manager or owner permissions")
        }

        sharedModifyAuthority(userManagedAuthorities, projectAuthority, value)
    }

    override fun modifyManagerAuthority(
        userDetails: UserDetails,
        projectAuthorityId: ProjectAuthorityIdentity,
        value: Boolean
    ) {
        val projectAuthority = ProjectAuthority(
            username = projectAuthorityId.username,
            projectId = projectAuthorityId.projectId,
            authority = projectAuthorityId.authority
        )
        val userManagedAuthorities = repository.findByUsernameAndProjectId(
            projectAuthorityId.username,
            projectAuthorityId.projectId
        )

        if (userManagedAuthorities.any { it.authority == ScopedPermission.Owner.value }) {
            throw AccessDeniedException("Can't modify owner permissions")
        }

        sharedModifyAuthority(userManagedAuthorities, projectAuthority, value)
    }

    /**
     * Shared logic for saving and removing authorities
     */
    private fun sharedModifyAuthority(
        userManagedAuthorities: List<ProjectAuthority>,
        projectAuthority: ProjectAuthority,
        value: Boolean
    ) {
        if (value) {
            // adding any authority should also add view to the project if it isn't already added
            if (userManagedAuthorities.none { it.authority == ScopedPermission.View.value }) {
                val viewAuthority = ProjectAuthority(username = projectAuthority.username, projectId = projectAuthority.projectId, ScopedPermission.View.value)
                repository.saveAllAndFlush(listOf(viewAuthority, projectAuthority))
            } else {
                repository.saveAndFlush(projectAuthority)
            }
        } else if (projectAuthority.authority == ScopedPermission.View.value) {
            // Removing view authority removes all other authorities
            repository.deleteAllByUsernameAndProjectId(projectAuthority.username, projectAuthority.projectId)
        } else {
            repository.delete(projectAuthority)
        }
    }
}