package dev.elbullazul.linkguardian.api.objects

import kotlinx.serialization.Serializable

@Serializable
class Link(
    var id: Int,
    var name: String,
    var type: String,
    var description: String,
    var collectionId: Int,
    var url: String,
    var textContent: String? = null,
    var preview: String,
    var image: String = "unavailable",
    var pdf: String = "unavailable",
    var readable: String = "unavailable",
    var monolith: String,
    var lastPreserved: String,  // should be date, but crashes date parser
    var importDate: String?,    // should be date, but crashes date parser
    var createdAt: String,      // should be date, but crashes date parser
    var updatedAt: String,      // should be date, but crashes date parser
    var tags: Array<Tag>,
    var collection: Collection,
    var pinnedBy: Array<Id>
)