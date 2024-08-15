package dev.elbullazul.linkguardian

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.elbullazul.linkguardian.fragments.LoginFragment
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkGuardianTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InitContainer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun InitContainer(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val sharedPref =
        context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE) ?: return
    val savedUrl = sharedPref.getString(PREF_SERVER_URL, EMPTY_STRING)
    val savedToken = sharedPref.getString(PREF_API_TOKEN, EMPTY_STRING)

    // TODO: re-enable once app is stable
//    if (!savedUrl.isNullOrEmpty() && !savedToken.isNullOrEmpty()) {
//        ShowToast(context, savedUrl + savedToken)
//
//        // TODO: load complete application
//    }
//    else {
    LoginFragment(
        modifier = modifier,
        context = context
    )
//    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LinkGuardianTheme {
        InitContainer()
    }
}