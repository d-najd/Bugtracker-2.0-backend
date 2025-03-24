package io.dnajd.mainservice.infrastructure.mapper

import dev.krud.shapeshift.condition.MappingCondition
import dev.krud.shapeshift.condition.MappingConditionContext
import jakarta.persistence.FetchType
import org.hibernate.Hibernate

/**
 * If a field with [FetchType.LAZY] is initialized it will be mapped otherwise it won't be
 */
class LazyInitializedCondition : MappingCondition<Any> {
    override fun isValid(context: MappingConditionContext<Any>): Boolean {
        return Hibernate.isInitialized(context.originalValue)
    }
}