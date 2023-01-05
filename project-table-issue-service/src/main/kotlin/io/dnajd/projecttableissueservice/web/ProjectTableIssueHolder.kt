package io.dnajd.projecttableissueservice.web

import io.dnajd.projecttableissueservice.model.ProjectTableIssue

data class ProjectTableIssueHolder(
    val projectTableIssues: List<ProjectTableIssue> = emptyList()
)