package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class ArrayResponse(
    var response: Array<Link> = arrayOf()
)