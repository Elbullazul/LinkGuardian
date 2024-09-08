package dev.elbullazul.linkguardian.api

import android.content.Context
import dev.elbullazul.linkguardian.storage.PreferencesManager
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

const val SUCCESS = 0
const val INVALID_DOMAIN = 1
const val DOMAIN_UNREACHABLE = 2
const val TOKEN_INVALID = 3

class LinkwardenAPI(
    private var domain: String,
    private var token: String,
    private var scheme: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    fun loadFromPreferences(context: Context) {
        val prefs = PreferencesManager(context)

        domain = prefs.domain
        token = prefs.token
        scheme = prefs.scheme
    }

    fun connect(): Int {
        try {
            val future = ResponseFuture()
//            val url = HttpUrl.Builder()
//                .scheme(scheme)
//                .host(domain)
//                .build()

            val request = Request.Builder()
                .url("$scheme://$domain")
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            if (!future.get().isSuccessful)
                return TOKEN_INVALID
        }
        catch (e: IllegalArgumentException) {
            return INVALID_DOMAIN
        }
        catch (e: Exception) {
            return DOMAIN_UNREACHABLE
        }

        return SUCCESS
    }
}