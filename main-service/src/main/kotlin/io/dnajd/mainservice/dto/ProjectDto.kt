package io.dnajd.mainservice.dto

import io.dnajd.mainservice.infrastructure.pojo.BaseDto
import io.dnajd.mainservice.infrastructure.pojo.BaseListDto
import java.util.*

class ProjectDto(
    var title: String,
    var description: String?,
    var createdAt: Date,
) : BaseDto()

class ProjectListDto(data: List<ProjectDto>) : BaseListDto<ProjectDto>(data)
