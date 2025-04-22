package dev.elbullazul.linkguardian.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dev.elbullazul.linkguardian.R
import kotlinx.serialization.Serializable

// routes
@Serializable data object LOGIN
@Serializable data class BOOKMARKS(val collectionId: String? = null, val tagId: String? = null)
@Serializable data class BOOKMARK_EDITOR(val bookmarkId: String? = null, val bookmarkUrl: String? = null)
@Serializable data object COLLECTIONS
@Serializable data object SETTINGS

class Destination(
    val label: Int,
    val icon: ImageVector,
    val route: Any
)

val destinations = listOf(
    Destination(label = R.string.dashboard, icon = Icons.Outlined.Home, route = BOOKMARKS()),
    Destination(label = R.string.collections, icon = Icons.AutoMirrored.Outlined.List, route = COLLECTIONS),
    Destination(label = R.string.settings, icon = Icons.Outlined.Settings, route = SETTINGS)
)
