package dev.elbullazul.linkguardian.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.elbullazul.linkguardian.api.APIWrapper
import dev.elbullazul.linkguardian.fragments.LinkList
import dev.elbullazul.linkguardian.fragments.LoginFragment
import dev.elbullazul.linkguardian.storage.PreferencesManager

@Composable
fun AppNavController(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(DASHBOARD) {
            // TODO: wrapper doesn't work at all. Have each screen create its own instance, or find a way to share a wrapper (not sure if good idea)
            val context = LocalContext.current
            val preferencesManager = PreferencesManager(context)
            preferencesManager.load()

            val wrapper = APIWrapper(context, preferencesManager.domain, preferencesManager.token)

            LinkList(wrapper = wrapper)
        }
        composable(LOGIN_SCREEN) {
            LoginFragment(
                onLogin = { navController.navigate(DASHBOARD) }
            )
        }
        composable(SETTINGS) { /* TODO: call settings screen when implemented */ }
    }
}