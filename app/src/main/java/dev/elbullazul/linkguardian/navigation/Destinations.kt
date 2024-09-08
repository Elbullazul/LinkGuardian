package dev.elbullazul.linkguardian.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// routes
val DASHBOARD = "dashboard"
val LOGIN_SCREEN = "login"
val SETTINGS = "settings"

class Destination(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val destinations = listOf(
    Destination(label = "home", icon = Icons.Outlined.Home, route = DASHBOARD),
    Destination(label = "settings", icon = Icons.Outlined.Settings, route = SETTINGS)
)
