package dev.elbullazul.linkguardian.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.data.DataFactory
import dev.elbullazul.linkguardian.data.extensions.Collectionable
import dev.elbullazul.linkguardian.data.extensions.Describable
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.storage.PreferencesManager

class BookmarkEditorViewModel : ViewModel() {
    var id by mutableStateOf("-1")
        private set
    var url by mutableStateOf("")
        private set
    var tags by mutableStateOf("")
        private set
    var name by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var collectionId by mutableStateOf("-1")
        private set

    var initialized by mutableStateOf(false)
        private set
    var isCollectionPickerVisible by mutableStateOf(false)
        private set
    private var update by mutableStateOf(false)

    fun load(bookmarkId: String?, bookmarkUrl: String?, context: Context) {
        if (!bookmarkUrl.isNullOrBlank())
            url = bookmarkUrl
        else if (!bookmarkId.isNullOrBlank())
            fromBookmark(bookmarkId, context)

        initialized = true
    }

    private fun fromBookmark(bookmarkId: String, context: Context) {
        val backend = backend(context)
        val bookmark = backend.getBookmark(bookmarkId)

        id = bookmark.getId()
        url = bookmark.url
        tags = bookmark.tagsToString()
        name = bookmark.name
        description = if (bookmark is Describable) bookmark.description.toString() else ""
        collectionId = if (bookmark is Collectionable) bookmark.getCollectionId() else "-1"

        update = true
    }

    fun updateUrl(url: String) {
        this.url = url
    }

    fun updateTags(tags: String) {
        this.tags = tags
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updateCollectionId(collectionId: String) {
        this.collectionId = collectionId
    }

    fun showCollectionPicker() {
        isCollectionPickerVisible = true
    }

    fun hideCollectionPicker() {
        isCollectionPickerVisible = false
    }

    fun submit(context: Context, onSuccess: () -> Unit) {
        val backend = backend(context)
        val bookmark = toBookmark(backend, context)

        if (update && !backend.updateBookmark(bookmark)) {
            ShowToast(context, context.getString(R.string.submission_failed))

            return
        } else if (!update && !backend.createBookmark(bookmark)) {
            ShowToast(context, context.getString(R.string.submission_failed))

            return
        }

        ShowToast(context, context.getString(R.string.submission_successful))
        onSuccess()
    }

    private fun backend(context: Context): Backend {
        val preferencesManager = PreferencesManager(context)
        return DataFactory(preferencesManager.serverType).backend(
            preferencesManager.scheme,
            preferencesManager.domain,
            preferencesManager.token
        )
    }

    private fun toBookmark(backend: Backend, context: Context): Bookmark {
        val preferencesManager = PreferencesManager(context)
        val factory = DataFactory(preferencesManager.serverType)

        val tags = factory.tags(tags.split(","))
        val collection = if (collectionId == "-1") null else backend.getCollection(collectionId)

        val bookmark = factory.bookmark(
            id = id,
            url = url,
            name = name,
            description = description,
            tags = tags,
            collection = collection
        )

        return bookmark
    }
}