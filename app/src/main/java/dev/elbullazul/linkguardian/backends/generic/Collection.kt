package dev.elbullazul.linkguardian.backends.generic

interface Collection {
    val id: Int
    val name: String
    val links: List<Bookmark>

    fun bookmarkCount(): Int
}