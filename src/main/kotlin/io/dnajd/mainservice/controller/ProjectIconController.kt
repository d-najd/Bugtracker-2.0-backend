package io.dnajd.mainservice.controller

import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.ScopedAuthorize
import io.dnajd.mainservice.infrastructure.ScopedEvaluatorType
import io.dnajd.mainservice.infrastructure.ScopedPermission
import io.dnajd.mainservice.service.project_icon.ProjectIconService
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.UnsupportedMediaTypeException
import java.nio.file.Files
import kotlin.io.path.Path

@RestController
@RequestMapping(Endpoints.PROJECT_ICON)
class ProjectIconController(
    private val service: ProjectIconService,
) {
    @GetMapping("/projectId/{projectId}")
    @ScopedAuthorize("#projectId", ScopedEvaluatorType.Project, ScopedPermission.View)
    fun getByProjectId(
        @PathVariable projectId: Long,
    ): ResponseEntity<FileSystemResource> {
        val file = service.getByProjectId(projectId)
        val contentType = Files.probeContentType(Path(file.path))

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(file)
    }

    @ScopedAuthorize("#projectId", ScopedEvaluatorType.Project, ScopedPermission.Owner)
    @PutMapping("/projectId/{projectId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateByProjectId(
        @RequestParam("file") file: MultipartFile,
        @PathVariable projectId: Long,
    ) {
        val contentType = file.contentType!!
        if (MediaType.IMAGE_PNG_VALUE != contentType && MediaType.IMAGE_JPEG_VALUE != contentType) {
            throw UnsupportedMediaTypeException("File must be of type png or jpeg")
        }

        service.updateByProjectId(projectId, file)
    }
}
