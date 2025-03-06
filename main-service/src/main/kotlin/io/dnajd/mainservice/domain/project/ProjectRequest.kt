package io.dnajd.mainservice.domain.project

import dev.krud.shapeshift.enums.AutoMappingStrategy
import dev.krud.shapeshift.resolver.annotation.AutoMapping

@AutoMapping(Project::class, AutoMappingStrategy.BY_NAME)
class ProjectRequest(
    var title: String = "",
    var description: String? = null,
)