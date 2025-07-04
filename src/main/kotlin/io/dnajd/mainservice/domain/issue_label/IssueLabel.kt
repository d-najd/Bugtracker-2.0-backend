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
    val issueId: Long = -1L,

    @Id
    @Column(length = 255)
    @Size(max = 255)
    val label: String = "",
)

data class IssueLabelIdentity(
    val issueId: Long = -1L,
    val label: String = "",
)