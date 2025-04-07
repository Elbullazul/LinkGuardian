package dev.elbullazul.linkguardian.backends.generic

interface Bookmark {
    val id: Int
    val name: String
    val description: String
    val url: String
    val tags: List<Tag>

    fun shortDescription(): String {
        if (description.length > 150)
            return description.substring(0,150) + "..."

        return description
    }
}