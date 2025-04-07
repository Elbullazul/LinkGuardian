package dev.elbullazul.linkguardian.storage

import android.content.Context

const val PREFERENCES_KEY_FILE = "com.elbullazul.linkguardian.PREFERENCES_KEY_FILE"
const val PREF_DOMAIN = "DOMAIN"
const val PREF_TOKEN = "TOKEN"
const val PREF_SCHEME = "SCHEME"
const val PREF_LINKWARDEN_USERID = "USERID"

const val SCHEME_HTTP = "http"
const val SCHEME_HTTPS = "https"

class PreferencesManager(
    private val context: Context,
    var scheme: String = SCHEME_HTTP,
    var domain: String = "",
    var token: String = "",
    var userId: Int = -1
) {
    fun load(): Boolean {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        scheme = preferences.getString(PREF_SCHEME, "")!!.toString()
        domain = preferences.getString(PREF_DOMAIN, "")!!.toString()
        token = preferences.getString(PREF_TOKEN, "")!!.toString()
        userId = preferences.getInt(PREF_LINKWARDEN_USERID, -1)

        return domain.isNotEmpty() && token.isNotEmpty()
    }

    fun persist() {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString(PREF_SCHEME, scheme)
            putString(PREF_DOMAIN, domain)
            putString(PREF_TOKEN, token)
            putInt(PREF_LINKWARDEN_USERID, userId)
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
            remove(PREF_LINKWARDEN_USERID)
            commit()
        }
    }
}