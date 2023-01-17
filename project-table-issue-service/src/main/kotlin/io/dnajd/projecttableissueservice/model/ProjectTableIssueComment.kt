package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table(name = "project_table_issue_comment")
data class ProjectTableIssueComment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L,

    @NotEmpty
    @Column(name = "user")
    var user: String = "",

    @JsonIgnore
    @Column(name = "issue_id")
    var issueId: Long = -1L,

    @Column(name = "message", length = 65534)
    var message: String = "",

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    var createdAt: Date = Date(),

    @Column(name = "edited_at")
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    var editedAt: Date? = null,

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
        other as ProjectTableIssueComment

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , user = $user , issueId = $issueId , message = $message , createdAt = $createdAt , editedAt = $editedAt )"
    }
}
