package dev.elbullazul.linkguardian.backends.linkwarden.responses

import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenLinksResponse(
    @SerialName("response")
    val links: List<LinkwardenLink>
)