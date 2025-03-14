package io.dnajd.mainservice.domain.project_table

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "project_table")
@AutoMapping(ProjectTableDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(ProjectTableDto::class)
data class ProjectTable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @Column
    var projectId: Long = -1L,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    var project: Project? = null,

    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    var title: String = "",

    @NotNull
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @Min(0)
    var position: Int = -1,

    @OneToMany(mappedBy = "tableId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var issues: List<TableIssue> = emptyList()
)

class ProjectTableList(val data: List<ProjectTable>)
