package dev.elbullazul.linkguardian.storage

import android.content.Context
import dev.elbullazul.linkguardian.backends.BackendTypes
import dev.elbullazul.linkguardian.backends.intToEnum
import dev.elbullazul.linkguardian.backends.enumToInt

const val PREFERENCES_KEY_FILE = "com.elbullazul.linkguardian.PREFERENCES_KEY_FILE"
const val PREF_DOMAIN = "DOMAIN"
const val PREF_TOKEN = "TOKEN"
const val PREF_SCHEME = "SCHEME"
const val PREF_USERID = "USERID"
const val PREF_SHOW_PREVIEWS = "SHOW_PREVIEWS"
const val PREF_OLED_THEME = "OLED_THEME"
const val PREF_SERVER_TYPE = "SERVER_TYPE"

const val SCHEME_HTTP = "http"
const val SCHEME_HTTPS = "https"

class PreferencesManager(
    private val context: Context,
    var scheme: String = SCHEME_HTTP,
    var domain: String = "",
    var token: String = "",
    var userId: Int = -1,
    var showPreviews: Boolean = false,
    var oledTheme: Boolean = false,
    var serverType: BackendTypes = BackendTypes.None
) {
    fun load() {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        scheme = preferences.getString(PREF_SCHEME, "")!!.toString()
        domain = preferences.getString(PREF_DOMAIN, "")!!.toString()
        token = preferences.getString(PREF_TOKEN, "")!!.toString()
        userId = preferences.getInt(PREF_USERID, -1)
        showPreviews = preferences.getBoolean(PREF_SHOW_PREVIEWS, false)
        oledTheme = preferences.getBoolean(PREF_OLED_THEME, false)
        serverType = intToEnum(preferences.getInt(PREF_SERVER_TYPE, 0))
    }

    fun validCredentials(): Boolean {
        return domain.isNotEmpty() && token.isNotEmpty()
    }

    fun persist() {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString(PREF_SCHEME, scheme)
            putString(PREF_DOMAIN, domain)
            putString(PREF_TOKEN, token)
            putInt(PREF_USERID, userId)
            putBoolean(PREF_SHOW_PREVIEWS, showPreviews)
            putBoolean(PREF_OLED_THEME, oledTheme)
            putInt(PREF_SERVER_TYPE, enumToInt(serverType))

            apply()
        }
    }

    fun clear() {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        with(preferences.edit()) {
            remove(PREF_SCHEME)
            remove(PREF_DOMAIN)
            remove(PREF_TOKEN)
            remove(PREF_USERID)
            remove(PREF_SHOW_PREVIEWS)
            remove(PREF_OLED_THEME)
            remove(PREF_SERVER_TYPE)

            commit()
        }
    }
}