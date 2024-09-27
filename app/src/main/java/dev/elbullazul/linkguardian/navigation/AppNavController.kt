package dev.elbullazul.linkguardian.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.elbullazul.linkguardian.fragments.LinkList
import dev.elbullazul.linkguardian.fragments.LoginFragment
import dev.elbullazul.linkguardian.fragments.SettingsFragment
import dev.elbullazul.linkguardian.fragments.SubmitLinkFragment
import dev.elbullazul.linkguardian.storage.PreferencesManager

@Composable
fun AppNavController(navController: NavHostController, startDestination: String) {
    // TODO: block navigation to the current page

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_DASHBOARD) {
            val context = LocalContext.current
            val preferencesManager = PreferencesManager(context)
            preferencesManager.load()

            LinkList()
        }
        composable(ROUTE_LOGIN) {
            LoginFragment(
                onLogin = { navController.navigate(ROUTE_DASHBOARD) }
            )
        }
        composable(ROUTE_SETTINGS) {
            SettingsFragment(
                onLogout = { navController.navigate(ROUTE_LOGIN) }
            )
        }
        composable(ROUTE_SUBMIT_LINK) {
            SubmitLinkFragment()
        }
    }
}