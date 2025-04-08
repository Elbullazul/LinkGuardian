package dev.elbullazul.linkguardian.data.linkwarden

import dev.elbullazul.linkguardian.data.extensions.Colorizable
import dev.elbullazul.linkguardian.data.extensions.Creatable
import dev.elbullazul.linkguardian.data.extensions.Describable
import dev.elbullazul.linkguardian.data.extensions.Iconifiable
import dev.elbullazul.linkguardian.data.extensions.Previewable
import dev.elbullazul.linkguardian.data.extensions.Updateable
import dev.elbullazul.linkguardian.data.generic.Bookmark
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenLink(
    override val id: Int,
    override val name: String,
    val type: String = "",
    override val description: String,
    val createdById: Int = -1,
    val collectionId: Int = -1,
    override val icon: String? = null,
    override val iconWeight: String? = null,
    override val color: String? = null,         // doc says it's not supposed to be null?
    override val url: String,
    val textContent: String? = null,
    override val preview: String? = null,
    val image: String? = null,
    val pdf: String? = null,
    val readable: String? = null,
    val monolith: String? = null,
    val lastPreserved: String? = null,          // TODO: date tome
    val importDate: String? = null,             // TODO: date time
    override val createdAt: String = "",
    override val updatedAt: String = "",
    override val tags: List<LinkwardenTag> = listOf(),
    val collection: LinkwardenCollection? = null,
    val pinnedBy: List<Int> = listOf()
) : Bookmark, Describable, Previewable, Creatable, Updateable, Colorizable, Iconifiable