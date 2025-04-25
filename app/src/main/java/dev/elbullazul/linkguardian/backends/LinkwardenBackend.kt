package dev.elbullazul.linkguardian.backends

import dev.elbullazul.linkguardian.backends.features.PreviewProvider
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.data.generic.User
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenCollectionResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenCollectionsResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenLinkResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenLinksResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenTagsResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenUserResponse
import dev.elbullazul.linkguardian.data.linkwarden.responses.LinkwardenUsersResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class LinkwardenBackend(
    override var scheme: String,
    override var domain: String,
    override var token: String
) : Backend, PreviewProvider {
    override val client: OkHttpClient = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    override val type = BackendTypes.Linkwarden

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
    private val ROUTE_ARCHIVES = "api/v1/archives"

    override suspend fun getBookmarks(collectionId: String?, tagId: String?): List<Bookmark> {
        val args = mutableMapOf(Pair("cursor", "$linkCursor"), Pair("sort", "0"))

        if (!collectionId.isNullOrBlank())
            args["collectionId"] = collectionId
        if (!tagId.isNullOrBlank())
            args["tagId"] = tagId

        val data = get(ROUTE_LINKS, args)
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

    override fun getBookmark(id: String): Bookmark {
        val data = get("$ROUTE_LINKS/$id")
        val response = json.decodeFromString<LinkwardenLinkResponse>(data)

        return response.link
    }

    override fun getCollection(id: String): Collection {
        val data = get("$ROUTE_COLLECTIONS/$id")
        val response = json.decodeFromString<LinkwardenCollectionResponse>(data)

        return response.collection
    }

    override fun getTag(id: String): Tag {
        val data = get(ROUTE_TAGS)
        val response = json.decodeFromString<LinkwardenTagsResponse>(data)

        var tag = response.tags.find { tag ->
            tag.getId() == id
        }

        if (tag == null)
            tag = LinkwardenTag(-1, "")

        return tag
    }

    override fun getUser(id: String): User {
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
    }

    override fun createBookmark(bookmark: Bookmark): Boolean {
        val payload = json.encodeToString<LinkwardenLink>(bookmark as LinkwardenLink)

        return post(ROUTE_LINKS, payload)
    }

    override fun updateBookmark(bookmark: Bookmark): Boolean {
        val payload = json.encodeToString<LinkwardenLink>(bookmark as LinkwardenLink)

        return put("$ROUTE_LINKS/${bookmark.id}", payload)
    }

    override fun deleteBookmark(bookmark: Bookmark): Boolean {
        return delete("$ROUTE_LINKS/${bookmark.getId()}")
    }

    override fun createCollection(collection: Collection): Boolean {
        val payload = json.encodeToString<LinkwardenCollection>(collection as LinkwardenCollection)

        println(payload)

        return post(ROUTE_COLLECTIONS, payload)
    }

    override fun updateCollection(collection: Collection): Boolean {
        val payload = json.encodeToString<LinkwardenCollection>(collection as LinkwardenCollection)

        return put("$ROUTE_COLLECTIONS/${collection.id}", payload)
    }

    override fun deleteCollection(collection: Collection): Boolean {
        return delete("$ROUTE_COLLECTIONS/${collection.getId()}")
    }

    override fun isAuthorized(): Boolean {
        try {
            val request = Request.Builder()
                .url(buildUrl(ROUTE_LINKS))
                .header("Authorization", "Bearer $token")
                .build()

            val result = enqueue(request)

            return result.isSuccessful
        }
        catch (e: Exception) {
            println(e.message)
        }

        return false
    }

    override fun getPreviewUrl(bookmark: Bookmark): String {
        return buildUrl("$ROUTE_ARCHIVES/${bookmark.getId()}", mapOf(Pair("format", "1"))).toString()
    }

    override fun reset() {
        hasBookmarks = true
        linkCursor = 0
    }
}