package io.dnajd.mainservice.domain.table_issue

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.issue_assignee.IssueAssigneeDto
import io.dnajd.mainservice.domain.issue_comment.IssueCommentDto
import io.dnajd.mainservice.domain.issue_label.IssueLabelDto
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
    @MappedField
    var title: String = "",
    var position: Int = -1,
    @MappedField
    var description: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var childIssues: MutableList<TableIssueDto>? = null,
    var assigned: MutableList<IssueAssigneeDto>? = null,
    var comments: MutableList<IssueCommentDto>? = null,
    var labels: MutableList<IssueLabelDto>? = null,
)

class TableIssueDtoList(val data: List<TableIssueDto>)
