package io.dnajd.projectservice.web

import io.dnajd.projectservice.model.Project

data class ProjectHolder(
    val projects: List<Project> = emptyList()
)