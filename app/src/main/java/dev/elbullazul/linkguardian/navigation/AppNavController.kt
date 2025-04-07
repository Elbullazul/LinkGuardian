package dev.elbullazul.linkguardian.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.elbullazul.linkguardian.ui.pages.CollectionsPage
import dev.elbullazul.linkguardian.ui.pages.BookmarksPage
import dev.elbullazul.linkguardian.ui.fragments.LoginFragment
import dev.elbullazul.linkguardian.ui.pages.SettingsPage
import dev.elbullazul.linkguardian.ui.fragments.SubmitLinkFragment

@Composable
fun AppNavController(navController: NavHostController, startDestination: String) {
    // TODO: block navigation to the current page?

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NAV_ROUTE_DASHBOARD) {
//            val context = LocalContext.current
//            val preferencesManager = PreferencesManager(context)
//            preferencesManager.load()

            BookmarksPage()
        }
        composable(NAV_ROUTE_LOGIN) {
            LoginFragment(
                onLogin = { navController.navigate(NAV_ROUTE_DASHBOARD) }
            )
        }
        composable(NAV_ROUTE_SETTINGS) {
            SettingsPage(
                onLogout = { navController.navigate(NAV_ROUTE_LOGIN) }
            )
        }
        composable(NAV_ROUTE_SUBMIT_LINK) {
            SubmitLinkFragment(
                onSubmit = { navController.navigate(NAV_ROUTE_DASHBOARD) }
            )
        }
        composable(NAV_ROUTE_COLLECTIONS) {
            CollectionsPage()
        }
    }
}