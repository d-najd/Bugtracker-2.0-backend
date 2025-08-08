package io.dnajd.mainservice.infrastructure

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
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
class ScopedPermissionEvaluator(
    private val repository: ProjectAuthorityRepository
) {
    fun hasPermission(
        authentication: Authentication,
        targetId: Serializable,
        evaluatorType: ScopedEvaluatorType,
        permissions: List<ScopedPermission>
    ): Boolean {
        val userDetails = authentication.principal as UserDetails
        return when (evaluatorType) {
            ScopedEvaluatorType.Project -> {
                hasProjectPermission(userDetails, targetId as Long, permissions)
            }
            ScopedEvaluatorType.Table -> {
                hasTablePermission(userDetails, targetId as Long, permissions)
            }
            ScopedEvaluatorType.Issue -> {
                hasIssuePermission(userDetails, targetId as Long, permissions)
            }
            ScopedEvaluatorType.IssueComment -> {
                hasIssueCommentPermission(userDetails, targetId as Long, permissions)
            }
            ScopedEvaluatorType.HasGrantingAuthority -> {
                canGrantProjectAuthority(userDetails, targetId as ProjectAuthorityIdentity, permissions)
            }
            else -> {
                throw ClassNotFoundException(evaluatorType.value)
            }
        }
    }

    fun hasProjectPermission(userDetails: UserDetails, projectId: Long, permissions: List<ScopedPermission>): Boolean {
        val authorities = repository.findByUsernameAndProjectId(userDetails.username, projectId)
        if (authorities.any { it.authority == ScopedPermission.Owner.value }) return true
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }

    fun hasTablePermission(userDetails: UserDetails, tableId: Long, permissions: List<ScopedPermission>): Boolean {
        val authorities = repository.findByUsernameAndTableId(userDetails.username, tableId)
        if (authorities.any { it.authority == ScopedPermission.Owner.value }) return true
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }

    fun hasIssuePermission(userDetails: UserDetails, issueId: Long, permissions: List<ScopedPermission>): Boolean {
        val authorities = repository.findByUsernameAndIssueId(userDetails.username, issueId)
        if (authorities.any { o -> o.authority == ScopedPermission.Owner.value }) return true
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }

    fun hasIssueCommentPermission(userDetails: UserDetails, issueCommentId: Long, permissions: List<ScopedPermission>): Boolean {
        val authorities = repository.findByUsernameAndIssueCommentId(userDetails.username, issueCommentId)
        if (authorities.any { it.authority == ScopedPermission.Owner.value }) return true
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }

    fun canGrantProjectAuthority(userDetails: UserDetails, projectAuthority: ProjectAuthorityIdentity, permissions: List<ScopedPermission>): Boolean {
        val authorities = repository.findByUsernameAndProjectId(userDetails.username, projectAuthority.projectId)
        if (authorities.any { it.authority == ScopedPermission.Owner.value }) return true
        if (authorities.none { o -> o.authority == projectAuthority.authority}) return false
        return permissions.all { o -> authorities.any { o.value == it.authority } }
    }
}