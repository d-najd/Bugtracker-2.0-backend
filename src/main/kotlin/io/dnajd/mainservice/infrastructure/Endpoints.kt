package io.dnajd.mainservice.infrastructure

object Endpoints {
    const val BASE: String = "/api/"
    const val PROJECT: String = BASE + "project/"
    const val PROJECT_TABLE: String = BASE + "project-table/"
    const val PROJECT_TABLE_ISSUE: String = BASE + "project-table-issue/"
    const val USER: String = BASE + "user/"
    const val GOOGLE_AUTH: String = BASE + "google_auth/"
    const val JWT_REFRESH_AUTH: String = BASE + "jwt_refresh_auth/"
    const val PROJECT_AUTHORITY: String = BASE + "project-authority/"
}