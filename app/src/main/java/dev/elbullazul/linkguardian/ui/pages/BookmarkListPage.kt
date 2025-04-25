package dev.elbullazul.linkguardian.ui.pages

import android.annotation.SuppressLint
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.ui.fragments.BookmarkFragment
import dev.elbullazul.linkguardian.ui.fragments.BottomSheetAction
import dev.elbullazul.linkguardian.ui.models.BookmarkListViewModel
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkListPage(
    collectionId: String? = null,
    tagId: String? = null,
    backend: Backend,
    bookmarkListViewModel: BookmarkListViewModel = viewModel(),
    onEdit: (String) -> Unit,
    onTagClick: (String) -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    if (!bookmarkListViewModel.initialized)
        bookmarkListViewModel.loadFilters(collectionId, tagId)

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    when (bookmarkListViewModel.isReloading) {
        true -> scope.launch { bookmarkListViewModel.loadBookmarks(context) }
        false -> {}
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (bookmarkListViewModel.hasFilters) {
            FilterBar(
                filter = bookmarkListViewModel.getFilterString(context),
                onFilterClear = { bookmarkListViewModel.clearFilters() }
            )
        }

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TODO: the loading indicator does not spin properly
            if (bookmarkListViewModel.isReloading) {
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
            items(bookmarkListViewModel.bookmarks) { bookmark ->
                BookmarkFragment(
                    bookmark = bookmark,
                    backend = backend,
                    showPreviews = bookmarkListViewModel.showPreviews(context),
                    onClick = { uriHandler.openUri(bookmark.url) },
                    onLongClick = { bookmarkListViewModel.showBottomSheet(bookmark) },
                    onTagClick = onTagClick
                )
            }
            item { Box(modifier = Modifier.padding(bottom = 75.dp)) }
        }
    }

    if (bookmarkListViewModel.isActionSheetVisible) {
        ModalBottomSheet(onDismissRequest = { bookmarkListViewModel.hideBottomSheet() }, sheetState = sheetState) {
            Column(modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)) {
                Text(
                    text = bookmarkListViewModel.selectedLinkTitle(context),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                )
                Text(
                    text = bookmarkListViewModel.selectedLinkDescription(context),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 20.dp, start = 15.dp, end = 15.dp)
                )
                BottomSheetAction(
                    imageVector = Icons.Default.Edit,
                    iconTint = MaterialTheme.colorScheme.onSurface,
                    text = stringResource(R.string.edit),
                    onClick = { bookmarkListViewModel.editSelected(onEdit) }
                )
                BottomSheetAction(
                    imageVector = Icons.Default.Share,
                    iconTint = MaterialTheme.colorScheme.primary,
                    text = stringResource(R.string.share),
                    onClick = { bookmarkListViewModel.shareSelected(context) }
                )
                BottomSheetAction(
                    imageVector = Icons.Default.Delete,
                    iconTint = MaterialTheme.colorScheme.error,
                    text = stringResource(R.string.delete),
                    onClick = { bookmarkListViewModel.deleteSelected(context) }
                )
            }
        }
    }
}

@Composable
fun FilterBar(
    filter: String,
    onFilterClear: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = filter,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1.0f)
        )
        TextButton(onClick = onFilterClear) {
            Text(text = stringResource(R.string.clear_filters))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkListPreview() {
    LinkGuardianTheme {
        BookmarkListPage(
            backend = LinkwardenBackend("", "", ""),
            onEdit = {},
            onTagClick = {}
        )
    }
}