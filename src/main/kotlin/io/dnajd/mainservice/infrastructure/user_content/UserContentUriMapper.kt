package io.dnajd.mainservice.infrastructure.user_content

/**
 * Used for resolving and converting url's
 *
 * This is used to prevent urls being strongly dependent on the specific backend for example
 * user-content://project-icon/icon.jpg -> www.backend-url.com/icon.jpg this way the exact backend url
 * won't have to be stored and if it changes it won't break all user content, other url's which don't use this
 * schema should instead just get ignored
 */
object UserContentUriMapper {
    const val BASE_USER_CONTENT_URI = "user-content://"

    /**
     * user-content://project-icon/icon.jpg -> www.backend-url.com/icon.jpg
     * default - www.image-hoster/image.jpg -> www.image-hoster/image.jpg
     */
    fun resolveUri(uri: String): String {
        if (isUserContentUri(uri)) {
            return uri.drop(BASE_USER_CONTENT_URI.length)
        }

        return uri
    }

    fun isUserContentUri(uri: String): Boolean {
        return uri.startsWith(BASE_USER_CONTENT_URI)
    }

    /*
    fun mapUrl(url: String): String {
        return ""
    }
     */
}