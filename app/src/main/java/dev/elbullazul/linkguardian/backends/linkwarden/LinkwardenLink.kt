package dev.elbullazul.linkguardian.backends.linkwarden

import dev.elbullazul.linkguardian.backends.generic.Bookmark
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenLink(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val url: String,
    override val tags: List<LinkwardenTag> = listOf(),

    // specific to linkwarden
    val type: String = "",
    val createdById: Int = -1,
    val collectionId: Int = -1,
    val icon: String? = null,
    val iconWeight: String? = null,
    val color: String? = null,                  // doc says it's not supposed to be null?
    val textContent: String? = null,
    val preview: String? = null,
    val image: String? = null,
    val pdf: String? = null,
    val readable: String? = null,
    val monolith: String? = null,
    val lastPreserved: String? = null,          // TODO: date tome
    val importDate: String? = null,             // TODO: date time
    val createdAt: String = "",                 // TODO: date time
    val updatedAt: String = "",                 // TODO: date time

    val collection: LinkwardenCollection? = null
) : Bookmark