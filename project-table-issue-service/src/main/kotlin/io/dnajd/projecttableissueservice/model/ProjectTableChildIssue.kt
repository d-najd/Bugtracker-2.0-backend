package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.Hibernate
import java.util.*

// TODO optimize this with @Inheritance
@Entity
@Table(name = "project_table_issue")
data class ProjectTableChildIssue (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L,

    @NotEmpty
    @Column(name = "title")
    var title: String = "",

    @Column(name = "table_id")
    var tableId: Long = -1L,

    @JsonIgnore
    @NotEmpty
    @Column(name = "reporter")
    var reporter: String = "",

    @JsonIgnore
    @Column(name = "parent_issue_id")
    var parentIssueId: Long? = null,

    @JsonIgnore
    @Column(name = "severity")
    var severity: Int = -1,

    @JsonIgnore
    @Column(name = "position")
    var position: Int = -1,

    @JsonIgnore
    @Column(name = "description", length = 65534)
    var description: String? = null,

    @JsonIgnore
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    var createdAt: Date = Date(),

    @JsonIgnore
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    var updatedAt: Date? = null,

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
        return this::class.simpleName + "(id = $id , title = $title , tableId = $tableId , reporter = $reporter , parentIssueId = $parentIssueId , severity = $severity , position = $position , description = $description , createdAt = $createdAt , updatedAt = $updatedAt )"
    }
}
