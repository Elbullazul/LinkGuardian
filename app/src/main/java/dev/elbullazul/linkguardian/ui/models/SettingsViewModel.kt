package dev.elbullazul.linkguardian.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.storage.PreferencesManager

class SettingsViewModel: ViewModel() {
    var initialized by mutableStateOf(false)
        private set
    var isHighContrastThemeEnabled by mutableStateOf(false)
        private set
    var arePreviewImagesEnabled by mutableStateOf(false)
        private set

    fun loadValues(context: Context) {
        val preferencesManager = PreferencesManager(context)

        isHighContrastThemeEnabled = preferencesManager.oledTheme
        arePreviewImagesEnabled = preferencesManager.showPreviews

        initialized = true
    }

    fun updateHighContrastSetting(isEnabled: Boolean) {
        isHighContrastThemeEnabled = isEnabled
    }

    fun updatePreviewImagesSetting(isEnabled: Boolean) {
        arePreviewImagesEnabled = isEnabled
    }

    fun logout(context: Context) {
        val preferencesManager = PreferencesManager(context)
        preferencesManager.clear()

        ShowToast(context, context.getString(R.string.logged_out))
    }

    fun save(context: Context) {
        val preferencesManager = PreferencesManager(context)

        preferencesManager.oledTheme = isHighContrastThemeEnabled
        preferencesManager.showPreviews = arePreviewImagesEnabled
        preferencesManager.persist()
    }
}