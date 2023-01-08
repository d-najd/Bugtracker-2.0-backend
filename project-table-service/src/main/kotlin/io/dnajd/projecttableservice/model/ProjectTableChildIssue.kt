package io.dnajd.projecttableservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

// TODO optimize this with @Inheritance
@Entity
@Table(name = "project_table_issue")
class ProjectTableChildIssue{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L

    @JsonIgnore
    @Column(name = "table_id")
    var tableId = -1L

    @JsonIgnore
    @Column(name = "parent_issue_id")
    var parentIssueId: Long? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "table_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var table: ProjectTable? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_issue_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var parentIssue: ProjectTableIssue? = null
}
