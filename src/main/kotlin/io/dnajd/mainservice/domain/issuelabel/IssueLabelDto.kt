package io.dnajd.mainservice.domain.issuelabel

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(IssueLabel::class)
data class IssueLabelDto(
    val issueId: Long? = null,
    val label: String? = null,
)
