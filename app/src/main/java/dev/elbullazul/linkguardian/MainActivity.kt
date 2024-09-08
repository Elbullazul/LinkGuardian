package dev.elbullazul.linkguardian

import android.content.Context
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
import dev.elbullazul.linkguardian.api.APIWrapper
import dev.elbullazul.linkguardian.fragments.LoginFragment
import dev.elbullazul.linkguardian.navigation.AppNavController
import dev.elbullazul.linkguardian.navigation.DASHBOARD
import dev.elbullazul.linkguardian.navigation.LOGIN_SCREEN
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
    val context = LocalContext.current
    val sharedPref =
        context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE) ?: return
    val savedUrl = sharedPref.getString(PREF_SERVER_URL, EMPTY_STRING)!!.toString()
    val savedToken = sharedPref.getString(PREF_API_TOKEN, EMPTY_STRING)!!.toString()

    var startDestination = LOGIN_SCREEN

    if (savedUrl.isNotEmpty() && savedToken.isNotEmpty()) {
        // TODO: validate URL and token
        startDestination = DASHBOARD
    }

    val navController = rememberNavController()

    LinkGuardianTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
            },
            bottomBar = {
                if (startDestination != LOGIN_SCREEN) {
                    BottomAppBar {
                        for (dest in destinations) {
                            NavigationBarItem(
                                selected = false,
                                onClick = { /*TODO*/ },
                                icon = { dest.icon },
                                label = { Text(text = dest.label) }
                            )
                        }
                    }
                }
                else { null }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                AppNavController(navController = navController, startDestination = startDestination)
            }
        }
    }
}