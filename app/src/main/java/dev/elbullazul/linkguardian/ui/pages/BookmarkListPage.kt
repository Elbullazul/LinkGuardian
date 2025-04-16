package dev.elbullazul.linkguardian.ui.pages

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.fragments.BookmarkFragment
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun BookmarkListPage(collectionId: String? = null, tagId: String? = null, backend: Backend, preferences: PreferencesManager, onEdit: (String) -> Unit) {
    val loading = remember { mutableStateOf(true) }
    val itemList = remember { mutableStateListOf<Bookmark>() }
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        // TODO: this should be moved elsewhere, to new dedicated pages for collection and tag viewing, for example
        if (!collectionId.isNullOrBlank() || !tagId.isNullOrBlank()) {
            Row(
                modifier = Modifier.padding(5.dp)
            ) {
                if (!collectionId.isNullOrBlank())
                    Text(text = "${stringResource(R.string.collection)} ${collectionId}" )
                if (!tagId.isNullOrBlank())
                    Text(text = "${stringResource(R.string.tag)} ${tagId}" )
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TODO: the loading indicator does not spin properly
            item {
                if (loading.value) {
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
            items(itemList) { link ->
                BookmarkFragment(
                    link = link,
                    serverUrl = "${backend.scheme}://${backend.domain}",
                    showPreviews = preferences.showPreviews,
                    onEdit = { onEdit(link.getId()) },

                    // TODO: should open the tag page
                    onTagClick = { id->

                        println("=============================$id")
                    }
                )
            }
        }

        LaunchedEffect(key1 = 0) {
            loading.value = true

            // TODO: maybe a model-based approach would work better
            while (backend.hasBookmarks) {
                itemList.addAll(backend.run { getBookmarks(collectionId, tagId) })
            }

            loading.value = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkListPreview() {
    LinkGuardianTheme {
        BookmarkListPage(
            backend = LinkwardenBackend("","",""),
            preferences = PreferencesManager(LocalContext.current),
            onEdit = {}
        )
    }
}