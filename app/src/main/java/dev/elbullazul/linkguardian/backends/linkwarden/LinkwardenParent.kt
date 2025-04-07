package dev.elbullazul.linkguardian.backends.linkwarden

import kotlinx.serialization.Serializable

@Serializable
class LinkwardenParent(
    val id: Int,
    val name: String
)