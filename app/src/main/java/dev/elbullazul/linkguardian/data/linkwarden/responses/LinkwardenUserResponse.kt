package dev.elbullazul.linkguardian.data.linkwarden.responses

import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenUserResponse(
    @SerialName("response")
    val user: LinkwardenUser
)