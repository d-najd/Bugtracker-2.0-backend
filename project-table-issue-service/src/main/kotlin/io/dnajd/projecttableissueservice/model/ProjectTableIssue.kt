package io.dnajd.projecttableissueservice.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*


@Entity
@Table(name = "project_table_issue")
class ProjectTableIssue{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id = -1L

    @Column(name = "table_id")
    var tableId = -1L

    @NotEmpty
    @Column(name = "reporter")
    var reporter = ""

    @Column(name = "parent_issue_id")
    var parentIssueId: Long? = null

    @Column(name = "severity")
    var severity = -1

    @NotEmpty
    @Column(name = "title")
    var title = ""

    @Column(name = "position")
    var position = -1

    @Column(name = "description", length = 65535)
    var description: String? = null

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var createdAt: Date = Date()

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var updatedAt: Date = Date()
}