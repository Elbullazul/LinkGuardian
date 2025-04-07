package dev.elbullazul.linkguardian.storage

import android.content.Context

const val PREFERENCES_KEY_FILE = "com.elbullazul.linkguardian.PREFERENCES_KEY_FILE"
const val PREF_DOMAIN = "DOMAIN"
const val PREF_TOKEN = "TOKEN"
const val PREF_SCHEME = "SCHEME"
const val PREF_USERID = "USERID"
const val PREF_SHOW_PREVIEWS = "SHOW_PREVIEWS"

const val SCHEME_HTTP = "http"
const val SCHEME_HTTPS = "https"

class PreferencesManager(
    private val context: Context,
    var scheme: String = SCHEME_HTTP,
    var domain: String = "",
    var token: String = "",
    var userId: Int = -1,
    var showPreviews: Boolean = false
) {
    fun load(): Boolean {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        scheme = preferences.getString(PREF_SCHEME, "")!!.toString()
        domain = preferences.getString(PREF_DOMAIN, "")!!.toString()
        token = preferences.getString(PREF_TOKEN, "")!!.toString()
        userId = preferences.getInt(PREF_USERID, -1)
        showPreviews = preferences.getBoolean(PREF_SHOW_PREVIEWS, false)

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
            commit()
        }
    }
}