package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class PostLink(
    val collection: PostCollection,
    val description: String = "",
    val image: String = "",
    val monolith: String = "",
    val name: String = "",
    val pdf: String = "",
    val preview: String = "",
    val readable: String = "",
    val tags: Array<PostTag> = arrayOf(),
    val textContent: String = "",
    val type: String = "url",
    val url: String
)