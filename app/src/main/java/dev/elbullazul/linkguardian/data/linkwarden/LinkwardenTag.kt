package dev.elbullazul.linkguardian.data.linkwarden

import dev.elbullazul.linkguardian.data.extensions.Creatable
import dev.elbullazul.linkguardian.data.extensions.Updateable
import dev.elbullazul.linkguardian.data.generic.Tag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenTag(
    override val id: Int,
    override val name: String,
    val ownerId: Int = -1,
    override val createdAt: String = "",
    override val updatedAt: String = "",

    @SerialName("_count.links")
    val links: Int? = 0
) : Tag, Creatable, Updateable