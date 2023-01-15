package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable
import java.util.Objects


@Entity
@Table(name = "project_table_issue_assigne")
class ProjectTableIssueAssigne {

    @Column(name = "issue_id", nullable = false, insertable = false, updatable = false)
    var issueId: Long = -1L

    @Column(name = "assigner_username", nullable = false, insertable = false, updatable = false)
    var assignerUsername: String = ""

    @Column(name = "assigned_username", nullable = false, insertable = false, updatable = false)
    var assignedUsername: String = ""

    @JsonIgnore
    @EmbeddedId
    var identity: ProjectTableIssueAssigneIdentity = ProjectTableIssueAssigneIdentity()

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "issue_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var issue: ProjectTableIssue? = null
}

@Embeddable
class ProjectTableIssueAssigneIdentity : Serializable {
    @Column(name = "issue_id", nullable = false)
    var issueId: Long = -1L

    // TODO make this reference user
    @Column(name = "assigner_username", nullable = false)
    var assignerUsername: String = ""

    // TODO make this reference user
    @Column(name = "assigned_username", nullable = false)
    var assignedUsername: String = ""

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other == null || this::class.java != other::class.java) return false

        val that: ProjectTableIssueAssigneIdentity = other as ProjectTableIssueAssigneIdentity
        if(issueId == that.issueId) return false
        if(assignerUsername == that.assignerUsername) return false
        return assignedUsername == that.assignedUsername
    }

    override fun hashCode(): Int {
        return Objects.hash(issueId, assignerUsername, assignedUsername)
    }

    companion object {
        private const val serialVersionUID = -6630648757722119042L
    }
}