package io.dnajd.mainservice.domain.project_table

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.infrastructure.mapper.DontMapCondition

@DefaultMappingTarget(ProjectTable::class)
data class ProjectTableDto(
    var id: Long? = null,
    var projectId: Long? = null,
    @MappedField
    var title: String? = null,
    var position: Int? = null,
    var issues: MutableList<TableIssueDto>? = null
)

class ProjectTableDtoList(val data: List<ProjectTableDto>)
