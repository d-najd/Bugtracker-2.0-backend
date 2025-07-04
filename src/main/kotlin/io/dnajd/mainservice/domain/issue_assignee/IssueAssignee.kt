package io.dnajd.mainservice.domain.issue_assignee

import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import jakarta.persistence.*

@Entity
@Table(
    name = "project_table_issue_assignee",
    uniqueConstraints = [UniqueConstraint(columnNames = ["issue_id", "assigned_username"])]
)
@IdClass(IssueAssigneeIdentity::class)
@AutoMapping(IssueAssigneeDto::class, AutoMappingStrategy.BY_NAME)
@DefaultMappingTarget(IssueAssigneeDto::class)
data class IssueAssignee(
    @Id
    @Column(updatable = false)
    val issueId: Long = -1L,

    @Id
    @Column(updatable = false)
    val assignerUsername: String = "",

    @Id
    @Column(updatable = false)
    val assignedUsername: String = "",
)

data class IssueAssigneeIdentity(
    val issueId: Long = -1L,
    val assignerUsername: String = "",
    val assignedUsername: String = "",
)