package io.dnajd.mainservice.domain.issue_label

import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "project_issue_label")
@IdClass(IssueLabelIdentity::class)
@AutoMapping(IssueLabelDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(IssueLabelDto::class)
data class IssueLabel(
    @Id
    @Column
    var issueId: Long = -1L,

    @Id
    @Column(length = 255)
    @Size(max = 255)
    var label: String = "",
)

data class IssueLabelIdentity(
    var issueId: Long = -1L,
    var label: String = "",
)