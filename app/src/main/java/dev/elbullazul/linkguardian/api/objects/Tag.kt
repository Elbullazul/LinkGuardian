package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class Tag(
    var id: Int,
    var name: String,
    var ownerId: Int,
    var createdAt: String,
    var updatedAt: String,
    var _count: Count = Count(0)
)