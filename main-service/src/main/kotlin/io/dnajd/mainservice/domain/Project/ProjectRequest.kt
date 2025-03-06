package io.dnajd.mainservice.domain.Project

import io.dnajd.mainservice.infrastructure.pojo.BaseRequest
import java.util.*

class ProjectRequest(
    var title: String,
    var description: String?,
    var createdAt: Date,
) : BaseRequest()