package dev.elbullazul.linkguardian.api

import android.content.Context
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.objects.ArrayResponse
import dev.elbullazul.linkguardian.api.objects.Link
import dev.elbullazul.linkguardian.api.objects.PostCollection
import dev.elbullazul.linkguardian.api.objects.PostLink
import dev.elbullazul.linkguardian.api.objects.PostTag
import dev.elbullazul.linkguardian.storage.PreferencesManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

// TODO: if no arguments are received, loadFromPreferences should be called?
class LinkwardenAPI(
    private var context: Context,
    private var domain: String = "",
    private var token: String = "",
    private var scheme: String = "",
    private val client: OkHttpClient = OkHttpClient()
) {
    init {
        if (domain.isEmpty() && token.isEmpty() && scheme.isEmpty())
            loadFromPreferences()
    }

    private fun loadFromPreferences(): Boolean {
        val prefs = PreferencesManager(context)

        if (!prefs.load())
            return false;

        domain = prefs.domain
        token = prefs.token
        scheme = prefs.scheme

        return true
    }

    fun connect(): Int {
        val future = ResponseFuture()

        try {
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .build()
            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            future.get().close()

            if (!future.get().isSuccessful) {
                return TOKEN_INVALID
            }
        }
        catch (e: IllegalArgumentException) {
            return INVALID_DOMAIN
        }
        catch (e: Exception) {
            println(e.message)
            return DOMAIN_UNREACHABLE
        }

        return SUCCESS
    }

    fun getDashboard(): ArrayResponse {
        try {
            val future = ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .addPathSegments(context.getString(R.string.api_v2_dashboard))
                .build()
            val request = Request.Builder()
                .url(url)
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

    fun getLinks(cursor: Int = 0): ArrayResponse {
        try {
            val future = ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .addPathSegments(context.getString(R.string.api_v1_links))
                .addQueryParameter("cursor", cursor.toString())
                .build()
            val request = Request.Builder()
                .url(url)
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

    fun postLink(submitUrl: String, name: String = "", collection: String = "", linkTags: Array<String> = arrayOf()): Boolean {
        try {
            val future = ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .addPathSegments(context.getString(R.string.api_v1_links))
                .build()

            var tags: Array<PostTag> = arrayOf()
            for (tag in linkTags) tags += PostTag(tag)

            val payload = PostLink(
                name = name,
                collection = PostCollection(collection, 1 /*TODO: use correct user Id*/),
                tags = tags,
                url = submitUrl
            )

            val json = Json.encodeToString<PostLink>(payload)
            println(json)

            val request = Request.Builder()
                .url(url)
                .post(
                    Json.encodeToString<PostLink>(payload).toRequestBody(MEDIA_TYPE_JSON)
                )
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            // TODO: process response?
//            val response = future.get().body!!.string()

            return true;
        }
        catch (e: Exception) {
            println(e.message)
        }

        return false;
    }

    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }
}