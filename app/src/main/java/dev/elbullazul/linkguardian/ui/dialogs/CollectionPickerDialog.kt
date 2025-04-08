package dev.elbullazul.linkguardian.ui.dialogs

import android.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.data.generic.Collection

@Composable
fun CollectionPickerDialog(collections: List<Collection>, selected: Int, onSelect: (collectionId: Int) -> Unit) {
    val names = collections.map { it.name }.toTypedArray()
    val selectedCollectionIndex = remember { mutableIntStateOf(-1) }
    val builder: AlertDialog.Builder = AlertDialog.Builder(LocalContext.current)

    builder
        .setTitle(stringResource(R.string.collection_select))
        .setPositiveButton(stringResource(R.string.select)) { _, _ ->
            onSelect(selectedCollectionIndex.intValue)
        }
        .setNegativeButton(stringResource(R.string.none)) { _, _ ->
            onSelect(-1)
        }
        .setSingleChoiceItems(names, selected) { _, which ->
            selectedCollectionIndex.intValue = which
        }

    val dialog: AlertDialog = builder.create()
    dialog.show()
}