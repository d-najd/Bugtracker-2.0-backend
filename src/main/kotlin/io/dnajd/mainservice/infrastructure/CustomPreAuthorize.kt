package io.dnajd.mainservice.infrastructure

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.StandardReflectionParameterNameDiscoverer
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.io.Serializable
import java.lang.reflect.Method

enum class PreAuthorizeType(val value: String) {
    Project("Project"),
    Table("ProjectTable"),
    Issue("TableIssue"),
}

enum class PreAuthorizePermission(val value: String) {
    View("project_view"),
    Create("project_create"),
    Delete("project_delete"),
    Edit("project_edit"),
    Manage("project_manage"),
    Owner("project_owner"),
}


/**
 * Modified type safe variant of [PreAuthorize] hasAuthority
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomPreAuthorize(
    val targetId: String,
    val targetType: PreAuthorizeType,
    val permission: PreAuthorizePermission,
)

@Aspect
@Component
class CustomPreAuthorizeAspect(
    val permissionEvaluator: PermissionEvaluator
) {
    @Before("@annotation(customPreAuthorize)")
    fun checkPermission(joinPoint: JoinPoint, customPreAuthorize: CustomPreAuthorize) {
        val args = joinPoint.args
        val method = (joinPoint.signature as MethodSignature).method

        val targetIdValue = resolveSpEL(customPreAuthorize.targetId, method, args)
        val targetType = customPreAuthorize.targetType.value
        val permission = customPreAuthorize.permission.value

        val auth = SecurityContextHolder.getContext().authentication
        val hasPermission = permissionEvaluator.hasPermission(auth, targetIdValue, targetType, permission)

        if (!hasPermission) {
            throw AccessDeniedException("Access denied")
        }
    }

    private fun resolveSpEL(expression: String, method: Method, args: Array<Any>): Serializable {
        val context = StandardEvaluationContext()
        val paramNames = StandardReflectionParameterNameDiscoverer().getParameterNames(method)
        paramNames?.forEachIndexed { index, name -> context.setVariable(name, args[index]) }

        val parser = SpelExpressionParser()
        return parser.parseExpression(expression).getValue(context, Serializable::class.java)
            ?: throw IllegalArgumentException("Unable to resolve expression: $expression")
    }
}