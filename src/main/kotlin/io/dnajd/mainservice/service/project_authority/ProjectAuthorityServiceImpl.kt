package io.dnajd.mainservice.service.project_authority

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
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
    override fun findByUsernameAndProjectId(username: String, projectId: Long): ProjectAuthorityDtoList {
        val persistedAuthorities = repository.findByUsernameAndProjectId(username, projectId)

        return ProjectAuthorityDtoList(mapper.mapCollection(persistedAuthorities))
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
        val userManaged = repository.findByUsernameAndProjectId(
            projectAuthorityId.username,
            projectAuthorityId.projectId
        )

        if (userManaged.any { it.authority == PreAuthorizePermission.Manage.value || it.authority == PreAuthorizePermission.Owner.value }) {
            throw AccessDeniedException("Can't modify manager or owner permissions")
        }

        if (value) {
            repository.save(projectAuthority)
        } else if (projectAuthorityId.authority == PreAuthorizePermission.View.value) {
            // Removing view authority removes all other authorities
            repository.deleteAllByUsernameAndProjectId(projectAuthority.username, projectAuthority.projectId)
        } else {
            repository.delete(projectAuthority)
        }
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
        val userManaged = repository.findByUsernameAndProjectId(
            projectAuthorityId.username,
            projectAuthorityId.projectId
        )

        if (userManaged.any { it.authority == PreAuthorizePermission.Owner.value }) {
            throw AccessDeniedException("Can't modify owner permissions")
        }

        if (value) {
            repository.save(projectAuthority)
        } else if (projectAuthorityId.authority == PreAuthorizePermission.View.value) {
            // Removing view authority removes all other authorities
            repository.deleteAllByUsernameAndProjectId(projectAuthority.username, projectAuthority.projectId)
        } else {
            repository.delete(projectAuthority)
        }
    }
}