package dev.elbullazul.linkguardian.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.backends.BackendTypes
import dev.elbullazul.linkguardian.data.DataFactory
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.storage.SCHEME_HTTP
import dev.elbullazul.linkguardian.storage.SCHEME_HTTPS

class LoginViewModel : ViewModel() {
    var scheme by mutableStateOf("https")
        private set
    var url by mutableStateOf("")
        private set
    var token by mutableStateOf("")
        private set
    var isHttpsEnabled by mutableStateOf(true)
        private set

    fun updateUrl(url: String) {
        this.url = url
    }

    fun updateToken(token: String) {
        this.token = token
    }

    fun toggleScheme(useHttps: Boolean) {
        isHttpsEnabled = useHttps

        scheme = if (useHttps)
            SCHEME_HTTPS
        else
            SCHEME_HTTP
    }

    fun validate(context: Context, onSuccess: () -> Unit) {
        val preferencesManager = PreferencesManager(context)

        if (url.isEmpty() || token.isEmpty()) {
            ShowToast(context, context.getString(R.string.missing_information))

            return
        }

        val backend = DataFactory(preferencesManager.serverType).backend(scheme, url, token)

        if (!backend.isReachable()) {
            ShowToast(context, context.getString(R.string.domain_unreachable))

            return
        }

        if (!backend.isAuthorized()) {
            ShowToast(context, context.getString(R.string.access_denied))

            return
        }

        saveCredentials(preferencesManager)
        onSuccess()
    }

    private fun saveCredentials(preferencesManager: PreferencesManager) {
        preferencesManager.scheme = scheme
        preferencesManager.serverType = BackendTypes.Linkwarden
        preferencesManager.domain = url
        preferencesManager.token = token

        preferencesManager.persist()
    }
}