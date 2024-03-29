package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.Objects

@Entity
@Table(name = "project_table_issue_assigne")
data class ProjectTableIssueAssigne (
    @JsonIgnore
    @EmbeddedId
    var identity: ProjectTableIssueAssigneIdentity = ProjectTableIssueAssigneIdentity(),

    @JsonIgnore
    @Column(name = "issue_id", nullable = false, insertable = false, updatable = false)
    var issueId: Long = -1L,

    @Column(name = "assigner_username", nullable = false, insertable = false, updatable = false)
    var assignerUsername: String = "",

    @Column(name = "assigned_username", nullable = false, insertable = false, updatable = false)
    var assignedUsername: String = "",

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "issue_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var issue: ProjectTableIssue? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProjectTableIssueAssigne

        return identity == other.identity
    }

    override fun hashCode(): Int = Objects.hash(identity)

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(EmbeddedId = $identity , issueId = $issueId , assignerUsername = $assignerUsername , assignedUsername = $assignedUsername )"
    }

}

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProjectTableIssueAssigneIdentity

        if (issueId != other.issueId) return false
        if (assignerUsername != other.assignerUsername) return false
        if (assignedUsername != other.assignedUsername) return false

        return true
    }

    override fun hashCode(): Int {
        var result = issueId.hashCode()
        result = 31 * result + assignerUsername.hashCode()
        result = 31 * result + assignedUsername.hashCode()
        return result
    }

}