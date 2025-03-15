package io.dnajd.mainservice.domain.issue_label

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(IssueLabel::class)
data class IssueLabelDto(
    var issueId: Long? = null,
    var label: String? = null,
)
