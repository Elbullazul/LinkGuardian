package dev.elbullazul.linkguardian.backends.linkwarden.responses

import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenUserResponse(
    @SerialName("response")
    val user: LinkwardenUser
)