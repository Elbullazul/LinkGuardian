package dev.elbullazul.linkguardian.backends.generic

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.elbullazul.linkguardian.R

interface Bookmark {
    val id: Int
    val name: String
    val description: String
    val url: String
    val tags: List<Tag>

    @Composable
    fun shortDescription(): String {
        if (description.isEmpty())
            return stringResource(R.string.no_description)

        if (description.length > 150)
            return description.substring(0,150) + "..."

        return description
    }
}