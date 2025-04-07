package dev.elbullazul.linkguardian.backends.linkwarden.responses

import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenUsersResponse(
    @SerialName("response")
    val users: List<LinkwardenUser> = listOf()
)