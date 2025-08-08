package io.dnajd.mainservice.infrastructure

import dev.krud.shapeshift.transformer.ImplicitCollectionMappingTransformer
import dev.krud.shapeshift.transformer.base.MappingTransformer
import dev.krud.shapeshift.transformer.base.MappingTransformerContext
import java.lang.reflect.ParameterizedType

/**
 * I don't know how I don't know why, but for some reason the type does not seem to resolve correctly so I have to do this
 * @see ImplicitCollectionMappingTransformer
 */
class ImplicitCollectionMappingTransformerFixed : MappingTransformer<Collection<Any>, Collection<Any>> {
    override fun transform(context: MappingTransformerContext<out Collection<Any>>): Collection<Any>? {
        context.originalValue ?: return null
        val type = context.toField.genericType as? ParameterizedType ?: return null
        val collectionType = type.actualTypeArguments[0] as? Class<*> ?: return null
        val se = type.rawType.typeName
        val ere = type.rawType.javaClass
        val baseMapping = context.originalValue!!.map { context.shapeShift.map(it, collectionType) }
        val ge = se + "" + ere
        return when (type.rawType.typeName) {
            List::class.java.typeName -> {
                baseMapping
            }
            MutableList::class.java.typeName -> {
                baseMapping.toMutableList()
            }
            Set::class.java.typeName -> {
                baseMapping.toSet()
            }
            MutableSet::class.java.typeName -> {
                baseMapping.toMutableSet()
            }
            else -> {
                baseMapping
            }
        }
    }
}
