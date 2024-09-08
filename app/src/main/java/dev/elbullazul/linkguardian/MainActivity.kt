package dev.elbullazul.linkguardian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import dev.elbullazul.linkguardian.navigation.AppNavController
import dev.elbullazul.linkguardian.navigation.DASHBOARD
import dev.elbullazul.linkguardian.navigation.LOGIN_SCREEN
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
    var startDestination = LOGIN_SCREEN

    val context = LocalContext.current
    val prefs = PreferencesManager(context)
    val navController = rememberNavController()

    if (prefs.load()) {
        // TODO: validate that values are still valid

        startDestination = DASHBOARD
    }

    LinkGuardianTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
            },
            bottomBar = {
                // TODO: make check dynamic
                if (startDestination != LOGIN_SCREEN) {
                    BottomAppBar {
                        for (dest in destinations) {
                            NavigationBarItem(
                                selected = false,
                                onClick = { navController.navigate(dest.route) },
                                icon = { dest.icon },
                                label = { Text(text = dest.label) }
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                AppNavController(navController = navController, startDestination = startDestination)
            }
        }
    }
}