package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.ui.dialogs.CollectionPickerDialog
import dev.elbullazul.linkguardian.ui.models.BookmarkEditorViewModel
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun BookmarkEditorPage(
    bookmarkId: String? = null,
    bookmarkUrl: String? = null,
    backend: Backend,
    bookmarkEditorViewModel: BookmarkEditorViewModel = viewModel(),
    onSubmit: () -> Unit
) {
    val context = LocalContext.current

    if (!bookmarkEditorViewModel.initialized)
        bookmarkEditorViewModel.load(bookmarkId, bookmarkUrl, context)

    if (bookmarkEditorViewModel.isCollectionPickerVisible) {
        CollectionPickerDialog(
            collections = backend.getCollections().toMutableList(),
            selectedCollectionId = bookmarkEditorViewModel.collectionId,
            onSelect = { bookmarkEditorViewModel.updateCollectionId(it) }
        )
        bookmarkEditorViewModel.hideCollectionPicker()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BookmarkEditorLayout(
            url = bookmarkEditorViewModel.url,
            tags = bookmarkEditorViewModel.tags,
            name = bookmarkEditorViewModel.name,
            description = bookmarkEditorViewModel.description,
            onUrlChange = { bookmarkEditorViewModel.updateUrl(it) },
            onTagsChanged = { bookmarkEditorViewModel.updateTags(it) },
            onNameChange = { bookmarkEditorViewModel.updateName(it) },
            onDescriptionChange = { bookmarkEditorViewModel.updateDescription(it) }
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { bookmarkEditorViewModel.showCollectionPicker() }) {
                Text(text = stringResource(id = R.string.collection))
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { bookmarkEditorViewModel.submit(context, onSubmit) }) {
                Text(text = stringResource(id = R.string.submit))
            }
        }
    }
}

@Composable
fun BookmarkEditorLayout(
    url: String,
    tags: String,
    name: String,
    description: String,
    onUrlChange: (String) -> Unit,
    onTagsChanged: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = url,
        label = { Text(stringResource(id = R.string.link_url)) },
        placeholder = { Text(stringResource(id = R.string.server_url_placeholder)) },
        onValueChange = onUrlChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
    Text(
        text = stringResource(R.string.optional_parameters),
        modifier = Modifier.padding(top = 15.dp),
        color = MaterialTheme.colorScheme.secondary
    )
    OutlinedTextField(
        value = tags,
        label = { Text(stringResource(R.string.tags)) },
        placeholder = { Text(stringResource(R.string.tags_placeholder)) },
        onValueChange = onTagsChanged,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
    OutlinedTextField(
        value = name,
        label = { Text(stringResource(R.string.name)) },
        placeholder = { Text(stringResource(R.string.autogenerate_if_empty)) },
        onValueChange = onNameChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
    OutlinedTextField(
        value = description,
        label = { Text(stringResource(R.string.description)) },
        placeholder = { Text(stringResource(R.string.description_placeholder)) },
        onValueChange = onDescriptionChange,
        singleLine = false,
        minLines = 3,
        maxLines = 3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SubmitLinkPreview() {
    LinkGuardianTheme(darkTheme = true) {
        BookmarkEditorPage(
            backend = LinkwardenBackend("", "", ""),
            onSubmit = {}
        )
    }
}