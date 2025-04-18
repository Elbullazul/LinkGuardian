package dev.elbullazul.linkguardian.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.pages.LoginPage
import dev.elbullazul.linkguardian.ui.pages.BookmarkListPage
import dev.elbullazul.linkguardian.ui.pages.CollectionListPage
import dev.elbullazul.linkguardian.ui.pages.SettingsPage
import dev.elbullazul.linkguardian.ui.pages.BookmarkEditorPage

@Composable
fun AppNavController(
    navController: NavHostController,
    preferences: PreferencesManager,
    backend: Backend,
    startDestination: Any
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<LOGIN> {
            LoginPage(
                backend = backend,
                preferences = preferences,
                onLogin = { navController.navigate(BOOKMARKS()) }
            )
        }
        composable<BOOKMARKS> { backStackEntry->
            val route = backStackEntry.toRoute<BOOKMARKS>()

            // re-fetch bookmark list
            backend.reset()

            BookmarkListPage(
                collectionId = route.collectionId,
                tagId = route.tagId,
                backend = backend,
                preferences = preferences,
                onEdit = { bookmarkId ->
                    navController.navigate(BOOKMARK_EDITOR(bookmarkId))
                },
                onTagClick = { tagId ->
                    navController.navigate(BOOKMARKS(tagId = tagId))
                }
            )
        }
        composable<BOOKMARK_EDITOR> { backStackEntry ->
            BookmarkEditorPage(
                backend = backend,
                preferences = preferences,
                onSubmit = { navController.navigate(BOOKMARKS()) },
                bookmarkId = backStackEntry.toRoute<BOOKMARK_EDITOR>().bookmarkId
            )
        }
        composable<COLLECTIONS> {
            CollectionListPage(
                backend = backend,
                onClick = { id ->
                    navController.navigate(BOOKMARKS(collectionId = id))
                }
            )
        }
        composable<SETTINGS> {
            SettingsPage(
                preferences = preferences,
                onLogout = { navController.navigate(LOGIN) }
            )
        }
    }
}