package io.dnajd.mainservice.domain.authority

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "user_authority_type")
data class Authority(
    @Id
    @Convert(converter = AuthorityTypeConverter::class)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    @NotNull
    var authority: AuthorityType = AuthorityType.PROJECT_VIEW
): GrantedAuthority {
    override fun getAuthority(): String {
        return authority.value
    }
}