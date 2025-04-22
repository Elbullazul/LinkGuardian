package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.data.DataFactory
import dev.elbullazul.linkguardian.data.extensions.Collectionable
import dev.elbullazul.linkguardian.data.extensions.Describable
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.dialogs.CollectionPickerDialog
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun BookmarkEditorPage(bookmarkId: String? = null, bookmarkUrl: String? = null, backend: Backend, preferences: PreferencesManager, onSubmit: () -> Unit) {
    val context = LocalContext.current
    val typeFactory = DataFactory(backend.type)
    val update = remember { mutableStateOf(false) }
    val collections = backend.getCollections().toMutableList()
    val assignExisting = remember { mutableStateOf(true) }

    val id = remember { mutableStateOf("-1") }
    val url = remember { mutableStateOf("") }
    val tags = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val collectionIndex = remember { mutableIntStateOf(-1) }
    val showCollectionPicker = rememberSaveable { (mutableStateOf(false)) }

    if (!bookmarkUrl.isNullOrBlank()) {
        url.value = bookmarkUrl
    }
    else if (!bookmarkId.isNullOrBlank() && assignExisting.value) {
        val bookmark = backend.getBookmark(bookmarkId)

        update.value = true
        id.value = bookmark.getId()
        url.value = bookmark.url
        name.value = bookmark.name
        tags.value = bookmark.tagsToString()

        if (bookmark is Collectionable) {
            val collectionId = bookmark.getCollectionId()

            collectionIndex.intValue = collections.indexOfFirst {
                it.getId() == collectionId
            }
        }

        if (bookmark is Describable)
            description.value = bookmark.description.toString()

        assignExisting.value = false
    }

    if (showCollectionPicker.value) {
        // we pass the collection list so that we can select the appropriate entry if the dialog is re-opened
        // TODO: only pass collection ID. Find it in backend.getCollections() with indexOfFirst { it.getId() == collectionId }
        CollectionPickerDialog(
            collections = collections,
            selected = collectionIndex.intValue
        ) { idx ->
            collectionIndex.intValue = idx
        }

        showCollectionPicker.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = url.value,
            onValueChange = { url.value = it },
            placeholder = { Text(stringResource(id = R.string.server_url_placeholder)) },
            label = { Text(stringResource(id = R.string.link_url)) }
        )
        Text(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 15.dp),
            text = "Optional parameters"
        )
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = tags.value,
            onValueChange = { tags.value = it },
            placeholder = { Text(stringResource(R.string.tags_placeholder)) },
            label = { Text(stringResource(R.string.tags)) }
        )
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = name.value,
            onValueChange = { name.value = it },
            placeholder = { Text(stringResource(R.string.autogenerate_if_empty)) },
            label = { Text(stringResource(R.string.name)) }
        )
        TextField(
            singleLine = false,
            minLines = 3,
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = description.value,
            onValueChange = { description.value = it },
            placeholder = { Text(stringResource(R.string.description_placeholder)) },
            label = { Text(stringResource(R.string.description)) }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextButton(
                onClick = { showCollectionPicker.value = true }
            ) {
                Text(text = stringResource(id = R.string.collection))
            }
            Row(modifier = Modifier.weight(1f)) {}
            Button(
                onClick = {
                    val bookmark = typeFactory.bookmark(
                        id = id.value,
                        url = url.value,
                        name = name.value,
                        description = description.value,
                        tags = tags.value.split(","),
                        collection = when (collectionIndex.intValue > -1) {
                            true -> collections[collectionIndex.intValue]
                            false -> null
                        }
                    )

                    val res = if (update.value) backend.updateBookmark(bookmark) else backend.createBookmark(bookmark)

                    if (res) {
                        ShowToast(context, context.getString(R.string.link_submit_success))
                        onSubmit()
                    } else {
                        ShowToast(context, context.getString(R.string.link_submit_failed));
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.submit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubmitLinkPreview() {
    LinkGuardianTheme(darkTheme = true) {
        BookmarkEditorPage(
            backend = LinkwardenBackend("", "", ""),
            preferences = PreferencesManager(LocalContext.current),
            onSubmit = {}
        )
    }
}