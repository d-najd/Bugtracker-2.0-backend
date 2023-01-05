package io.dnajd.projectservice.web

import io.dnajd.projectservice.model.Project

data class ProjectHolder(
    val data: List<Project> = emptyList()
)