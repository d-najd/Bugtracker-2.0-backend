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
    var id: Long? = null,
    var tableId: Long? = null,
    var reporter: String? = null,
    @JsonIgnore
    var parentIssueId: Long? = null,
    @MappedField
    var severity: Int? = null,
    @MappedField
    var title: String? = null,
    var position: Int? = null,
    @MappedField
    var description: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var childIssues: MutableSet<TableIssueDto>? = null,
    var assigned: MutableSet<IssueAssigneeDto>? = null,
    var comments: MutableSet<IssueCommentDto>? = null,
    var labels: MutableSet<IssueLabelDto>? = null,
)

class TableIssueDtoList(val data: List<TableIssueDto>)
