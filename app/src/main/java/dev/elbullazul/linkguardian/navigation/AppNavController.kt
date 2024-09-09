package dev.elbullazul.linkguardian.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.elbullazul.linkguardian.fragments.LinkList
import dev.elbullazul.linkguardian.fragments.LoginFragment
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
        composable(ROUTE_SETTINGS) { /* TODO: call settings screen when implemented */ }
        composable(ROUTE_ADD_LINK) {
            Column {
                Text(text = "Coming soon")
            }
        }
    }
}