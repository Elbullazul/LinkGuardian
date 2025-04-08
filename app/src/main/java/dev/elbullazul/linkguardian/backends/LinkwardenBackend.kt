package dev.elbullazul.linkguardian.backends

import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.data.generic.User
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenCollectionResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenCollectionsResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenLinkResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenLinksResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenTagsResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenUserResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenUsersResponse
import dev.elbullazul.linkguardian.futures.ResponseFuture
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class LinkwardenBackend(
    override var scheme: String,
    override var domain: String,
    override var token: String
) : Backend {
    override val client: OkHttpClient = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    override val type = SupportedBackends.Linkwarden

    // store last link ID for cursor pagination (https://www.prisma.io/docs/orm/prisma-client/queries/pagination#cursor-based-pagination)
    // collection and tag endpoints return all objects without paginating
    private var linkCursor = 0
    override var hasBookmarks = true
    override var hasCollections = false
    override var hasTags = false

    // API routes
    private val ROUTE_USERS = "api/v1/users"
    private val ROUTE_TAGS = "api/v1/tags"
    private val ROUTE_COLLECTIONS = "api/v1/collections"
    private val ROUTE_LINKS = "api/v1/links"
    private val ROUTE_DASHBOARD = "api/v2/dashboard"
    private val ROUTE_AVATAR = "api/v1/avatar"

    override suspend fun getBookmarks(): List<Bookmark> {
        val data = get(ROUTE_LINKS, mapOf(Pair("cursor", "$linkCursor"), Pair("sort", "0")))
        val response = json.decodeFromString<LinkwardenLinksResponse>(data)
        val links = response.links

        if (links.isEmpty())
            hasBookmarks = false
        else
            linkCursor = links.last().id

        return response.links
    }

    override fun getCollections(): List<Collection> {
        val data = get(ROUTE_COLLECTIONS)
        val response = json.decodeFromString<LinkwardenCollectionsResponse>(data)

        return response.collections
    }

    override fun getTags(): List<Tag> {
        val data = get(ROUTE_TAGS)
        val response = json.decodeFromString<LinkwardenTagsResponse>(data)

        return response.tags
    }

    override fun getBookmark(id: Int): Bookmark {
        val data = get("$ROUTE_LINKS/$id")
        val response = json.decodeFromString<LinkwardenLinkResponse>(data)

        return response.link
    }

    override fun getCollection(id: Int): Collection {
        val data = get("$ROUTE_COLLECTIONS/$id")
        val response = json.decodeFromString<LinkwardenCollectionResponse>(data)

        return response.collection
    }

    override fun getTag(id: Int): Tag {
        val data = get("$ROUTE_TAGS/$id")

        TODO("No API route for specific tag exists")
    }

    override fun getUser(id: Int): User {
        val data = get("$ROUTE_USERS/$id")
        val response = json.decodeFromString<LinkwardenUserResponse>(data)

        return response.user
    }

    // unnecessary?
    fun getUsers(): List<User> {
        val data = get(ROUTE_USERS)
        val response = json.decodeFromString<LinkwardenUsersResponse>(data)

        return response.users
    }

    // unnecessary?
    fun getDashboardData() {
        val data = get(ROUTE_DASHBOARD)
        println(data)
    }

    override fun createBookmark(link: Bookmark): Boolean {
        val payload = json.encodeToString<LinkwardenLink>(link as LinkwardenLink)

        return post(ROUTE_LINKS, payload)
    }

    override fun isAuthorized(): Boolean {
        try {
            val future =
                ResponseFuture()
            val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(domain)
                .addPathSegments(ROUTE_LINKS)

            val request = Request.Builder()
                .url(url.build())
                .header("Authorization", "Bearer $token")
                .build()

            client.newCall(request).enqueue(future)

            return future.get().isSuccessful
        }
        catch (e: Exception) {
            println(e.message)
        }

        return false
    }
}