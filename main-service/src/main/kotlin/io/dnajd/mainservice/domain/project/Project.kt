package io.dnajd.mainservice.domain.project

import com.fasterxml.jackson.annotation.JsonFormat
import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Entity
@Table(name = "project")
@AutoMapping(ProjectDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(ProjectDto::class)
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    var title: String = "",

    @Column
    var owner: String = "",

    @NotBlank
    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotNull
    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: Date? = null,

    @OneToMany(mappedBy = "projectId", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @MappedField(DontMapCondition::class)
    var tables: List<ProjectTable> = emptyList()
)

class ProjectList(val data: List<Project>)