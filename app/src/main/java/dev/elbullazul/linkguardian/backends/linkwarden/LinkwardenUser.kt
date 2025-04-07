package dev.elbullazul.linkguardian.backends.linkwarden

import dev.elbullazul.linkguardian.backends.generic.User
import kotlinx.serialization.Serializable

@Serializable
class LinkwardenUser(
    override val id: Int,
    override val username: String,
    val email: String? = "",
    val name: String? = "",
    val image: String? = "",
    val archiveAsScreenshot: Boolean? = false,
    val archiveAsMonolith: Boolean? = false,
    val archiveAsPDF: Boolean? = false,
    val emailVerified: String? = "",
    val subscriptions: String? = "",
    val createdAt: String = ""             // TODO: use date time
): User