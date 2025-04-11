package dev.elbullazul.linkguardian.data

import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.BackendTypes
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag

class DataFactory(
    val backendType: BackendTypes
) {
    // only linkwarden types for now
    fun backend(scheme: String, domain: String, secret: String): Backend {
        return LinkwardenBackend(scheme, domain, secret);
    }

    fun bookmark(url: String, name: String, description: String = "", tags: List<String>, collection: Collection?): Bookmark {
        return LinkwardenLink(
            id = -1,
            name = name,
            url = url,
            description = description,
            tags = tags(tags) as List<LinkwardenTag>,
            collection = collection as LinkwardenCollection?
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