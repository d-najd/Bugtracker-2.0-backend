package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*

@Entity
@Table(name = "project_table_issue_comment")
class ProjectTableIssueComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L

    @NotEmpty
    @Column(name = "user")
    var user = ""
    
    @Column(name = "issue_id")
    var issueId = -1L

    @Column(name = "message", length = 65534)
    var message = ""

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var createdAt: Date = Date()

    @Column(name = "edited_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var editedAt: Date? = null

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
