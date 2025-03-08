package io.dnajd.mainservice.domain.project_table

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField

@DefaultMappingTarget(ProjectTable::class)
data class ProjectTableDto(
    var id: Long? = null,
    var projectId: Long? = null,
    @MappedField
    var title: String? = null,
    var position: Int? = null,
)

class ProjectTableDtoList(val data: List<ProjectTableDto>)
