package io.dnajd.mainservice.domain.issue_assignee

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(IssueAssignee::class)
data class IssueAssigneeDto (
    @JsonIgnore
    val issueId: Long? = null,
    val assignerUsername: String? = null,
    val assignedUsername: String? = null,
)