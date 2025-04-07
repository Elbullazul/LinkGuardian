package dev.elbullazul.linkguardian.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dev.elbullazul.linkguardian.R

// routes
const val NAV_ROUTE_DASHBOARD = "dashboard"
const val NAV_ROUTE_LOGIN = "login"
const val NAV_ROUTE_SETTINGS = "settings"
const val NAV_ROUTE_SUBMIT_LINK = "add_link"
const val NAV_ROUTE_COLLECTIONS = "collections"

class Destination(
    val label: Int,
    val icon: ImageVector,
    val route: String
)

val destinations = listOf(
    Destination(label = R.string.dashboard, icon = Icons.Outlined.Home, route = NAV_ROUTE_DASHBOARD),
    Destination(label = R.string.collections, icon = Icons.AutoMirrored.Outlined.List, route = NAV_ROUTE_COLLECTIONS),
    Destination(label = R.string.settings, icon = Icons.Outlined.Settings, route = NAV_ROUTE_SETTINGS)
)
