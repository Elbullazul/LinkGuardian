package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class DashboardResponse(
    var response: Array<Link>
)