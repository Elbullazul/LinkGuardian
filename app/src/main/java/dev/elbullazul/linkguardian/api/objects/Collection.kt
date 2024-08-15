package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class Collection(
    var id: Int,
    var name: String,
    var description: String,
    var color: String,
    var parentId: Int?,
    var isPublic: Boolean,
    var ownerId: Int,
    var createdAt: String,
    var updatedAt: String,
    var parent: Id? = null,
    var members: Array<Int>? = null,
    var _count: Count = Count(0)
)