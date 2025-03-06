package io.dnajd.mainservice.infrastructure

import dev.krud.shapeshift.ShapeShift
import dev.krud.shapeshift.ShapeShiftBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.full.memberProperties

@Configuration
class Mapper {
    @Bean
    fun shapeShiftMapper() = ShapeShiftBuilder().build()
}

inline fun <reified Entity : Any, reified Request : Any> ShapeShift.mapForUpdate(entity: Entity, request: Request): Entity {

    //var te = this.mappingDefinitionResolvers
    //Entity::class.memberProperties.associateBy { it.name }
    TODO()

    /*
    val targetProperties = this::class.memberProperties.associateBy { it.name }
    source::class.memberProperties.forEach { sourceProp ->
        targetProperties[sourceProp.name]?.let { targetProp ->
            if (targetProp.returnType == sourceProp.returnType) {
                (targetProp as? KMutableProperty<*>)?.setter?.call(this, sourceProp.getter.call(source))
            }
        }
    }
     */

    // return map(map(request, Request::class.java))
}
