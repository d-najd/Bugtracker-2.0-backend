package io.dnajd.mainservice.infrastructure

import io.dnajd.mainservice.config.CustomPermissionEvaluator
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.table_issue.TableIssue
import jakarta.el.TypeConverter
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.StandardReflectionParameterNameDiscoverer
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.core.convert.support.GenericConversionService
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.expression.spel.support.StandardTypeConverter
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.io.Serializable
import java.lang.reflect.Method


/**
 * Evaluators determine how the permission check will be done and how [PreAuthorizePermission] will be used if at all.
 * Each evaluator must accept only one type of input
 */
enum class PreAuthorizeEvaluator(val value: String) {
    /**
     * input type [io.dnajd.mainservice.domain.project.Project.id]
     */
    Project("Project"),

    /**
     * input type [ProjectTable.id]
     */
    Table("ProjectTable"),
    /**
     * input type [TableIssue.id]
     */
    Issue("TableIssue"),
    /**
     * input type [ProjectAuthorityIdentity]
     * the user must have the authorities that are inside [ProjectAuthorityIdentity] as well as other authorities passed
     */
    HasGrantingAuthority("HasGrantingAuthorityAndManage")
}

/**
 * The type of permission that will be checked by [PreAuthorizeEvaluator], how this check is done and whether the
 * permission is used is dependent on [PreAuthorizeEvaluator]
 */
enum class PreAuthorizePermission(val value: String) {
    View("project_view"),
    Create("project_create"),
    Delete("project_delete"),
    Edit("project_edit"),
    Manage("project_manage"),
    Owner("project_owner"),
    /**
     * This authority will be ignored, if its the only one empty list will be sent
     */
    None("NoneIgnore")
}


/**
 * Modified type safe variant of [PreAuthorize] hasAuthority
 *
 * @param targetId should pass the target identity for the data class, ex #userId and should match parameter in the
 * function that it annotates, this id can be of any type but is usually [Long] the type depends on the [evaluatorType]
 * @param evaluatorType The type of permission evaluator that will be used. each evaluator must only accept one type of
 * [targetId]
 * @param permissions the permission
 * @see PreAuthorizeEvaluator
 * @see PreAuthorizePermission
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomPreAuthorize(
    val targetId: String,
    val evaluatorType: PreAuthorizeEvaluator,
    vararg val permissions: PreAuthorizePermission,
)

/**
 * Modified type safe variant of [PreAuthorize] hasAuthority
 */
/* This one seems less powerful
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomPreAuthorizeClass(
    val targetObject: String,
    val permission: PreAuthorizePermission,
)

@Aspect
@Component
class CustomPreAuthorizeClassAspect(
    val permissionEvaluator: PermissionEvaluator
) {
    @Before("@annotation(customPreAuthorizeClass)")
    fun checkPermission(joinPoint: JoinPoint, customPreAuthorizeClass: CustomPreAuthorizeClass) {
        val args = joinPoint.args
        val method = (joinPoint.signature as MethodSignature).method

        val targetValue = CustomPreAuthorizeShared.resolveSpEL(customPreAuthorizeClass.targetObject, method, args)
        val permission = customPreAuthorizeClass.permission.value

        val auth = SecurityContextHolder.getContext().authentication
        val hasPermission = permissionEvaluator.hasPermission(auth, targetValue, permission)

        if (!hasPermission) {
            throw AccessDeniedException("Access denied")
        }
    }
}
 */

@Aspect
@Component
class CustomPreAuthorizeAspect(
    val permissionEvaluator: CustomPermissionEvaluator
) {
    @Before("@annotation(customPreAuthorize)")
    fun checkPermission(joinPoint: JoinPoint, customPreAuthorize: CustomPreAuthorize) {
        val args = joinPoint.args
        val method = (joinPoint.signature as MethodSignature).method

        val targetIdValue = CustomPreAuthorizeShared.resolveSpEL(customPreAuthorize.targetId, method, args)
        val targetType = customPreAuthorize.evaluatorType
        val permissions = customPreAuthorize.permissions.toMutableList().filter { o -> o != PreAuthorizePermission.None }

        val auth = SecurityContextHolder.getContext().authentication
        val hasPermission = permissionEvaluator.hasPermission(auth, targetIdValue, targetType, permissions)

        if (!hasPermission) {
            throw AccessDeniedException("Access denied")
        }
    }
}

internal object CustomPreAuthorizeShared {
    fun resolveSpEL(expression: String, method: Method, args: Array<Any>): Serializable {
        val context = StandardEvaluationContext()
        val paramNames = StandardReflectionParameterNameDiscoverer().getParameterNames(method)
        paramNames?.forEachIndexed { index, name -> context.setVariable(name, args[index]) }

        val parser = SpelExpressionParser()
        return parser.parseExpression(expression).getValue(context, Serializable::class.java)
            ?: throw IllegalArgumentException("Unable to resolve expression: $expression")
    }
}