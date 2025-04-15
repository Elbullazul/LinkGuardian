package dev.elbullazul.linkguardian.backends

import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.data.generic.User
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
    val type: BackendTypes

    var hasBookmarks: Boolean
    var hasCollections: Boolean
    var hasTags: Boolean

    suspend fun getBookmarks(): List<Bookmark>
    fun getCollections(): List<Collection>
    fun getTags(): List<Tag>
    fun getBookmark(id: String): Bookmark
    fun getCollection(id: String): Collection
    fun getTag(id: String): Tag
    fun getUser(id: String): User

    fun createBookmark(link: Bookmark): Boolean

    fun isAuthorized(): Boolean
    fun isReachable(): Boolean {
        try {
            val future = ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .build()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(future)

            val result = future.get()
            future.get().close()

            return result.isSuccessful
        } catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    fun get(route: String, args: Map<String, String> = mapOf()): String {
        try {
            val future = ResponseFuture()
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
            future.get().close()

            return body
        } catch (e: Exception) {
            println(e.message)
        }

        return ""
    }

    fun post(route: String, payload: String, args: Map<String, String> = mapOf()): Boolean {
        try {
            val future = ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .addPathSegments(route)
            args.forEach { arg ->
                url.addQueryParameter(arg.key, arg.value)
            }

            val request = Request.Builder()
                .url(url.build())
                .post(payload.toRequestBody(MEDIA_TYPE_JSON))
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            val result = future.get().isSuccessful
            future.get().close()

            return result
        } catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }
}