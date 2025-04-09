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
fun SettingsPage(preferences: PreferencesManager, onLogout: () -> Unit) {
    val context = LocalContext.current
    val rowModifier = Modifier.fillMaxWidth().padding(5.dp, 6.dp)

    var enableOledTheme by remember { mutableStateOf(preferences.oledTheme) }
    var enableBookmarkPreviews by remember { mutableStateOf(preferences.showPreviews) }

    Column(modifier = Modifier.fillMaxSize().padding(10.dp, 15.dp)) {
        Row(modifier = rowModifier) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.oled_theme))
                Text(
                    text = stringResource(R.string.work_in_progress),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Switch(
                enabled = false,
                checked = enableOledTheme,
                onCheckedChange = {
                    enableOledTheme = it
                    preferences.oledTheme = it
                    preferences.persist()
                }
            )
        }
        Row(modifier = rowModifier) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.show_previews))
                Text(
                    text = stringResource(R.string.work_in_progress),
//                    text = stringResource(R.string.only_if_supported),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Switch(
                enabled = false,
                checked = enableBookmarkPreviews,
                onCheckedChange = {
                    enableBookmarkPreviews = it
                    preferences.showPreviews = it
                    preferences.persist()
                }
            )
        }
        Row(modifier = rowModifier) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.connected_to_server))
                Text(
                    text = preferences.domain,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            TextButton(
                onClick = {
                    preferences.clear()

                    ShowToast(context, context.getString(R.string.logged_out))

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
        SettingsPage(
            preferences = PreferencesManager(LocalContext.current),
            onLogout = {}
        )
    }
}