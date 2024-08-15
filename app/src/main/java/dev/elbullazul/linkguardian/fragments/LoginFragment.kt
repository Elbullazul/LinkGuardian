package dev.elbullazul.linkguardian.fragments

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elbullazul.linkguardian.EMPTY_STRING
import dev.elbullazul.linkguardian.PREFERENCES_KEY_FILE
import dev.elbullazul.linkguardian.PREF_API_TOKEN
import dev.elbullazul.linkguardian.PREF_SERVER_URL
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.api.APIWrapper

@Composable
fun LoginFragment(context: Context, modifier: Modifier = Modifier) {
    // temporary, to facilitate testing. Once app is ready, redirect to dashboard instead
    val sharedPref =
        context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE) ?: return
    val savedUrl = sharedPref.getString(PREF_SERVER_URL, EMPTY_STRING)!!.toString()
    val savedToken = sharedPref.getString(PREF_API_TOKEN, EMPTY_STRING)!!.toString()

    val serverUrl = remember { mutableStateOf(savedUrl) }
    val apiToken = remember { mutableStateOf(savedToken) }

    Column(
        modifier = modifier
            .padding(horizontal = 35.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.padding(vertical = 10.dp),
            fontSize = 30.sp
        )
        TextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            value = serverUrl.value,
            onValueChange = { serverUrl.value = it },
            placeholder = { Text(stringResource(id = R.string.server_url_placeholder)) },
            label = { Text(stringResource(id = R.string.server_url)) }
        )
        TextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            value = apiToken.value,
            placeholder = { Text(stringResource(id = R.string.api_token_placeholder)) },
            onValueChange = { apiToken.value = it },
            label = { Text(stringResource(id = R.string.api_token)) }
        )
        LoginButton(
            context = context,
            modifier = Modifier.padding(vertical = 10.dp),
            serverUrl = serverUrl.value,
            apiToken = apiToken.value
        )
    }
}

@Composable
fun LoginButton(context: Context, modifier: Modifier, serverUrl: String, apiToken: String) {
    val unreachableMsg = stringResource(id = R.string.server_unreachable)
    val invalidTokenMsg = stringResource(id = R.string.could_not_authenticate)

    Button(
        modifier = modifier,
        onClick = {
            val wrapper = APIWrapper(context, serverUrl, apiToken)

            if (wrapper.serverReachable()) {
                val response = wrapper.dashboardData()

                println(response.message)
                println(response.code)

                if (response.isSuccessful) {
                    println(wrapper.dashboardData().message)

                    persistUserData(context, serverUrl, apiToken)

                    // TODO: navigate to dashboard view
                }
                else {
                    ShowToast(context, invalidTokenMsg)
                }
            }
            else {
                ShowToast(context, unreachableMsg)
            }
        }
    ) {
        Text(stringResource(id = R.string.login))
    }
}

fun persistUserData(context: Context, serverUrl: String, apiToken: String) {
    val sharedPref = context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE) ?: return

    with(sharedPref.edit()) {
        putString(PREF_SERVER_URL, serverUrl)
        putString(PREF_API_TOKEN, apiToken)
        apply()
    }
}