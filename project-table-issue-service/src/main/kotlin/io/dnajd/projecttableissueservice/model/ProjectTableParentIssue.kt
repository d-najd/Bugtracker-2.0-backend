package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*

// TODO optimize this with @Inheritance
@Entity
@Table(name = "project_table_issue")
class ProjectTableParentIssue{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L

    @NotEmpty
    @Column(name = "title")
    var title = ""

    @Column(name = "table_id")
    var tableId = -1L

    @NotEmpty
    @Column(name = "reporter")
    var reporter = ""

    @Column(name = "parent_issue_id")
    var parentIssueId: Long? = null

    @Column(name = "severity")
    var severity = -1

    @Column(name = "position")
    var position = -1

    @Column(name = "description", length = 65534)
    var description: String? = null

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var createdAt: Date = Date()

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var updatedAt: Date = Date()

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "table_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var table: ProjectTable? = null
}
