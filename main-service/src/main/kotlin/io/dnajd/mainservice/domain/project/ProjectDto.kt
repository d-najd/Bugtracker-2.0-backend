package io.dnajd.mainservice.domain.project

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import java.util.*

@DefaultMappingTarget(Project::class)
data class ProjectDto(
    var id: Long? = null,
    @MappedField
    var title: String = "",
    @MappedField
    var owner: String = "",
    @MappedField
    var description: String? = null,
    var createdAt: Date? = null,
)

class ProjectDtoList(val data: List<ProjectDto>)