package io.dnajd.mainservice.config

import io.dnajd.mainservice.domain.project.ProjectDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.repository.ProjectAuthorityRepository
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable

/**
 * The user by itself is not enough to figure out the authority but rather we need info about which project he is trying
 * to modify.
 */
@Component
class CustomPermissionEvaluator(
    private val projectAuthorityRepository: ProjectAuthorityRepository
) : PermissionEvaluator {

    override fun hasPermission(
        authentication: Authentication,
        targetDomainObject: Any,
        permission: Any
    ): Boolean {
        val userDetails = authentication.principal as UserDetails
        return when(targetDomainObject) {
            is ProjectDto -> {
                hasProjectPermission(userDetails, targetDomainObject.id!!, permission)
            }

            is ProjectTableDto -> {
                hasProjectTablePermission(userDetails, targetDomainObject.id!!, permission)
            }

            else -> {
                throw ClassNotFoundException(targetDomainObject::class.simpleName)
            }
        }
    }

    override fun hasPermission(
        authentication: Authentication,
        targetId: Serializable,
        targetType: String,
        permission: Any
    ): Boolean {
        val userDetails = authentication.principal as UserDetails
        return when (targetType) {
            PreAuthorizeType.Project.value -> {
                hasProjectPermission(userDetails, targetId as Long, permission)
            }

            PreAuthorizeType.ProjectTable.value -> {
                hasProjectTablePermission(userDetails, targetId as Long, permission)
            }

            else -> {
                throw ClassNotFoundException(targetType)
            }
        }
    }

    fun hasProjectPermission(userDetails: UserDetails, projectId: Long, permission: Any): Boolean {
        return projectAuthorityRepository.findByUsernameAndProjectId(userDetails.username, projectId)
            .any { o -> o.authority == permission || o.authority == PreAuthorizePermission.Owner.value }
    }

    fun hasProjectTablePermission(userDetails: UserDetails, projectTableId: Long, permission: Any): Boolean {
        return projectAuthorityRepository.findByUsernameAndProjectTableId(userDetails.username, projectTableId)
            .any { o -> o.authority == permission || o.authority == PreAuthorizePermission.Owner.value }
    }
}