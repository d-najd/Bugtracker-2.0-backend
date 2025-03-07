package io.dnajd.mainservice.infrastructure

import dev.krud.shapeshift.condition.MappingCondition
import dev.krud.shapeshift.condition.MappingConditionContext

class DontMapCondition : MappingCondition<Any> {
    override fun isValid(context: MappingConditionContext<Any>): Boolean {
        return false
    }
}