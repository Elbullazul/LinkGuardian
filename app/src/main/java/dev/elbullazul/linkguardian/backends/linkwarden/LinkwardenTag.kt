package dev.elbullazul.linkguardian.backends.linkwarden

import dev.elbullazul.linkguardian.backends.generic.Tag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenTag(
    override val id: Int,
    override val name: String,

    // specific to linkwarden
    val ownerId: Int = -1,
    val createdAt: String = "",  // TODO: date time
    val updatedAt: String = "",  // TODO: date time

    @SerialName("_count.links")
    val links: Int? = 0
) : Tag