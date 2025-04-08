package dev.elbullazul.linkguardian.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.elbullazul.linkguardian.backends.generic.Backend
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.pages.CollectionsPage
import dev.elbullazul.linkguardian.ui.pages.BookmarksPage
import dev.elbullazul.linkguardian.ui.fragments.LoginFragment
import dev.elbullazul.linkguardian.ui.pages.SettingsPage
import dev.elbullazul.linkguardian.ui.pages.SubmitBookmarkPage

@Composable
fun AppNavController(
    navController: NavHostController,
    preferences: PreferencesManager,
    backend: Backend,
    startDestination: String
) {
    // TODO: block navigation to the current page?

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NAV_ROUTE_DASHBOARD) {
            BookmarksPage(
                backend = backend,
                preferences = preferences
            )
        }
        composable(NAV_ROUTE_LOGIN) {
            // TODO: rework to support dynamically swapping backends (container class?)
            LoginFragment(
                backend = backend,
                preferences = preferences,
                onLogin = { navController.navigate(NAV_ROUTE_DASHBOARD) }
            )
        }
        composable(NAV_ROUTE_SETTINGS) {
            SettingsPage(
                preferences = preferences,
                onLogout = { navController.navigate(NAV_ROUTE_LOGIN) }
            )
        }
        composable(NAV_ROUTE_SUBMIT_LINK) {
            SubmitBookmarkPage(
                backend = backend,
                preferences = preferences,
                onSubmit = { navController.navigate(NAV_ROUTE_DASHBOARD) },
            )
        }
        composable(NAV_ROUTE_COLLECTIONS) {
            CollectionsPage()
        }
    }
}