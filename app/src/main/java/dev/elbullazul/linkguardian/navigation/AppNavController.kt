package dev.elbullazul.linkguardian.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.findActivity
import dev.elbullazul.linkguardian.ui.pages.LoginPage
import dev.elbullazul.linkguardian.ui.pages.BookmarkListPage
import dev.elbullazul.linkguardian.ui.pages.CollectionListPage
import dev.elbullazul.linkguardian.ui.pages.SettingsPage
import dev.elbullazul.linkguardian.ui.pages.BookmarkEditorPage
import dev.elbullazul.linkguardian.ui.pages.CollectionEditorPage
import dev.elbullazul.linkguardian.ui.pages.TagListPage

@Composable
fun AppNavController(
    backend: Backend,
    startDestination: Any,
    navController: NavHostController
) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = startDestination) {
        composable<LOGIN> {
            LoginPage(
                backend = backend,
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
                onEdit = { navController.navigate(BOOKMARK_EDITOR(bookmarkId = it)) },
                onTagClick = { navController.navigate(BOOKMARKS(tagId = it)) }
            )
        }
        composable<BOOKMARK_EDITOR> { backStackEntry ->
            val route = backStackEntry.toRoute<BOOKMARK_EDITOR>()

            BookmarkEditorPage(
                bookmarkId = route.bookmarkId,
                bookmarkUrl = route.bookmarkUrl,
                backend = backend,
                onSubmit = {
                    // exit app if bookmarkUrl is not empty (when sharing from external apps)
                    if (!route.bookmarkUrl.isNullOrBlank())
                        context.findActivity()?.finish()
                    else
                        navController.navigate(BOOKMARKS())
                }
            )
        }
        composable<COLLECTIONS> {
            CollectionListPage(
                backend = backend,
                onEdit = { navController.navigate(COLLECTION_EDITOR(collectionId = it)) },
                onClick = { navController.navigate(BOOKMARKS(collectionId = it)) }
            )
        }
        composable<COLLECTION_EDITOR> { backStackEntry ->
            val route = backStackEntry.toRoute<COLLECTION_EDITOR>()

            CollectionEditorPage(
                collectionId = route.collectionId,
                onSubmit = { navController.navigate(COLLECTIONS) }
            )
        }
        composable<TAGS> {
            TagListPage(
                onClick = { navController.navigate(BOOKMARKS(tagId = it)) },
                onEdit = {}
            )
        }
        composable<SETTINGS> {
            SettingsPage(
                backend = backend,
                onLogout = { navController.navigate(LOGIN) }
            )
        }
    }
}