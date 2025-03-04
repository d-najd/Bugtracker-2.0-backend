package io.dnajd.mainservice.model

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class UserAuthorityIdentity(
    @Column(name = "user", nullable = false)
    var username: String = "",

    @Column(name = "project_id", nullable = false)
    var projectId: Long = -1L,

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    var authority: UserAuthorityType = UserAuthorityType.project_view,
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserAuthorityIdentity

        if (username != other.username) return false
        if (projectId != other.projectId) return false
        if (authority != other.authority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + projectId.hashCode()
        result = 31 * result + authority.hashCode()
        return result
    }

}