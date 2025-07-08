package io.dnajd.mainservice.config

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
import io.dnajd.mainservice.infrastructure.PreAuthorizeEvaluator
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.repository.ProjectAuthorityRepository
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
) {

    fun hasPermission(
        authentication: Authentication,
        targetId: Serializable,
        evaluatorType: PreAuthorizeEvaluator,
        permissions: List<PreAuthorizePermission>
    ): Boolean {
        val userDetails = authentication.principal as UserDetails
        return when (evaluatorType) {
            PreAuthorizeEvaluator.Project -> {
                hasProjectPermission(userDetails, targetId as Long, permissions)
            }
            PreAuthorizeEvaluator.Table -> {
                hasTablePermission(userDetails, targetId as Long, permissions)
            }
            PreAuthorizeEvaluator.Issue -> {
                hasIssuePermission(userDetails, targetId as Long, permissions)
            }
            PreAuthorizeEvaluator.HasGrantingAuthority -> {
                canGrantProjectAuthority(userDetails, targetId as ProjectAuthorityIdentity, permissions)
            }
            else -> {
                throw ClassNotFoundException(evaluatorType.value)
            }
        }
    }

    fun hasProjectPermission(userDetails: UserDetails, projectId: Long, permissions: List<PreAuthorizePermission>): Boolean {
        val authorities = projectAuthorityRepository.findByUsernameAndProjectId(userDetails.username, projectId)
        if (authorities.any { it.authority == PreAuthorizePermission.Owner.value }) return true
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }

    fun hasTablePermission(userDetails: UserDetails, tableId: Long, permissions: List<PreAuthorizePermission>): Boolean {
        val authorities = projectAuthorityRepository.findByUsernameAndTableId(userDetails.username, tableId)
        if (authorities.any { it.authority == PreAuthorizePermission.Owner.value }) return true
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }

    fun hasIssuePermission(userDetails: UserDetails, issueId: Long, permissions: List<PreAuthorizePermission>): Boolean {
        val authorities = projectAuthorityRepository.findByUsernameAndIssueId(userDetails.username, issueId)
        if (authorities.any { o -> o.authority == PreAuthorizePermission.Owner.value }) return true
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }

    fun canGrantProjectAuthority(userDetails: UserDetails, projectAuthority: ProjectAuthorityIdentity, permissions: List<PreAuthorizePermission>): Boolean {
        val authorities = projectAuthorityRepository.findByUsernameAndProjectId(userDetails.username, projectAuthority.projectId)
        if (authorities.any { it.authority == PreAuthorizePermission.Owner.value }) return true
        if (authorities.none { o -> o.authority == projectAuthority.authority}) return false
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }
}