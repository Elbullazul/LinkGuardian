package dev.elbullazul.linkguardian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import dev.elbullazul.linkguardian.api.LinkwardenAPI
import dev.elbullazul.linkguardian.api.SUCCESS
import dev.elbullazul.linkguardian.navigation.AppNavController
import dev.elbullazul.linkguardian.navigation.ROUTE_DASHBOARD
import dev.elbullazul.linkguardian.navigation.ROUTE_LOGIN
import dev.elbullazul.linkguardian.navigation.ROUTE_ADD_LINK
import dev.elbullazul.linkguardian.navigation.destinations
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
    var startDestination = ROUTE_LOGIN

    val context = LocalContext.current
    val apiWrapper = LinkwardenAPI(context)
    val navController = rememberNavController()

    if (apiWrapper.connect() == SUCCESS)
        startDestination = ROUTE_DASHBOARD

    LinkGuardianTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
            },
            bottomBar = {
                // TODO: make check dynamic
                if (startDestination != ROUTE_LOGIN) {
                    BottomAppBar {
                        for (dest in destinations) {
                            NavigationBarItem(
                                selected = true, // TODO: route is null when running this part: (navController.currentDestination?.route == dest.route)
                                onClick = { navController.navigate(dest.route) },
                                icon = { Icon(imageVector = dest.icon, contentDescription = "") },
                                label = { Text(text = stringResource(id = dest.label)) }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (startDestination != ROUTE_LOGIN) {
                    FloatingActionButton(
                        onClick = { navController.navigate(ROUTE_ADD_LINK) },
                    ) {
                        Icon(Icons.Filled.Add, "")
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