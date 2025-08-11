package io.dnajd.mainservice.infrastructure.user_content

import org.springframework.web.multipart.MultipartFile

/**
 * Used for resolving and converting url's
 *
 * This is used to prevent urls being strongly dependent on the specific backend for example
 * user-content://project-icon/icon.jpg -> project/path/user-content/path/to/file.jpg this way the exact backend url
 * won't have to be stored and if it changes it won't break all user content, other url's which don't use this
 * schema should instead just get ignored
 *
 * @see UserContentDirs
 */
object UserContentPathMapper {
    const val USER_CONTENT_URI = "user-content://"

    /**
     * user-content://project-icon/icon.jpg -> project/path/user-content/project-icon/icon.jpg
     * default - www.image-hoster/image.jpg -> www.image-hoster/image.jpg
     */
    fun resolveAbsolutePathFromUri(uri: String): String {
        if (isUserContentUri(uri)) {
            val uriPath = uri.drop(USER_CONTENT_URI.length)
            return "${UserContentDirs.ABSOLUTE_PATH}$uriPath"
        }

        return uri
    }

    /**
     *
     */
    fun toUserContentUri(path: String, file: MultipartFile, fileName: String = file.name): String {
        return "${USER_CONTENT_URI}${toUserContentPath(path, file, fileName)}"
    }

    fun toUserContentPath(path: String, file: MultipartFile, fileName: String = file.name): String {
        return "$path$fileName.${file.contentType!!.substringAfter("/")}"
    }

    fun isUserContentUri(uri: String): Boolean {
        return uri.startsWith(USER_CONTENT_URI)
    }

    fun absolutePathToUserContentUri(absolutePath: String): String {
        val index = absolutePath.lastIndexOf(UserContentDirs.BASE)
        if (index != -1) {
            val userContentDir = absolutePath.substring(index + UserContentDirs.BASE.length)
            return "$USER_CONTENT_URI$userContentDir"
        } else {
            throw IllegalArgumentException("given path doesn't contain user-content")
        }
    }
}