package io.dnajd.mainservice.web

import io.dnajd.mainservice.model.Project

data class ProjectHolder(
    val data: List<Project> = emptyList()
)