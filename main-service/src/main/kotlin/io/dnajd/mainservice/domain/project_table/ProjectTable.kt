package io.dnajd.mainservice.domain.project_table

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.domain.project.ProjectDto
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.Cascade

@Entity
@Table(name = "project_table")
@AutoMapping(ProjectTableDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(ProjectTableDto::class)
data class ProjectTable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @NotNull
    @Column(nullable = false)
    var projectId: Long = -1L,

    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    var title: String = "",

    @NotNull
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    var position: Int = -1,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="projectId", insertable = false, updatable = false)
    var project: Project? = null
)

class ProjectTableList(val data: List<ProjectTable>)
