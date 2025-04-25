package dev.elbullazul.linkguardian.backends

import coil3.network.NetworkHeaders
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
import okhttp3.Response

interface Backend {
    var scheme: String
    var domain: String
    var token: String
    val client: OkHttpClient
    val type: BackendTypes

    var hasBookmarks: Boolean
    var hasCollections: Boolean
    var hasTags: Boolean

    suspend fun getBookmarks(collectionId: String? = null, tagId: String? = null): List<Bookmark>
    fun getCollections(): List<Collection>
    fun getTags(): List<Tag>

    fun getBookmark(id: String): Bookmark
    fun getCollection(id: String): Collection
    fun getTag(id: String): Tag
    fun getUser(id: String): User

    fun createBookmark(bookmark: Bookmark): Boolean
    fun updateBookmark(bookmark: Bookmark): Boolean
    fun deleteBookmark(bookmark: Bookmark): Boolean

    fun createCollection(collection: Collection): Boolean
    fun updateCollection(collection: Collection): Boolean
    fun deleteCollection(collection: Collection): Boolean

    fun reset()     // reset internal state

    fun buildUrl(route: String = "", args: Map<String, String> = mapOf()): HttpUrl {
        val url = HttpUrl.Builder()
            .scheme(scheme)
            .host(domain)
            .addPathSegments(route)
        args.forEach { arg ->
            url.addQueryParameter(arg.key, arg.value)
        }

        return url.build()
    }

    fun enqueue(request: Request): Response {
        val future = ResponseFuture()
        client.newCall(request).enqueue(future)

        return future.get()
    }

    fun isAuthorized(): Boolean
    fun isReachable(): Boolean {
        try {
            val request = Request.Builder()
                .url(buildUrl())
                .build()

            val result = enqueue(request)

            return result.isSuccessful
        } catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    fun get(route: String, args: Map<String, String> = mapOf()): String {
        try {
            val request = Request.Builder()
                .url(buildUrl(route, args))
                .header("Authorization", "Bearer $token")
                .build()

            val body = enqueue(request).body!!.string()

            return body
        } catch (e: Exception) {
            println(e.message)
        }

        return ""
    }

    fun post(route: String, payload: String, args: Map<String, String> = mapOf()): Boolean {
        try {
            val request = Request.Builder()
                .url(buildUrl(route, args))
                .post(payload.toRequestBody(MEDIA_TYPE_JSON))
                .header("Authorization", "Bearer $token")
                .build()

            val result = enqueue(request)

            return result.isSuccessful
        } catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    fun put(route: String, payload: String, args: Map<String, String> = mapOf()): Boolean {
        try {
            val request = Request.Builder()
                .url(buildUrl(route, args))
                .put(payload.toRequestBody(MEDIA_TYPE_JSON))
                .header("Authorization", "Bearer $token")
                .build()

            val result = enqueue(request)

            return result.isSuccessful
        } catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    fun delete(route: String, args: Map<String, String> = mapOf()): Boolean {
        try {
            val request = Request.Builder()
                .url(buildUrl(route, args))
                .delete()
                .header("Authorization", "Bearer $token")
                .build()

            val result = enqueue(request)

            return result.isSuccessful
        } catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    fun getHeaders(): NetworkHeaders {
        return NetworkHeaders.Builder()
            .set("Authorization", "Bearer $token")
            .build()
    }

    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }
}