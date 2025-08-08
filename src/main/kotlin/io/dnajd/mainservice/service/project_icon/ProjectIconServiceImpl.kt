package io.dnajd.mainservice.service.project_icon

import io.dnajd.mainservice.repository.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ProjectIconServiceImpl(
    private val projectRepository: ProjectRepository,
) : ProjectIconService {
    companion object {
        const val PROJECT_ICON_DIRECTORY: String = "user-content/project-icon"
    }

    override fun get(projectId: Long): MultipartFile {
        val project = projectRepository.getReferenceById(projectId)

        val filePath = "example.jpg"
        val fileNameAndPath: Path = Paths.get(PROJECT_ICON_DIRECTORY, filePath)
        /*
        val fileNames = StringBuilder()
        val fileNameAndPath: Path = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename())
        fileNames.append(file.getOriginalFilename())
        Files.write(fileNameAndPath, file.getBytes())
        model.addAttribute("msg", "Uploaded images: $fileNames")
        return "imageupload/index"
        Paths.get("")
        TODO()
         */
        TODO()
    }

    override fun update(projectId: Long, icon: MultipartFile) {
        val uploadDir = "$PROJECT_ICON_DIRECTORY/"

        // this is less expandable, I want to not have to upload the default project icons over and over
        // val uploadDir = "$PROJECT_ICON_DIRECTORY/$projectId/"
        val directory = File(uploadDir)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val filePath = uploadDir + icon.originalFilename
        File(filePath).writeBytes(icon.bytes)
    }
}
