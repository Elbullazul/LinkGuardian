package dev.elbullazul.linkguardian.api

import android.content.Context
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.objects.ArrayResponse
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class APIWrapper(
    private val context: Context,
    private val url: String,
    private val token: String,
    private val client: OkHttpClient = OkHttpClient()
) {
    fun serverReachable(): Boolean {
        val future = ResponseFuture()

        try {
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(future)

            return future.get().isSuccessful
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return false;
        } catch (e: IOException) {
            println("IO error: no internet?")
            return false;
        } catch (e: Exception) {
            println(e.message)
            return false;
        }
    }

    fun dashboardData(): ArrayResponse {
        try {
            val future = ResponseFuture()
            val request = Request.Builder()
                .url(context.getString(R.string.api_v1_dashboard, url))
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            val json = future.get().body!!.string()
            val data = Json.decodeFromString<ArrayResponse>(json)

            return data
        }
        catch (e: Exception) {
            println(e.message)
            return ArrayResponse()
        }
    }

    fun linkData(cursor: Int = 0): ArrayResponse {
        try {
            // TODO: eventually migrate to HttpUrl.Builder
            val future = ResponseFuture()
            val request = Request.Builder()
                .url(context.getString(R.string.api_v1_links, url) + "?cursor=$cursor")
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            val json = future.get().body!!.string()
            val data = Json.decodeFromString<ArrayResponse>(json)

            return data
        }
        catch (e: Exception) {
            println(e.message)
            return ArrayResponse()
        }
    }
}