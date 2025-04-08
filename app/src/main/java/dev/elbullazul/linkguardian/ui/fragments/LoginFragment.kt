package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.storage.SCHEME_HTTP
import dev.elbullazul.linkguardian.storage.SCHEME_HTTPS
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun LoginFragment(backend: Backend, preferences: PreferencesManager, onLogin: () -> Unit) {
    val context = LocalContext.current

    val useHttps = remember { mutableStateOf(true) }
    val serverUrl = remember { mutableStateOf(preferences.domain) }
    val apiToken = remember { mutableStateOf(preferences.token) }

    Column(
        modifier = Modifier
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = serverUrl.value,
            onValueChange = {  serverUrl.value = it },
            placeholder = { Text(stringResource(id = R.string.server_url_placeholder)) },
            label = { Text(stringResource(id = R.string.server_url)) }
        )
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = apiToken.value,
            placeholder = { Text(stringResource(id = R.string.api_token_placeholder)) },
            onValueChange = {  apiToken.value = it },
            label = { Text(stringResource(id = R.string.api_token)) }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = true,
                onCheckedChange = { useHttps.value = it }
            )
            Text(text = stringResource(R.string.use_https))
        }
        Button(
            modifier = Modifier.padding(vertical = 10.dp),
            onClick = {
                backend.scheme = if (useHttps.value) SCHEME_HTTPS else SCHEME_HTTP
                backend.domain = serverUrl.value
                backend.token = apiToken.value

                if (!backend.isReachable()) {
                    ShowToast(context, context.getString(R.string.domain_unreachable))
                }
                else if (!backend.isAuthorized()) {
                    ShowToast(context, context.getString(R.string.access_denied))
                }
                else {
                    preferences.scheme = backend.scheme
                    preferences.domain = backend.domain
                    preferences.token = backend.token

                    // TODO: save backend type when multiple backends are supported
                    preferences.persist()

                    onLogin()
                }
            }
        ) {
            Text(stringResource(id = R.string.login))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LinkGuardianTheme(darkTheme = true) {
        LoginFragment(
            backend = LinkwardenBackend("", "", ""),
            preferences = PreferencesManager(LocalContext.current),
            onLogin = {}
        )
    }
}