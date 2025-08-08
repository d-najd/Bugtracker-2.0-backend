package io.dnajd.mainservice.domain.table_issue

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.issue_assignee.IssueAssignee
import io.dnajd.mainservice.domain.issue_comment.IssueComment
import io.dnajd.mainservice.domain.issue_label.IssueLabel
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.infrastructure.ImplicitCollectionMappingTransformerFixed
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import io.dnajd.mainservice.infrastructure.mapper.LazyInitializedCondition
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Entity
@Table(
    name = "project_table_issue",
    uniqueConstraints = [UniqueConstraint(columnNames = ["table_id", "position"])]
)
@AutoMapping(TableIssueDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(TableIssueDto::class)
data class TableIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    @Column
    val tableId: Long = -1L,

    @Column
    val reporter: String = "",

    @Nullable
    @Column(nullable = true)
    val parentIssueId: Long? = null,

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED")
    @Min(0)
    @Max(5)
    @NotNull
    // @Type(type = "org.hibernate.type.IntegerType")
    val severity: Int = -1,

    @Column(nullable = false, length = 255)
    @NotEmpty
    @Size(max = 255)
    val title: String = "",

    @NotNull
    @Column(nullable = false)
    @Min(0)
    val position: Int = -1,

    @NotBlank
    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @Column(nullable = false)
    @NotNull
    @CreationTimestamp
    val createdAt: Date? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @Column
    @NotNull
    val updatedAt: Date? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tableId", insertable = false, updatable = false)
    val table: ProjectTable? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(
        name = "parentIssueId",
        updatable = false,
        insertable = false,
    )
    @MappedField(DontMapCondition::class)
    val parentIssue: TableIssue? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "parentIssue",
        fetch = FetchType.LAZY
    )
    @MappedField(
        condition = LazyInitializedCondition::class,
        transformer = ImplicitCollectionMappingTransformerFixed::class
    )
    val childIssues: Set<TableIssue> = emptySet(),

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
    )
    @JoinColumn(name = "issueId")
    @MappedField(
        condition = LazyInitializedCondition::class,
        transformer = ImplicitCollectionMappingTransformerFixed::class
    )
    val assigned: Set<IssueAssignee> = emptySet(),

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
    )
    @JoinColumn(name = "issueId")
    @MappedField(
        condition = LazyInitializedCondition::class,
        transformer = ImplicitCollectionMappingTransformerFixed::class
    )
    val comments: Set<IssueComment> = emptySet(),

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
    )
    @JoinColumn(name = "issueId")
    @MappedField(
        condition = LazyInitializedCondition::class,
        transformer = ImplicitCollectionMappingTransformerFixed::class
    )
    val labels: Set<IssueLabel> = emptySet(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val another: TableIssue = other as TableIssue
        return another.id == id &&
                another.tableId == tableId &&
                Objects.equals(another.reporter, reporter) &&
                another.parentIssueId == parentIssueId &&
                another.severity == severity &&
                Objects.equals(another.title, title) &&
                another.position == position &&
                Objects.equals(another.description, description) &&
                Objects.equals(another.createdAt, createdAt) &&
                Objects.equals(another.updatedAt, updatedAt)
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            tableId,
            reporter,
            parentIssueId,
            severity,
            parentIssueId,
            severity,
            title,
            position,
            description,
            createdAt,
            updatedAt
        )
    }

    companion object {
        fun entityGraph(
            includeChildIssues: Boolean = false,
            includeAssigned: Boolean = false,
            includeComments: Boolean = false,
            includeLabels: Boolean = false,
            graphType: EntityGraphType = EntityGraphType.LOAD,
        ): EntityGraph {
            val graph = DynamicEntityGraph.builder(graphType)

            if (includeChildIssues) {
                graph.addPath(TableIssue::childIssues.name)
            }
            if (includeAssigned) {
                graph.addPath(TableIssue::assigned.name)
            }
            if (includeComments) {
                graph.addPath(TableIssue::comments.name)
            }
            if (includeLabels) {
                graph.addPath(TableIssue::labels.name)
            }

            return graph.build()
        }
    }
}