package dev.elbullazul.linkguardian.data.linkwarden

import dev.elbullazul.linkguardian.data.extensions.Creatable
import dev.elbullazul.linkguardian.data.extensions.Updateable
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenMember(
    val userId: String?,
    val collectionId: Int?,
    val canCreate: Boolean?,
    val canUpdate: Boolean?,
    val canDelete: Boolean?,
    override val createdAt: String?,
    override val updatedAt: String?,
) : Creatable, Updateable