package dev.elbullazul.linkguardian

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.navigation.AppNavController
import dev.elbullazul.linkguardian.navigation.NAV_ROUTE_COLLECTIONS
import dev.elbullazul.linkguardian.navigation.NAV_ROUTE_DASHBOARD
import dev.elbullazul.linkguardian.navigation.NAV_ROUTE_LOGIN
import dev.elbullazul.linkguardian.navigation.NAV_ROUTE_SETTINGS
import dev.elbullazul.linkguardian.navigation.NAV_ROUTE_SUBMIT_LINK
import dev.elbullazul.linkguardian.navigation.destinations
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val context = LocalContext.current
    val preferences = PreferencesManager(context)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val loggedIn = rememberSaveable { (mutableStateOf(false)) }
    val displayBottomBar = rememberSaveable { (mutableStateOf(false)) }
    val displayBackButton = rememberSaveable { (mutableStateOf(false)) }
    val displayFloatingButton = rememberSaveable { (mutableStateOf(false)) }

    if (preferences.load())
        loggedIn.value = true

    val backend = LinkwardenBackend(preferences.scheme, preferences.domain, preferences.token)

    when (navBackStackEntry?.destination?.route) {
        NAV_ROUTE_LOGIN -> {
            displayBottomBar.value = false
            displayFloatingButton.value = false
            displayBackButton.value = false
        }

        NAV_ROUTE_DASHBOARD -> {
            displayBottomBar.value = true
            displayFloatingButton.value = true
            displayBackButton.value = false
        }

        NAV_ROUTE_COLLECTIONS -> {
            displayBottomBar.value = true
            displayFloatingButton.value = false
            displayBackButton.value = false
        }

        NAV_ROUTE_SETTINGS -> {
            displayBottomBar.value = true
            displayFloatingButton.value = false
            displayBackButton.value = false
        }

        NAV_ROUTE_SUBMIT_LINK -> {
            displayBottomBar.value = true
            displayFloatingButton.value = false
            displayBackButton.value = true
        }
    }

    // TODO: use `preferences.oledTheme` to enable/disable dark theme when building the theme
    LinkGuardianTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    navigationIcon = {
                        if (displayBackButton.value) {
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (displayBottomBar.value) {
                    BottomAppBar {
                        for (dest in destinations) {
                            NavigationBarItem(
                                selected = (navController.currentDestination?.route == dest.route),
                                onClick = { navController.navigate(dest.route) },
                                icon = { Icon(imageVector = dest.icon, contentDescription = "") },
                                label = { Text(text = stringResource(id = dest.label)) }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (displayFloatingButton.value) {
                    FloatingActionButton(
                        onClick = { navController.navigate(NAV_ROUTE_SUBMIT_LINK) },
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                AppNavController(
                    navController = navController,
                    preferences = preferences,
                    backend = backend,
                    startDestination = if (!loggedIn.value) {
                        NAV_ROUTE_LOGIN
                    }
                    else if (context.findActivity()?.intent?.action == Intent.ACTION_SEND) {
                        NAV_ROUTE_SUBMIT_LINK
                    }
                    else {
                        NAV_ROUTE_DASHBOARD
                    }
                )
            }
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}