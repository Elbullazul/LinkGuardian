package dev.elbullazul.linkguardian.backends.linkwarden

import dev.elbullazul.linkguardian.backends.generic.Bookmark
import dev.elbullazul.linkguardian.backends.generic.Collection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenCollection(
    override val id: Int,
    override val name: String,

    // specific to linkwarden
    val description: String = "",
    val icon: String? = null,
    val iconWeight: String? = null,
    val color: String? = null,        // doc says it's not supposed to be null?
    val parentId: Int? = null,
    val isPublic: Boolean = false,
    val ownerId: Int = -1,
    val createdById: Int = -1,
    val createdAt: String = "",       // TODO: date time
    val updatedAt: String = "",       // TODO: date time

    @SerialName("_count")
    private val linkCount: LinkwardenLinkCount = LinkwardenLinkCount(0),

    @SerialName("members")
    override val links: List<Bookmark> = listOf(),

    @SerialName("parent")
    private val parent: LinkwardenParent? = null

) : Collection {
    override fun bookmarkCount(): Int {
        return linkCount.count
    }

    fun shortDescription(): String {
        if (description.length > 150)
            return description.substring(0,150) + "..."

        return description
    }
}