package io.dnajd.mainservice.domain.table_issue

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
@Table(
    name = "project_table_issue",
    uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("table_id", "position"))]
)
@AutoMapping(TableIssueDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(TableIssueDto::class)
data class TableIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @Column
    var tableId: Long = -1L,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tableId", insertable = false, updatable = false)
    var table: ProjectTable? = null,

    @Column
    var reporter: String = "",

    @Nullable
    @Column(nullable = true)
    var parentIssueId: Long? = null,

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
    var childIssues: MutableList<TableIssue> = mutableListOf(),

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
)

class TableIssueList(val data: List<TableIssue>)
