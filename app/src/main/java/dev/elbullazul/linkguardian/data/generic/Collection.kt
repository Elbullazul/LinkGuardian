package dev.elbullazul.linkguardian.data.generic

interface Collection {
    val name: String
    val links: List<Bookmark>

    fun getId(): String
}