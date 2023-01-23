package io.dnajd.userauthorityservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table(name = "user_authority")
data class UserAuthority(
    @JsonIgnore
    @EmbeddedId
    var identity: UserAuthorityIdentity = UserAuthorityIdentity(),

    @Column(name = "user", nullable = false, insertable = false, updatable = false)
    var username: String = "",

    @Column(name = "project_id", nullable = false, insertable = false, updatable = false)
    var projectId: Long = -1L,

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false, insertable = false, updatable = false)
    var authority: UserAuthorityType = UserAuthorityType.ProjectView,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserAuthority

        return identity == other.identity
    }

    override fun hashCode(): Int = Objects.hash(identity);

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(EmbeddedId = $identity )"
    }
}