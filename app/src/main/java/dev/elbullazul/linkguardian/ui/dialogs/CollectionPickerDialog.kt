package dev.elbullazul.linkguardian.ui.dialogs

import android.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.ui.pages.BookmarkEditorPage
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun CollectionPickerDialog(collections: List<Collection>, selectedCollectionId: String, onSelect: (collectionId: String) -> Unit) {
    val names = collections.map { it.name }.toTypedArray()
    val collectionId = remember { mutableStateOf("-1") }
    val selectedCollection = collections.indexOfFirst {
        it.getId() == selectedCollectionId
    }

    val dialog = AlertDialog.Builder(LocalContext.current)
        .setTitle(stringResource(R.string.collection_select))
        .setPositiveButton(stringResource(R.string.select)) { _, _ ->
            onSelect(collectionId.value)
        }
        .setNegativeButton(stringResource(R.string.none)) { _, _ ->
            onSelect("-1")
        }
        .setSingleChoiceItems(names, selectedCollection) { _, which ->
            collectionId.value = collections[which].getId()
        }
        .create()

    dialog.show()
}

@Preview(showBackground = true)
@Composable
fun CollectionPickerDialogPreview() {
    LinkGuardianTheme(darkTheme = true) {
        CollectionPickerDialog(
            collections = listOf(
                LinkwardenCollection(id = 1, name = "Collection 1"),
                LinkwardenCollection(id = 2, name = "Collection 2"),
                LinkwardenCollection(id = 3, name = "Collection 3")
            ),
            selectedCollectionId = "2",
            onSelect = {}
        )
    }
}