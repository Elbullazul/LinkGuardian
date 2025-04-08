package dev.elbullazul.linkguardian.data.linkwarden.responses

import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenCollectionsResponse(
    @SerialName("response")
    val collections: List<LinkwardenCollection>
)