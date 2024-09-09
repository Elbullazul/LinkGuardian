package dev.elbullazul.linkguardian.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dev.elbullazul.linkguardian.R

// routes
const val ROUTE_DASHBOARD = "dashboard"
const val ROUTE_LOGIN = "login"
const val ROUTE_SETTINGS = "settings"
const val ROUTE_ADD_LINK = "add_link"

class Destination(
    val label: Int,
    val icon: ImageVector,
    val route: String
)

val destinations = listOf(
    Destination(label = R.string.dashboard, icon = Icons.Outlined.Home, route = ROUTE_DASHBOARD),
    Destination(label = R.string.settings, icon = Icons.Outlined.Settings, route = ROUTE_SETTINGS),
)
