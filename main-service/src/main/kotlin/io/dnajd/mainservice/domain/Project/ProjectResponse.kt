package io.dnajd.mainservice.domain.Project

import io.dnajd.mainservice.infrastructure.pojo.BaseListResponse
import io.dnajd.mainservice.infrastructure.pojo.BaseResponse
import java.util.*

class ProjectResponse(
    id: Long,
    var title: String,
    var description: String?,
    var createdAt: Date,
) : BaseResponse(id)

class ProjectListResponse(data: List<ProjectDto>) : BaseListResponse<ProjectDto>(data)
