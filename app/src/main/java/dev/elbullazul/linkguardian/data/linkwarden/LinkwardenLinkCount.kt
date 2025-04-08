package dev.elbullazul.linkguardian.data.linkwarden

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenLinkCount(
    @SerialName("links")
    val count: Int = 0
)