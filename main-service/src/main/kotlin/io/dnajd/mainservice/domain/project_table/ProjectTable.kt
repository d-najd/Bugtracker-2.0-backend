package io.dnajd.mainservice.domain.project_table

import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import io.dnajd.mainservice.domain.project.ProjectDto
import jakarta.persistence.*

@Entity
@Table(name = "project_table")
@AutoMapping(ProjectDto::class, AutoMappingStrategy.BY_NAME)
data class ProjectTable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    var projectId: Long = -1L,


)
