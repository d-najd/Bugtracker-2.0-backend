package io.dnajd.mainservice.controller

import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.project_icon.ProjectIconService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(Endpoints.PROJECT_ICON)
class ProjectIconController(
    private val service: ProjectIconService,
) {
    @PutMapping("/projectId/{projectId}")
    fun update(
        @RequestParam("file") file: MultipartFile,
        @PathVariable projectId: Long,
    ) {
        service.update(projectId, file)
    }
}
