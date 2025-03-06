package io.dnajd.mainservice.domain.project

import java.util.*

data class ProjectResponse(
    var id: Long = 0L,
    var title: String = "",
    var description: String? = null,
    var createdAt: Date? = Date(),
)

class ProjectListResponse(val data: List<ProjectResponse>)
