package io.dnajd.mainservice.service.projecticon

import org.springframework.core.io.FileSystemResource
import org.springframework.web.multipart.MultipartFile

interface ProjectIconService {
	fun getByProjectId(projectId: Long): FileSystemResource

	fun updateByProjectId(
		projectId: Long,
		icon: MultipartFile,
	)
}
