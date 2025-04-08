package dev.elbullazul.linkguardian.data.generic

interface Collection {
    val id: Int
    val name: String
    val links: List<Bookmark>

    fun bookmarkCount(): Int
}