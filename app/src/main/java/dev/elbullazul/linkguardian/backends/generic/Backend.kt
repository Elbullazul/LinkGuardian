package dev.elbullazul.linkguardian.backends.generic

import dev.elbullazul.linkguardian.backends.SupportedBackends
import dev.elbullazul.linkguardian.futures.ResponseFuture
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

interface Backend {
    var scheme: String
    var domain: String
    var token: String
    val client: OkHttpClient
    val type: SupportedBackends

    var hasBookmarks: Boolean
    var hasCollections: Boolean
    var hasTags: Boolean

    suspend fun getBookmarks(): List<Bookmark>
    fun getCollections(): List<Collection>
    fun getTags(): List<Tag>

    fun getBookmark(id: Int): Bookmark
    fun getCollection(id: Int): Collection
    fun getTag(id: Int): Tag
    fun getUser(id: Int): User

    fun createBookmark(link: Bookmark): Boolean

    fun isAuthorized(): Boolean
    fun isReachable(): Boolean {
        try {
            val future =
                ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .build()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(future)

            val result = future.get()
            return result.isSuccessful
        }
        catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    fun get(route: String, args: Map<String, String> = mapOf()): String {
        try {
            val future =
                ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .addPathSegments(route)
            args.forEach { arg ->
                url.addQueryParameter(arg.key, arg.value)
            }

            val request = Request.Builder()
                .url(url.build())
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            val body = future.get().body!!.string()
            return body
        }
        catch (e: Exception) {
            println(e.message)
        }

        return ""
    }

    fun post(route: String, payload: String, args: Map<String, String> = mapOf()): Boolean {
        try {
            val future =
                ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .addPathSegments(route)
            args.forEach { arg ->
                url.addQueryParameter(arg.key, arg.value)
            }

            val request = Request.Builder()
                .url(url.build())
                .post(
                    payload.toRequestBody(MEDIA_TYPE_JSON)
                )
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            val response = future.get()
            return response.isSuccessful
        }
        catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    fun buildUrl(resource: String): String {
        return "$scheme://$domain/$resource"
    }

    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }
}