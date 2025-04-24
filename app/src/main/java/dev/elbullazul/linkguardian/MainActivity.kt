package dev.elbullazul.linkguardian

import android.annotation.SuppressLint
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
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import dev.elbullazul.linkguardian.data.DataFactory
import dev.elbullazul.linkguardian.navigation.AppNavController
import dev.elbullazul.linkguardian.navigation.BOOKMARKS
import dev.elbullazul.linkguardian.navigation.BOOKMARK_EDITOR
import dev.elbullazul.linkguardian.navigation.COLLECTIONS
import dev.elbullazul.linkguardian.navigation.LOGIN
import dev.elbullazul.linkguardian.navigation.SETTINGS
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

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val preferences = PreferencesManager(context)

    val displayBottomBar = rememberSaveable { (mutableStateOf(false)) }
    val displayBackButton = rememberSaveable { (mutableStateOf(false)) }
    val displayFloatingButton = rememberSaveable { (mutableStateOf(false)) }
    val loggedIn = rememberSaveable { (mutableStateOf(preferences.validCredentials())) }

    val intent = context.findActivity()?.intent
    val dataFactory = DataFactory(preferences.getBackendType())
    val backend = dataFactory.backend(preferences.getScheme(), preferences.getDomain(), preferences.getToken())

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    if (intent?.action == Intent.ACTION_MAIN) {
        // TODO: replace this temporary hacky solution with something more standard
        when (navBackStackEntry?.destination?.route.toString().split("?").first().split(".").last()) {
            LOGIN::class.simpleName -> {
                displayBottomBar.value = false
                displayFloatingButton.value = false
                displayBackButton.value = false
            }

            BOOKMARKS::class.simpleName -> {
                displayBottomBar.value = true
                displayFloatingButton.value = true
                displayBackButton.value = false
            }

            COLLECTIONS::class.simpleName -> {
                displayBottomBar.value = true
                displayFloatingButton.value = false
                displayBackButton.value = false
            }

            SETTINGS::class.simpleName -> {
                displayBottomBar.value = true
                displayFloatingButton.value = false
                displayBackButton.value = false
            }

            BOOKMARK_EDITOR::class.simpleName -> {
                displayBottomBar.value = true
                displayFloatingButton.value = false
                displayBackButton.value = true
            }
        }
    }

    LinkGuardianTheme(highContrast = preferences.getHighContrastTheme()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    navigationIcon = {
                        if (displayBackButton.value) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (displayBottomBar.value) {
                    BottomAppBar {
                        destinations.forEach { dest ->
                            NavigationBarItem(
                                selected = routeMatches(navBackStackEntry?.destination, dest.route),
                                icon = { Icon(imageVector = dest.icon, contentDescription = stringResource(dest.label)) },
                                label = { Text(text = stringResource(dest.label)) },
                                onClick = {
                                    navController.navigate(dest.route)
//                                    navController.navigate(dest.route) {
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//                                    }
                                }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (displayFloatingButton.value) {
                    FloatingActionButton(onClick = { navController.navigate(BOOKMARK_EDITOR()) }) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                AppNavController(
                    backend = backend,
                    navController = navController,
                    startDestination = if (!loggedIn.value) {
                        LOGIN
                    } else if (intent?.action == Intent.ACTION_SEND) {
                        BOOKMARK_EDITOR(bookmarkUrl = intent.getStringExtra(Intent.EXTRA_TEXT))
                    } else {
                        BOOKMARKS()
                    }
                )
            }
        }
    }
}

fun routeMatches(destination: NavDestination?, route: Any): Boolean {
    // TODO: surely there is a less hacky way to do this!
    return destination?.route.toString().split("?").first().split(".").last() == route::class.simpleName
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}