package io.dnajd.mainservice.domain.project_table

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.table_issue.TableIssueDto

@DefaultMappingTarget(ProjectTable::class)
data class ProjectTableDto(
    val id: Long? = null,
    val projectId: Long? = null,
    @MappedField
    val title: String? = null,
    val position: Int? = null,
    val issues: List<TableIssueDto>? = null
)

class ProjectTableDtoList(val data: List<ProjectTableDto>)
