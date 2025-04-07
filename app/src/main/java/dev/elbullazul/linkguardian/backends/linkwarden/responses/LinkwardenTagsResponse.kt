package dev.elbullazul.linkguardian.backends.linkwarden.responses

import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenTag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenTagsResponse(
    @SerialName("response")
    val tags: List<LinkwardenTag>
)