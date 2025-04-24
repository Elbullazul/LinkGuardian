package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.ui.models.LoginViewModel
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun LoginPage(
    backend: Backend,
    loginViewModel: LoginViewModel = viewModel(),
    onLogin: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 35.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginLayout(
            useHttps = loginViewModel.isHttpsEnabled,
            url = loginViewModel.url,
            token = loginViewModel.token,
            onSchemeChanged = { loginViewModel.toggleScheme(it) },
            onUrlChanged = { loginViewModel.updateUrl(it) },
            onTokenChanged = { loginViewModel.updateToken(it) }
        )
        Button(
            modifier = Modifier.padding(vertical = 10.dp),
            onClick = {
                loginViewModel.validate(context, {
                    backend.scheme = loginViewModel.scheme
                    backend.domain = loginViewModel.url
                    backend.token = loginViewModel.token

                    onLogin()
                })
            }
        ) {
            Text(stringResource(id = R.string.login))
        }
    }
}

@Composable
fun LoginLayout(
    useHttps: Boolean,
    url: String,
    token: String,
    onUrlChanged: (String) -> Unit,
    onTokenChanged: (String) -> Unit,
    onSchemeChanged: (Boolean) -> Unit
) {
    Text(
        text = stringResource(id = R.string.app_name),
        modifier = Modifier.padding(vertical = 10.dp),
        fontSize = 30.sp
    )
    OutlinedTextField(
        value = url,
        label = { Text(stringResource(id = R.string.server_url)) },
        placeholder = { Text(stringResource(id = R.string.server_url_placeholder)) },
        singleLine = true,
        onValueChange = onUrlChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { }
        ),
    )
    OutlinedTextField(
        value = token,
        label = { Text(stringResource(id = R.string.api_token)) },
        placeholder = { Text(stringResource(id = R.string.api_token_placeholder)) },
        singleLine = true,
        onValueChange = onTokenChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { }
        ),
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = useHttps,
            onCheckedChange = onSchemeChanged
        )
        Text(text = stringResource(R.string.use_https))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LinkGuardianTheme(darkTheme = true) {
        LoginPage(
            backend = LinkwardenBackend("", "", ""),
            onLogin = {}
        )
    }
}