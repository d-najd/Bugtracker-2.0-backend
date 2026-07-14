package io.dnajd.mainservice.domain.issueassignee

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(IssueAssignee::class)
data class IssueAssigneeDto(
	val issueId: Long? = null,
	val assignerUsername: String? = null,
	val assignedUsername: String? = null,
)
