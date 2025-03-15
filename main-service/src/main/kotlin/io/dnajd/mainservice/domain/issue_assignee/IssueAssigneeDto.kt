package io.dnajd.mainservice.domain.issue_assignee

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(IssueAssignee::class)
data class IssueAssigneeDto (
    @JsonIgnore
    var issueId: Long? = null,
    var assignerUsername: String? = null,
    var assignedUsername: String? = null,
)