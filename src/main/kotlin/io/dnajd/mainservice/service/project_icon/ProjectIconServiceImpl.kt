package io.dnajd.mainservice.service.project_icon

import io.dnajd.mainservice.infrastructure.user_content.UserContentDirs
import io.dnajd.mainservice.infrastructure.user_content.UserContentUriMapper
import io.dnajd.mainservice.repository.ProjectRepository
import io.dnajd.mainservice.service.project.ProjectServiceImpl
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files

@Service
@Transactional
class ProjectIconServiceImpl(
    private val projectRepository: ProjectRepository,
) : ProjectIconService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    init {
        val projectIconsDirectory = File(UserContentDirs.PROJECT_ICON)
        if (!projectIconsDirectory.exists()) {
            projectIconsDirectory.mkdirs()
        }
    }

    override fun get(projectId: Long): MultipartFile {
        val project = projectRepository.getReferenceById(projectId)
        val iconUri = project.iconUri

        if (!UserContentUriMapper.isUserContentUri(iconUri)) {
            throw IllegalArgumentException("For files not stored in the project don't use the backend")
        }

        val path = 
        Files.readAllBytes()

        // File(iconUri)

    }

    override fun update(projectId: Long, icon: MultipartFile) {
        val project = projectRepository.getReferenceById(projectId)

        val iconDirectory = UserContentDirs.PROJECT_ICON
        val newIconUri = "$iconDirectory$projectId.${icon.contentType!!.substringAfter("/")}"

        File(newIconUri).writeBytes(icon.bytes)

        val projectWithNewIcon = project.copy(
            iconUri = newIconUri
        )

        try {
            projectRepository.saveAndFlush(projectWithNewIcon)
        } catch(e: IOException) {
            deleteFileWithoutThrowing(newIconUri)
            throw e
        }

        val oldIconUri = project.iconUri
        if (isDefaultProjectIcon(oldIconUri)) {
            return
        }

        deleteFileWithoutThrowing(oldIconUri)
    }

    private fun deleteFileWithoutThrowing(path: String) {
        try {
            File(path).delete()
        } catch (e: IOException) {
            log.error("Unable to delete file, must be removed manually by hand: $path")
            e.printStackTrace()
        }
    }

    private fun isDefaultProjectIcon(uri: String): Boolean {
        if (!UserContentUriMapper.isUserContentUri(uri)) return false
        return uri.contains(UserContentDirs.DEFAULT_PROJECT_ICON)
    }
}
