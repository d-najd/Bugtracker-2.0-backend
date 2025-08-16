package io.dnajd.mainservice.service.project_icon

import io.dnajd.mainservice.infrastructure.user_content.UserContentDirs
import io.dnajd.mainservice.infrastructure.user_content.UserContentPathMapper
import io.dnajd.mainservice.repository.ProjectRepository
import io.dnajd.mainservice.service.project.ProjectServiceImpl
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path


@Service
@Transactional
class ProjectIconServiceImpl(
    private val projectRepository: ProjectRepository,
) : ProjectIconService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    init {
        val absolutePath = "${UserContentDirs.ABSOLUTE_PATH}${UserContentDirs.BASE}${UserContentDirs.PROJECT_ICON}"
        Files.createDirectories(Path(absolutePath))
    }

    override fun getByProjectId(projectId: Long): FileSystemResource {
        val project = projectRepository.getReferenceById(projectId)
        val iconUri = project.iconUri

        if (!UserContentPathMapper.isUserContentUri(iconUri)) {
            throw IllegalArgumentException("For files not stored in the project don't use the backend")
        }

        val iconPath = UserContentPathMapper.resolveAbsolutePathFromUri(iconUri)
        return FileSystemResource(iconPath)
    }

    override fun updateByProjectId(projectId: Long, icon: MultipartFile) {
        val project = projectRepository.getReferenceById(projectId)

        val newIconUri = UserContentPathMapper.toUserContentUri(UserContentDirs.PROJECT_ICON, icon, projectId.toString())
        val newIconAbsolutePath = Path(UserContentPathMapper.resolveAbsolutePathFromUri(newIconUri))

        Files.write(newIconAbsolutePath, icon.bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE)

        val oldIconUri = project.iconUri
        if (oldIconUri == newIconUri) {
            return
        }

        val projectWithNewIcon = project.copy(
            iconUri = newIconUri
        )

        try {
            projectRepository.saveAndFlush(projectWithNewIcon)
        } catch (e: Exception) {
            deleteFileByUriWithoutThrowing(newIconUri)
            throw e
        }

        if (isDefaultProjectIcon(oldIconUri)) {
            return
        }

        deleteFileByUriWithoutThrowing(oldIconUri)
    }

    private fun deleteFileByUriWithoutThrowing(uri: String) {
        try {
            val absolutePath = Path("${UserContentDirs.ABSOLUTE_PATH}${uri}")
            Files.delete(absolutePath)
        } catch (e: Exception) {
            log.error("Unable to delete file, must be removed manually by hand: $uri")
            e.printStackTrace()
        }
    }

    private fun isDefaultProjectIcon(uri: String): Boolean {
        if (!UserContentPathMapper.isUserContentUri(uri)) return false
        return uri.startsWith(UserContentDirs.DEFAULT_PROJECT_ICON)
    }
}
