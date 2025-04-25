package dev.elbullazul.linkguardian.data.linkwarden

import dev.elbullazul.linkguardian.data.extensions.Creatable
import dev.elbullazul.linkguardian.data.extensions.Updateable
import dev.elbullazul.linkguardian.data.generic.Data
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenMember(
    var userId: String?,
    var collectionId: Int?,
    var canCreate: Boolean?,
    var canUpdate: Boolean?,
    var canDelete: Boolean?,
    override val createdAt: String?,
    override val updatedAt: String?,
) : Creatable, Updateable, Data