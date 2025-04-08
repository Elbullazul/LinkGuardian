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
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.dialogs.CollectionPickerDialog
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun SubmitBookmarkPage(backend: Backend, preferences: PreferencesManager, onSubmit: () -> Unit) {
    val context = LocalContext.current
    val showCollectionPicker = rememberSaveable { (mutableStateOf(false)) }
    var collections = remember { mutableListOf<Collection>() }

    val linkUrl = remember { mutableStateOf("") }
    val tags = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val collectionIndex = remember { mutableIntStateOf(-1) }

    if (showCollectionPicker.value) {
        collections = backend.getCollections().toMutableList()

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
            value = linkUrl.value,
            onValueChange = { linkUrl.value = it },
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
            Button(onClick = {
                // TODO: replace direct instantiation by factory method
                val link = LinkwardenLink(
                    id = -1,
                    name = name.value,
                    url = linkUrl.value,
                    description = description.value,
                    tags = castTags(tags.value),
                    collection = when (collectionIndex.intValue > -1) {
                        true -> collections[collectionIndex.intValue] as LinkwardenCollection
                        false -> null
                    }
                )

                if (backend.createBookmark(link)) {
                    ShowToast(context, context.getString(R.string.link_submit_success))
                    onSubmit()
                } else {
                    ShowToast(context, context.getString(R.string.link_submit_failed));
                }
            }) {
                Text(text = stringResource(id = R.string.submit))
            }
        }
    }
}

fun castTags(inputTags: String): List<LinkwardenTag> {
    var linkTags = arrayListOf<LinkwardenTag>()

    for (tag in inputTags.split(",")) {
        linkTags.add(
            LinkwardenTag(
                id = -1,
                name = tag
            )
        )
    }

    return linkTags
}

@Preview(showBackground = true)
@Composable
fun SubmitLinkPreview() {
    LinkGuardianTheme(darkTheme = true) {
        SubmitBookmarkPage(
            backend = LinkwardenBackend("", "", ""),
            preferences = PreferencesManager(LocalContext.current),
            onSubmit = {}
        )
    }
}