package io.dnajd.mainservice.domain.table_issue

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import dev.krud.shapeshift.transformer.ImplicitCollectionMappingTransformer
import io.dnajd.mainservice.domain.issue_assignee.IssueAssignee
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import io.dnajd.mainservice.infrastructure.mapper.LazyInitializedCondition
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
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
    var id: Long = -1L,

    @Column
    var tableId: Long = -1L,

    @Column
    var reporter: String = "",

    @Nullable
    @Column(nullable = true)
    var parentIssueId: Long? = null,

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED")
    @Min(0)
    @Max(5)
    @NotNull
    // @Type(type = "org.hibernate.type.IntegerType")
    var severity: Int = -1,

    @Column(nullable = false, length = 255)
    @NotEmpty
    @Size(max = 255)
    var title: String = "",

    @NotNull
    @Column(nullable = false)
    @Min(0)
    var position: Int = -1,

    @NotBlank
    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @Column(nullable = false)
    @NotNull
    @CreationTimestamp
    var createdAt: Date? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @Column(nullable = false)
    @NotNull
    @UpdateTimestamp
    var updatedAt: Date? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tableId", insertable = false, updatable = false)
    var table: ProjectTable? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(
        name = "parentIssueId",
        updatable = false,
        insertable = false,
    )
    @MappedField(DontMapCondition::class)
    var parentIssue: TableIssue? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "parentIssue",
        fetch = FetchType.LAZY
    )
    @MappedField(condition = LazyInitializedCondition::class, transformer = ImplicitCollectionMappingTransformer::class)
    var childIssues: MutableList<TableIssue> = mutableListOf(),

    @OneToMany(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "issueId")
    @MappedField(condition = LazyInitializedCondition::class, transformer = ImplicitCollectionMappingTransformer::class)
    var assigned: MutableList<IssueAssignee> = mutableListOf(),

    /*
    @OneToMany(
        cascade = [CascadeType.REMOVE],
        mappedBy = "issue",
        fetch = FetchType.LAZY,
    )
    var comments: MutableList<ProjectTableIssueComment> = mutableListOf(),

    @ManyToMany(
        // cascade = [CascadeType.REMOVE], labels should not be removed when the issue is removed
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "project_table_issue_label",
        joinColumns = [JoinColumn(name = "item_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "label_id", referencedColumnName = "id")]
    )
    var labels: MutableList<ProjectLabel> = mutableListOf(),
     */
)

class TableIssueList(val data: List<TableIssue>)
