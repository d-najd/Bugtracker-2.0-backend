package io.dnajd.mainservice.domain.project

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import java.util.*

@DefaultMappingTarget(Project::class)
data class ProjectDto(
    val id: Long? = null,
    @MappedField
    val title: String? = null,
    val owner: String? = null,
    @MappedField
    val description: String? = null,
    val createdAt: Date? = null,
)

class ProjectDtoList(val data: List<ProjectDto>)