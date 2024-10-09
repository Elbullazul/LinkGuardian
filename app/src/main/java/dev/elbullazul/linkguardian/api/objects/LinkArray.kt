package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinkArray (
    @SerialName("response")
    var items: Array<Link> = arrayOf()
)