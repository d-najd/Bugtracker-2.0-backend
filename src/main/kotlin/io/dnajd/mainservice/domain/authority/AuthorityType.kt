package io.dnajd.mainservice.domain.authority

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class AuthorityType(val value: String){
    PROJECT_VIEW("project_view"),
    PROJECT_CREATE("project_create"),
    PROJECT_EDIT("project_edit"),
    PROJECT_DELETE("project_delete"),
    PROJECT_MANAGE("project_manage"),
}

@Converter(autoApply = true)
class AuthorityTypeConverter: AttributeConverter<AuthorityType, String> {
    override fun convertToDatabaseColumn(p0: AuthorityType): String {
        return p0.value
    }

    override fun convertToEntityAttribute(p0: String): AuthorityType {
        return AuthorityType.entries.first { it.value == p0 }
    }
}