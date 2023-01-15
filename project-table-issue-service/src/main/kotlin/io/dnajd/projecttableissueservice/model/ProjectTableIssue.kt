package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*

@Entity
@Table(
    name = "project_table_issue",
    /*
        maintaining this proved to be more pain than its worth since you CANT SWAP VALUES BETWEEN ROWS which forces the
        position to be temporarily set to -1 which leads to more problems and breaking points which
        defeats the whole purpose of the constraint
     */
    /*
    uniqueConstraints = [
        UniqueConstraint(name = "project_table_issue_unique_1", columnNames = ["table_id", "position"]),
    ]
     */
)
class ProjectTableIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L

    @NotEmpty
    @Column(name = "title")
    var title = ""

    @Column(name = "table_id")
    var tableId = -1L

    //TODO create many-to-one relationship with user table
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

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        mappedBy = "issue",
        fetch = FetchType.LAZY
    )
    var comments: MutableList<ProjectTableIssueComment> = mutableListOf()

    @ManyToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "project_table_issue_label",
        joinColumns = [JoinColumn(name = "item_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "label_id", referencedColumnName = "id")]
    )
    var labels: MutableList<ProjectLabel> = mutableListOf()

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "table_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var table: ProjectTable? = null

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        mappedBy = "parentIssue",
        fetch = FetchType.LAZY
    )
    var childIssues: MutableList<ProjectTableChildIssue> = mutableListOf()

}