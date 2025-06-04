package io.dnajd.mainservice.config

import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class CustomPermissionEvaluator : PermissionEvaluator {
    override fun hasPermission(
        authentication: Authentication,
        targetDomainObject: Any,
        permission: Any
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(
        authentication: Authentication,
        targetId: Serializable,
        targetType: String,
        permission: Any
    ): Boolean {
        TODO("Not yet implemented")
    }

    fun hasProjectPermission() {

    }
}