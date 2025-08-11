package io.dnajd.mainservice.service.project_icon

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.UrlResource
import org.springframework.web.multipart.MultipartFile

interface ProjectIconService {
    fun getByProjectId(projectId: Long): FileSystemResource

    fun updateByProjectId(projectId: Long, icon: MultipartFile)
}
