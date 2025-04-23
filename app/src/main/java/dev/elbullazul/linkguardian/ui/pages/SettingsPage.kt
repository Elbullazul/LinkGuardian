package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.models.SettingsViewModel
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun SettingsPage(
    backend: Backend,
    settingsViewModel: SettingsViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    if (!settingsViewModel.initialized) {
        settingsViewModel.loadValues(context)
    }

    fun logout() {
        settingsViewModel.logout(context)
        onLogout()
    }

    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 6.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 15.dp)
    ) {
        SettingsLayout(
            modifier = rowModifier,
            previewsAvailable = false,      // backend is PreviewProvider
            isHighContrastThemeEnabled = settingsViewModel.isHighContrastThemeEnabled,
            arePreviewImagesEnabled = settingsViewModel.arePreviewImagesEnabled,
            updateHighContrastTheme = {
                settingsViewModel.updateHighContrastSetting(it)
                settingsViewModel.save(context)
            },
            updatePreviewImages = {
                settingsViewModel.updatePreviewImagesSetting(it)
                settingsViewModel.save(context)
            }
        )
        Row(modifier = rowModifier) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.connected_to_server))
                Text(
                    text = PreferencesManager(context).domain,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            TextButton(onClick = { logout() }) {
                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        Row(
            modifier = rowModifier.weight(1.0f),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        R.string.about,
                        stringResource(R.string.app_name),
                        versionCode(),
                        "Elbullazul",
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = AnnotatedString(stringResource(R.string.source_code)),
                    modifier = Modifier
                        .clickable { uriHandler.openUri("https://github.com/Elbullazul/LinkGuardian") }
                )
            }
        }
    }
}

@Composable
fun SettingsLayout(
    modifier: Modifier,
    previewsAvailable: Boolean,
    isHighContrastThemeEnabled: Boolean,
    arePreviewImagesEnabled: Boolean,
    updateHighContrastTheme: (Boolean) -> Unit,
    updatePreviewImages: (Boolean) -> Unit,
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = stringResource(R.string.oled_theme))
            Text(
                text = stringResource(R.string.restart_required),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Switch(
            enabled = false,    // TODO: enable when ready
            checked = isHighContrastThemeEnabled,
            onCheckedChange = updateHighContrastTheme
        )
    }
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = stringResource(R.string.show_previews))
            Text(
                text = stringResource(R.string.only_if_supported),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Switch(
            enabled = previewsAvailable,
            checked = arePreviewImagesEnabled,
            onCheckedChange = updatePreviewImages
        )
    }
}

@Composable
fun versionCode(): String {
    val context = LocalContext.current

    // show placeholder in preview
    if (context.packageManager.getPackageInfo(context.packageName, 0) == null)
        return "vX.X.X"

    return context.packageManager.getPackageInfo(context.packageName, 0).versionName.toString()
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    LinkGuardianTheme {
        SettingsPage(
            backend = LinkwardenBackend("", "", ""),
            onLogout = {}
        )
    }
}