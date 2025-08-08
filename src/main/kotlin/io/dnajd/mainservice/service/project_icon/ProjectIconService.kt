package io.dnajd.mainservice.service.project_icon

import org.springframework.web.multipart.MultipartFile

interface ProjectIconService {
    fun get(projectId: Long): MultipartFile

    fun update(projectId: Long, icon: MultipartFile)
}
