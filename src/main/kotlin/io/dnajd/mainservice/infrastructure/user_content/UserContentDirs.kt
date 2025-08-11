package io.dnajd.mainservice.infrastructure.user_content

import java.nio.file.Paths

/**
 * @see UserContentPathMapper
 */
object UserContentDirs {
    val ABSOLUTE_PATH = Paths.get("").toAbsolutePath().toString() + "/"

    const val BASE: String = "user-content/"
    const val PROJECT_ICON: String = "project-icon/"
    const val DEFAULT_PROJECT_ICON: String = "default-project-icon/"
}