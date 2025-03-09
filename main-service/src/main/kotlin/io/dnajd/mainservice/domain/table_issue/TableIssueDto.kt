package io.dnajd.mainservice.domain.table_issue

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import java.util.*

@DefaultMappingTarget(TableIssue::class)
data class TableIssueDto(
    var id: Long = -1L,
    var tableId: Long = -1L,
    var reporter: String = "",
    var parentIssueId: Long? = null,
    @MappedField
    var severity: Int = -1,
    @MappedField
    var title: String = "",
    var position: Int = -1,
    @MappedField
    var description: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
)

class TableIssueDtoList(val data: List<TableIssueDto>)
