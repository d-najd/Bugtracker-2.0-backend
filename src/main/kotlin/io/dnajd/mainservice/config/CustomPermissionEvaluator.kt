package io.dnajd.mainservice.config

import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.repository.ProjectAuthorityRepository
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable

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

        when (targetDomainObject) {
            is Project -> {
                return hasProjectPermission(userDetails, targetDomainObject.id, permission)
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

        when (targetType) {
            "Project" -> {
                return hasProjectPermission(userDetails, targetId as Long, permission)
            }

            else -> {
                throw ClassNotFoundException(targetType)
            }
        }
    }

    fun hasProjectPermission(userDetails: UserDetails, projectId: Long, permission: Any): Boolean {
        return projectAuthorityRepository.findByUsernameAndProjectId(userDetails.username, projectId)
            .any { o -> o.authority == permission }
    }
}