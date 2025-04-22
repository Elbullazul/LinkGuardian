package dev.elbullazul.linkguardian.data.linkwarden

import dev.elbullazul.linkguardian.data.extensions.Colorizable
import dev.elbullazul.linkguardian.data.extensions.Creatable
import dev.elbullazul.linkguardian.data.extensions.Describable
import dev.elbullazul.linkguardian.data.extensions.Iconifiable
import dev.elbullazul.linkguardian.data.extensions.ParentOfMany
import dev.elbullazul.linkguardian.data.extensions.Updateable
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.generic.Collection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenCollection(
    val id: Int,
    override val name: String,
    override val description: String = "",
    override val icon: String? = null,
    override val iconWeight: String? = null,
    override val color: String? = null,        // doc says it's not supposed to be null?
    val parentId: Int? = null,
    val isPublic: Boolean = false,
    val ownerId: Int = -1,
    val createdById: Int = -1,
    override val createdAt: String = "",
    override val updatedAt: String = "",

    @SerialName("_count")
    private val linkCount: LinkwardenLinkCount = LinkwardenLinkCount(0),

    @SerialName("members")
    private val members: List<LinkwardenMember> = listOf(),

    @SerialName("parent")
    private val parent: LinkwardenCollection? = null
) : Collection, Describable, Creatable, Updateable, Colorizable, Iconifiable, ParentOfMany {
    override fun getId(): String {
        return id.toString()
    }

    override fun getChildCount(): Int {
        return linkCount.value
    }
}