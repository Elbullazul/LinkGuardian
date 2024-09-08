package dev.elbullazul.linkguardian.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.elbullazul.linkguardian.EMPTY_STRING
import dev.elbullazul.linkguardian.PREFERENCES_KEY_FILE
import dev.elbullazul.linkguardian.PREF_API_TOKEN
import dev.elbullazul.linkguardian.PREF_SERVER_URL
import dev.elbullazul.linkguardian.api.APIWrapper
import dev.elbullazul.linkguardian.fragments.LinkList
import dev.elbullazul.linkguardian.fragments.LoginFragment

@Composable
fun AppNavController(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(DASHBOARD) {
            // TODO: wrapper doesn't work at all. Have each screen create its own instance, or find a way to share a wrapper (not sure if good idea)
            val context = LocalContext.current
            val sharedPref =
                context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
            val savedUrl = sharedPref.getString(PREF_SERVER_URL, EMPTY_STRING)!!.toString()
            val savedToken = sharedPref.getString(PREF_API_TOKEN, EMPTY_STRING)!!.toString()

            val wrapper = APIWrapper(context, savedUrl, savedToken)

            LinkList(wrapper = wrapper)
        }
        composable(LOGIN_SCREEN) { LoginFragment() }
        composable(SETTINGS) { /* TODO: call settings screen when implemented */ }
    }
}