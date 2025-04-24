package dev.elbullazul.linkguardian.storage

import android.content.Context
import android.content.SharedPreferences
import dev.elbullazul.linkguardian.backends.BackendTypes
import dev.elbullazul.linkguardian.backends.EnumToInt
import dev.elbullazul.linkguardian.backends.IntToEnum

const val SCHEME_HTTP = "http"
const val SCHEME_HTTPS = "https"

class PreferencesManager(
    private val context: Context
) {
    private fun preferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
    }

    fun getScheme(): String {
        return preferences(context).getString(PREF_SCHEME, SCHEME_HTTPS).toString()
    }

    fun getDomain(): String {
        return preferences(context).getString(PREF_DOMAIN, "").toString()
    }

    fun getToken(): String {
        return preferences(context).getString(PREF_TOKEN, "").toString()
    }

    fun getBackendType(): BackendTypes {
        return IntToEnum(preferences(context).getInt(PREF_SERVER_TYPE, 0))
    }

    fun setBackend(scheme: String, domain: String, token: String, type: BackendTypes) {
        with(preferences(context).edit()) {
            putString(PREF_SCHEME, scheme)
            putString(PREF_DOMAIN, domain)
            putString(PREF_TOKEN, token)
            putInt(PREF_SERVER_TYPE, EnumToInt(type))

            apply()
        }
    }

    fun getShowPreviews(): Boolean {
        return preferences(context).getBoolean(PREF_SHOW_PREVIEWS, false)
    }

    fun setShowPreviews(enabled: Boolean) {
        with(preferences(context).edit()) {
            putBoolean(PREF_SHOW_PREVIEWS, enabled)
            apply()
        }
    }

    fun getHighContrastTheme(): Boolean {
        return preferences(context).getBoolean(PREF_HIGH_CONTRAST_THEME, false)
    }

    fun setHighContrastTheme(enabled: Boolean) {
        with(preferences(context).edit()) {
            putBoolean(PREF_HIGH_CONTRAST_THEME, enabled)
            apply()
        }
    }

    fun clear() {
        with(preferences(context).edit()) {
            remove(PREF_SCHEME)
            remove(PREF_DOMAIN)
            remove(PREF_TOKEN)
            remove(PREF_SHOW_PREVIEWS)
            remove(PREF_HIGH_CONTRAST_THEME)
            remove(PREF_SERVER_TYPE)

            commit()
        }
    }

    fun validCredentials(): Boolean {
        with(preferences(context)) {
            return !getString(PREF_DOMAIN, "").isNullOrBlank() &&
                    !getString(PREF_TOKEN, "").isNullOrBlank()
        }
    }

    companion object {
        const val PREFERENCES_KEY_FILE = "com.elbullazul.linkguardian.PREFERENCES_KEY_FILE"

        private const val PREF_DOMAIN = "DOMAIN"
        private const val PREF_TOKEN = "TOKEN"
        private const val PREF_SCHEME = "SCHEME"
        private const val PREF_SHOW_PREVIEWS = "SHOW_PREVIEWS"
        private const val PREF_HIGH_CONTRAST_THEME = "OLED_THEME"
        private const val PREF_SERVER_TYPE = "SERVER_TYPE"
    }
}