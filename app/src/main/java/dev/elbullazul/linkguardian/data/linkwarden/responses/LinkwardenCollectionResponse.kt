package dev.elbullazul.linkguardian.data.linkwarden.responses

import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenCollectionResponse(
    @SerialName("response")
    val collection: LinkwardenCollection
)