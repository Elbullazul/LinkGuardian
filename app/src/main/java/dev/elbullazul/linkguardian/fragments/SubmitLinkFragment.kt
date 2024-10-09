package dev.elbullazul.linkguardian.fragments

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.LinkwardenAPI
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun SubmitLinkFragment(onSubmit: () -> Unit) {
    val context = LocalContext.current
    val apiWrapper = LinkwardenAPI(context)

    val linkUrl = remember { mutableStateOf("") }

    // optional parameters
    val tags = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val collectionIdx = remember { mutableIntStateOf(-1) }
    val showCollectionPicker = rememberSaveable { (mutableStateOf(false)) }

    val collections = apiWrapper.getCollections().items

    if (showCollectionPicker.value) {
        CollectionsDialog(
            selectedIdx = collectionIdx.intValue,
            collections = collections,
            onSelect = { selectedIdx: Int ->
                collectionIdx.intValue = selectedIdx

                showCollectionPicker.value = false
            },
            onCancel = {
                showCollectionPicker.value = false
            }
        )
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
            label = { Text(stringResource(R.string.tags))}
        )
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = name.value,
            onValueChange = { name.value = it },
            placeholder = { Text(stringResource(R.string.autogenerate_if_empty)) },
            label = { Text(stringResource(R.string.name))}
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
            label = { Text(stringResource(R.string.description))}
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
                val postSuccessful = apiWrapper.postLink(
                    submitUrl = linkUrl.value,

                    // if no collection was selected, linkwarden will assign it to the default collection
                    collection = when (collectionIdx.intValue > -1) {
                        true -> collections[collectionIdx.intValue]
                        false -> null
                    },
                    name = name.value,
                    description = description.value,
                    linkTags = tags.value.split(" ").toTypedArray()
                )

                if (postSuccessful) {
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

@Preview(showBackground = true)
@Composable
fun SubmitLinkPreview() {
    LinkGuardianTheme(darkTheme = true) {
        SubmitLinkFragment {}
    }
}