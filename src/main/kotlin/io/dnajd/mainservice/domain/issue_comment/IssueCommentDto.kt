package io.dnajd.mainservice.domain.issue_comment

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import java.util.*

@DefaultMappingTarget(IssueCommentDto::class)
data class IssueCommentDto(
    var id: Long? = null,
    var user: String? = null,
    @JsonIgnore
    var issueId: Long? = null,
    @MappedField
    var message: String? = null,
    var createdAt: Date? = null,
    var editedAt: Date? = null,
)
