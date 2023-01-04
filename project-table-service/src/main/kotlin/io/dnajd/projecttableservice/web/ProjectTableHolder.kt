package io.dnajd.projecttableservice.web

import io.dnajd.projecttableservice.model.ProjectTable

data class ProjectTableHolder(
    val projects: List<ProjectTable> = emptyList()
)