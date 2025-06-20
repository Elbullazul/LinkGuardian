package dev.elbullazul.linkguardian.ui.models

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.findFirstRoot
import androidx.lifecycle.ViewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.data.DataFactory
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.storage.PreferencesManager

class BookmarkListViewModel: ViewModel() {
    var collectionIdFilter by mutableStateOf("")
        private set
    var tagIdFilter by mutableStateOf("")
        private set
    var hasFilters by mutableStateOf(false)
        private set
    val bookmarks: MutableList<Bookmark> = mutableListOf()

    // TODO: allow multiple link selection?
    private val selectedBookmarks: MutableList<Bookmark> = mutableListOf()

    var initialized by mutableStateOf(false)
        private set
    var isReloading by mutableStateOf(true)
        private set
    var isActionSheetVisible by mutableStateOf(false)
        private set

    fun updateCollectionFilter(collectionId: String) {
        collectionIdFilter = collectionId
        hasFilters = true

        isReloading = true
    }

    fun updateTagFilter(tagId: String) {
        tagIdFilter = tagId
        hasFilters = true

        isReloading = true
    }

    fun loadFilters(collectionId: String?, tagId: String?) {
        if (!collectionId.isNullOrBlank())
            updateCollectionFilter(collectionId)
        if (!tagId.isNullOrBlank())
            updateTagFilter(tagId)

        initialized = true
    }

    fun clearFilters() {
        collectionIdFilter = ""
        tagIdFilter = ""

        hasFilters = false

        reload()
    }

    fun getFilterString(context: Context): String {
        var text = ""
        val backend = backend(context)

        if (collectionIdFilter.isNotBlank())
            text += "Collection: ${backend.getCollection(collectionIdFilter).name}"
        if (tagIdFilter.isNotBlank()) {
            if (text.isNotBlank()) text += " "
            text += "Tag: ${backend.getTag(tagIdFilter).name}"
        }

        return text
    }

    fun showBottomSheet(bookmark: Bookmark) {
        selectedBookmarks.add(bookmark)

        isActionSheetVisible = true
    }

    fun hideBottomSheet() {
        isActionSheetVisible = false
    }

    fun reload() {
        bookmarks.clear()
        selectedBookmarks.clear()

        isReloading = true
    }

    suspend fun loadBookmarks(context: Context) {
        with (backend(context)) {
            while (hasBookmarks) {
                bookmarks += getBookmarks(
                    collectionIdFilter.ifEmpty { null },
                    tagIdFilter.ifEmpty { null }
                )
            }
        }

        isReloading = false
    }

    fun backend(context: Context): Backend {
        val preferencesManager = PreferencesManager(context)
        return DataFactory(preferencesManager.getBackendType()).backend(
            preferencesManager.getScheme(),
            preferencesManager.getDomain(),
            preferencesManager.getToken()
        )
    }

    fun editSelected(launchEditor: (bookmarkId: String) -> Unit) {
        hideBottomSheet()

        launchEditor(selectedBookmarks.first().getId())

        selectedBookmarks.clear()
    }

    fun shareSelected(context: Context) {
        hideBottomSheet()

        // TODO: use selected bookmarks' URL
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, selectedBookmarks.first().url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)

        selectedBookmarks.clear()
    }

    fun deleteSelected(context: Context) {
        hideBottomSheet()

        backend(context).deleteBookmark(selectedBookmarks.first())

        reload()

        selectedBookmarks.clear()
    }

    fun showPreviews(context: Context): Boolean {
        return PreferencesManager(context).getShowPreviews()
    }

    fun selectedLinkTitle(context: Context): String {
        with (selectedBookmarks) {
            if (size == 1)
                return first().name

            return "$size ${context.getString(R.string.selected_links)}"
        }
    }

    fun selectedLinkDescription(context: Context): String {
        with (selectedBookmarks) {
            if (size == 1)
                return first().url.toString()

            return ""
        }
    }
}