package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class Id(
    var id: Int,
    var name: String = ""
)