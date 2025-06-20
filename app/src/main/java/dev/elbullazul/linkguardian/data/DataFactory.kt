package dev.elbullazul.linkguardian.data

import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.BackendTypes
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.generic.Data
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenMember
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag

class DataFactory(
    val backendType: BackendTypes
) {
    // only linkwarden types for now
    fun backend(scheme: String, domain: String, secret: String): Backend {
        return LinkwardenBackend(scheme, domain, secret)
    }

    fun bookmark(id: String, url: String?, tags: List<Tag>, name: String, description: String? = "", collection: Collection? = null): Bookmark {
        return LinkwardenLink(
            id = id.toInt(),
            url = url,
            tags = tags as List<LinkwardenTag>,
            name = name,
            description = description.toString(),
            collection = if (collection != null) collection as LinkwardenCollection else null
        )
    }

    fun collection(id: String, name: String, description: String? = "", ownershipData: List<Data> = listOf()): Collection? {
        if (id == "-1")
            return null

        return LinkwardenCollection(
            id = id.toInt(),
            name = name,
            description = description.toString(),
            ownershipData = ownershipData as List<LinkwardenMember>
        )
    }

    fun tags(textTags: List<String>): List<Tag> {
        val linkTags = arrayListOf<LinkwardenTag>()

        for (tag in textTags) {
            if (tag.isBlank())
                continue

            linkTags.add(
                LinkwardenTag(
                    id = -1,
                    name = tag
                )
            )
        }

        return linkTags
    }
}