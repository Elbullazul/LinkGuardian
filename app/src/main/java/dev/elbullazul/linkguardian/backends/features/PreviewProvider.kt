package dev.elbullazul.linkguardian.backends.features

import dev.elbullazul.linkguardian.data.generic.Bookmark
import okhttp3.HttpUrl

interface PreviewProvider {
    fun getPreviewUrl(bookmark: Bookmark): String
}