package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CollectionArray (
    @SerialName("response")
    var items: Array<Collection> = arrayOf()
)