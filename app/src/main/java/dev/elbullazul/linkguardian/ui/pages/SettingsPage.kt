package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun SettingsPage(onLogout: () -> Unit) {
    val context = LocalContext.current
    var oledEnabled by remember { mutableStateOf(true) }

    val prefs = PreferencesManager(context)
    prefs.load()

    Column(modifier = Modifier.fillMaxSize().padding(10.dp, 15.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(5.dp, 10.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.oled_theme))
                Text(
                    text = stringResource(R.string.missing_feature),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Switch(
                checked = false,
                enabled = false,
                onCheckedChange = { oledEnabled = it }
            )
        }
        Row(modifier = Modifier.fillMaxWidth().padding(5.dp, 2.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.connected_to_server))
                Text(
                    text = prefs.domain,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            TextButton(
                onClick = {
                    ShowToast(context, context.getString(R.string.logged_out))

                    val prefsManager = PreferencesManager(context)
                    prefsManager.clear()

                    onLogout()
                }
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    LinkGuardianTheme {
        SettingsPage {}
    }
}