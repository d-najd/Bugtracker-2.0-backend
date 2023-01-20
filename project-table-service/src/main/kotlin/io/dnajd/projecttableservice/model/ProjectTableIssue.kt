package io.dnajd.projecttableservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.Hibernate

@Entity
@Table(
    name = "project_table_issue",
    /*
    uniqueConstraints = [
        UniqueConstraint(name = "project_table_issue_unique_1", columnNames = ["table_id", "position"]),
    ]
     */
)
data class ProjectTableIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L,

    @NotEmpty
    @Column(name = "title")
    var title: String = "",

    @JsonIgnore
    @Column(name = "table_id")
    var tableId: Long = -1L,

    @Column(name = "parent_issue_id")
    var parentIssueId: Long? = null,

    @Column(name = "severity")
    var severity: Int = 1,

    @Column(name = "position")
    var position: Int = -1,

    @ManyToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "project_table_issue_label",
        joinColumns = [JoinColumn(name = "item_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "label_id", referencedColumnName = "id")]
    )
    var labels: MutableList<ProjectLabel> = mutableListOf(),

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "table_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var table: ProjectTable? = null,

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        mappedBy = "parentIssue",
        fetch = FetchType.LAZY
    )
    var childIssues: MutableList<ProjectTableChildIssue> = mutableListOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProjectTableIssue

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , tableId = $tableId , parentIssueId = $parentIssueId , severity = $severity , position = $position )"
    }

}