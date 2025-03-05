package io.dnajd.mainservice.web

import io.dnajd.mainservice.domain.Project

data class ProjectHolder(
    val data: List<Project> = emptyList()
)