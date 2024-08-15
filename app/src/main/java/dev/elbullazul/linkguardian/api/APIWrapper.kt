package dev.elbullazul.linkguardian.api

import android.content.Context
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.objects.DashboardResponse
import dev.elbullazul.linkguardian.api.objects.Link
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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

    fun dashboardData(): Response {
        try {
            val testUrl = context.getString(R.string.api_v1_dashboard, url)
            println(testUrl)

            val future = ResponseFuture()
            val request = Request.Builder()
                .url(testUrl)
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            val response = future.get()
            // TODO: move to dashboard fragment when it's ready
//            val json = response.body!!.string();
//
//            var dash = Json.decodeFromString<DashboardResponse>(json)
//            println("First URL is ${dash.response[0].url}")

            return response
        }
        catch (e: Exception) {
            println(e.message)
            return Response.Builder().build()
        }
    }
}