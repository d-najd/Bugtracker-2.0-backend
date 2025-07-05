package io.dnajd.mainservice.domain.table_issue

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import io.dnajd.mainservice.domain.issue_assignee.IssueAssigneeDto
import io.dnajd.mainservice.domain.issue_comment.IssueCommentDto
import io.dnajd.mainservice.domain.issue_label.IssueLabelDto
import org.hibernate.collection.spi.PersistentSet
import java.util.*
import kotlin.collections.HashSet

@DefaultMappingTarget(TableIssue::class)
data class TableIssueDto(
    val id: Long? = null,
    val tableId: Long? = null,
    val reporter: String? = null,
    @JsonIgnore
    val parentIssueId: Long? = null,
    @MappedField
    val severity: Int? = null,
    @MappedField
    val title: String? = null,
    val position: Int? = null,
    @MappedField
    val description: String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val childIssues: Set<TableIssueDto>? = null,
    val assigned: Set<IssueAssigneeDto>? = null,
    val comments: Set<IssueCommentDto>? = null,
    val labels: Set<IssueLabelDto>? = null,
)

class TableIssueDtoList(val data: List<TableIssueDto>)
