package dev.elbullazul.linkguardian.backends.linkwarden.responses

import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenCollection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenCollectionResponse(
    @SerialName("response")
    val collection: LinkwardenCollection
)