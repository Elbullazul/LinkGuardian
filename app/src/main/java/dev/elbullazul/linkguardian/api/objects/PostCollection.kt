package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class PostCollection(
    val name: String,
    val ownerId: Int
) {
    constructor(collection: Collection?): this(
        collection?.name ?: "",
        collection?.ownerId ?: -1
    )
}