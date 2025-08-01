package io.dnajd.mainservice.domain.issue_label

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget

@DefaultMappingTarget(IssueLabel::class)
data class IssueLabelDto(
    @JsonIgnore
    val issueId: Long? = null,
    val label: String? = null,
)
