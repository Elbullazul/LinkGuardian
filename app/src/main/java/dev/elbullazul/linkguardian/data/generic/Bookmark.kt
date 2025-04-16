package dev.elbullazul.linkguardian.data.generic

interface Bookmark {
    val name: String
    val url: String
    val tags: List<Tag>

    fun getId(): String
    fun tagsToString(): String
}