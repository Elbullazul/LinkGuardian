package dev.elbullazul.linkguardian.ui.pages

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.fragments.BookmarkFragment
import dev.elbullazul.linkguardian.ui.fragments.BottomSheetAction
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkListPage(
    collectionId: String? = null,
    tagId: String? = null,
    backend: Backend,
    preferences: PreferencesManager,
    onEdit: (String) -> Unit,
    onTagClick: (String) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val loading = remember { mutableStateOf(true) }
    val itemList = remember { mutableStateListOf<Bookmark>() }
    val selectedBookmarks = remember { mutableStateListOf<Bookmark>() }   // bit of a hack
    val applyFilters = remember { mutableStateOf(!collectionId.isNullOrBlank() || !tagId.isNullOrBlank()) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    fun hideBottomSheet() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    fun reload() {
        scope.launch {
            loading.value = true

            itemList.clear()
            backend.reset()

            while (backend.hasBookmarks) {
                itemList.addAll(backend.run {
                    if (applyFilters.value)
                        getBookmarks(collectionId, tagId)
                    else
                        getBookmarks()
                })
            }

            loading.value = false
        }
    }

    // TODO: maybe a model-based approach would work better
    Column(modifier = Modifier.fillMaxSize()) {
        if (applyFilters.value) {
            val text = if (!collectionId.isNullOrBlank())
                stringResource(
                    R.string.links_for_collection,
                    backend.getCollection(collectionId).name
                )
            else
                stringResource(R.string.links_for_tag, backend.getTag(tagId!!).name)

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1.0f)
                )
                Button(onClick = {
                    applyFilters.value = false
                    reload()
                }) {
                    Text(text = stringResource(R.string.clear_filters))
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TODO: the loading indicator does not spin properly
            if (loading.value) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
            items(itemList) { bookmark ->
                BookmarkFragment(
                    bookmark = bookmark,
                    backend = backend,
                    showPreviews = preferences.showPreviews,
                    onOptionClick = {
                        selectedBookmarks.clear()
                        selectedBookmarks.add(bookmark)

                        showBottomSheet = true
                    },
                    onTagClick = onTagClick
                )
            }
            item {
                Box(modifier = Modifier.padding(bottom = 75.dp))
            }
        }

        LaunchedEffect(key1 = this) {
            reload()
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }, sheetState = sheetState
        ) {
            // this works because selectedBookmarks is always populated before calling the modal sheet
            val selectedBookmark = selectedBookmarks.first()

            Column(modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)) {
                Text(
                    text = selectedBookmark.name,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                )
                Text(
                    text = selectedBookmark.url,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 20.dp, start = 15.dp, end = 15.dp)
                )
                BottomSheetAction(
                    imageVector = Icons.Default.Edit,
                    iconTint = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(R.string.edit),
                    onClick = {
                        hideBottomSheet()

                        onEdit(selectedBookmark.getId())
                    })
                BottomSheetAction(
                    imageVector = Icons.Default.Share,
                    iconTint = MaterialTheme.colorScheme.primary,
                    text = stringResource(R.string.share),
                    onClick = {
                        hideBottomSheet()

                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, selectedBookmark.url)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    })
                BottomSheetAction(
                    imageVector = Icons.Default.Delete,
                    iconTint = MaterialTheme.colorScheme.error,
                    text = stringResource(R.string.delete),
                    onClick = {
                        hideBottomSheet()

                        // TODO: ask for confirmation?

                        backend.deleteBookmark(selectedBookmark)
                        reload()
                    })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkListPreview() {
    LinkGuardianTheme {
        BookmarkListPage(
            backend = LinkwardenBackend("", "", ""),
            preferences = PreferencesManager(LocalContext.current),
            onEdit = {},
            onTagClick = {}
        )
    }
}