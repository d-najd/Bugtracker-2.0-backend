package io.dnajd.mainservice.domain.table_issue

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import jakarta.persistence.CascadeType
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import java.util.*

@DefaultMappingTarget(TableIssue::class)
data class TableIssueDto(
    var id: Long = -1L,
    var tableId: Long = -1L,
    var reporter: String = "",
    @JsonIgnore
    var parentIssueId: Long? = null,
    @MappedField
    var severity: Int = -1,
    var childIssues: MutableList<TableIssueDto>? = null,
    @MappedField
    var title: String = "",
    var position: Int = -1,
    @MappedField
    var description: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
)

class TableIssueDtoList(val data: List<TableIssueDto>)
