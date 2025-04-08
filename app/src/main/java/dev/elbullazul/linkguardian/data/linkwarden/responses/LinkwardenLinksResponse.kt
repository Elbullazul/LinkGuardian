package dev.elbullazul.linkguardian.data.linkwarden.responses

import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenLinksResponse(
    @SerialName("response")
    val links: List<LinkwardenLink>
)