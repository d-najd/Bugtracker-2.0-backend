package io.dnajd.projecttableservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate

// TODO optimize this with @Inheritance
@Entity
@Table(name = "project_table_issue")
data class ProjectTableChildIssue (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L,

    @JsonIgnore
    @Column(name = "table_id")
    var tableId: Long = -1L,

    @JsonIgnore
    @Column(name = "parent_issue_id")
    var parentIssueId: Long? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "table_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var table: ProjectTable? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_issue_id",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var parentIssue: ProjectTableIssue? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProjectTableChildIssue

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , tableId = $tableId , parentIssueId = $parentIssueId )"
    }
}
