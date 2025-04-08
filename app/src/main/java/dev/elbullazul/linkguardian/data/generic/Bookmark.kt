package dev.elbullazul.linkguardian.data.generic

interface Bookmark {
    val id: Int
    val name: String
    val url: String
    val tags: List<Tag>
}