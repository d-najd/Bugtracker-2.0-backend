package io.dnajd.mainservice.domain.issue_comment

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import java.util.*

@DefaultMappingTarget(IssueComment::class)
data class IssueCommentDto(
    val id: Long? = null,
    val user: String? = null,
    @JsonIgnore
    val issueId: Long? = null,
    @MappedField
    val message: String? = null,
    val createdAt: Date? = null,
    val editedAt: Date? = null,
)
