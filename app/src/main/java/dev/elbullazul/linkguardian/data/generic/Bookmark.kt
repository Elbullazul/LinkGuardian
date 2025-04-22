package dev.elbullazul.linkguardian.data.generic

interface Bookmark {
    val name: String
    val url: String
    val tags: List<Tag>

    fun getId(): String
    fun tagsToString(): String

    fun shortName(): String {
        if (name.length > SHORT_LENGTH)
            return name.substring(0, SHORT_LENGTH) + "..."

        return name
    }

    companion object {
        val SHORT_LENGTH = 70
    }
}