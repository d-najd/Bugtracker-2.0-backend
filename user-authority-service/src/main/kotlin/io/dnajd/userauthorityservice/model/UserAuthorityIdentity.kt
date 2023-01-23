package io.dnajd.userauthorityservice.model

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class UserAuthorityIdentity(
    @Column(name = "user", nullable = false)
    var username: String = "",

    @Column(name = "project_id", nullable = false)
    var projectId: Long = -1L,

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    var authority: UserAuthorityType = UserAuthorityType.ProjectView,
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserAuthorityIdentity

        if (username != other.username) return false
        if (projectId != other.projectId) return false
        if (authority != other.authority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + projectId.hashCode()
        result = 31 * result + authority.hashCode()
        return result
    }

}


/*
@Embeddable
data class ProjectTableIssueAssigneIdentity (
    @Column(name = "issue_id", nullable = false)
    var issueId: Long = -1L,

    // TODO make this reference user
    @Column(name = "assigner_username", nullable = false)
    var assignerUsername: String = "",

    // TODO make this reference user
    @Column(name = "assigned_username", nullable = false)
    var assignedUsername: String = "",
) : Serializable {
    companion object {
        private const val serialVersionUID = -6630648757722119042L
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other == null || this::class.java != other::class.java) return false

        val that: ProjectTableIssueAssigneIdentity = other as ProjectTableIssueAssigneIdentity
        if(issueId == that.issueId) return false
        if(assignerUsername == that.assignerUsername) return false
        return assignedUsername == that.assignedUsername
    }

    override fun hashCode(): Int = javaClass.hashCode()


    @Override
    override fun toString(): String {
        return this::class.simpleName + "(Companion = $Companion , serialVersionUID = $serialVersionUID , issueId = $issueId , assignerUsername = $assignerUsername , assignedUsername = $assignedUsername )"
    }

}

 */