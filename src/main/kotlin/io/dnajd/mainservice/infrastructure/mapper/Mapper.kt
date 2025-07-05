package io.dnajd.mainservice.infrastructure.mapper

import dev.krud.shapeshift.ShapeShift
import dev.krud.shapeshift.ShapeShiftBuilder
import io.dnajd.mainservice.infrastructure.ImplicitCollectionMappingTransformerFixed
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@Configuration
class Mapper {

    @Bean
    fun shapeShiftMapper() = ShapeShiftBuilder().withTransformer(ImplicitCollectionMappingTransformerFixed()).build()
}

/**
 * Maps [input] to [T1] using shapeshift then invokes [ShapeShift.mapChangedFieldsSameType]
 * NOTE [input] must be mappable to [mapTo] and [T1] must have no-args constructor
 * @param mapTo Entity which will have the values changed
 * @param input Dto from which to determine which values need to be changed in [mapTo]
 * @see ShapeShift.mapChangedFieldsSameType
 */
inline fun <reified T1 : Any, T2 : Any> ShapeShift.mapChangedFields(mapTo: T1, input: T2): T1 {
    val mappedInput: T1 = this.map(input)
    return this.mapChangedFieldsSameType(mapTo = mapTo, input = mappedInput)
}

/**
 * Compares which values are different from [input] and default instance of [T] and the values that differ get set to
 * [mapTo], this is mainly used for updating data, since usually Post and Update request are able to change the same
 * parameters.
 * NOTE [T] must have no-args constructor
 * @see ShapeShift.mapChangedFields
 */
fun <T : Any> ShapeShift.mapChangedFieldsSameType(mapTo: T, input: T): T {
    val defaultInstance = input::class.primaryConstructor!!.callBy(emptyMap())
    val properties = (input::class as KClass).memberProperties
    val parameters = (input::class as KClass).primaryConstructor!!.parameters

    val finalProperties = mutableMapOf<KParameter, Any?>()
    properties.forEach { property ->
        val inputValue = property.getter.call(input)
        val parameter = parameters.find { o -> o.name == property.name }!!

        if (inputValue != property.getter.call(defaultInstance)) {
            finalProperties[parameter] = inputValue
        } else {
            finalProperties[parameter] = property.getter.call(mapTo)
        }
    }

    return input::class.primaryConstructor!!.callBy(finalProperties);
}
