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
    var issueId: Long = -1L,

    @Id
    @Column(updatable = false)
    var assignerUsername: String = "",

    @Id
    @Column(updatable = false)
    var assignedUsername: String = "",
)

data class IssueAssigneeIdentity(
    var issueId: Long = -1L,
    var assignerUsername: String = "",
    var assignedUsername: String = "",
)