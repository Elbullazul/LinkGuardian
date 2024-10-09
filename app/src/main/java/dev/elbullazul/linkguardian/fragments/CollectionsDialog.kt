package dev.elbullazul.linkguardian.fragments

import android.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.objects.Collection

@Composable
fun CollectionsDialog(selectedIdx: Int, collections: Array<Collection>, onSelect: (collectionId: Int) -> Unit, onCancel: () -> Unit) {
    val context = LocalContext.current
    val selectedCollection = remember { mutableIntStateOf(0) }

    var collectionNames: Array<String> = arrayOf()

    for (collection in collections) {
        collectionNames += collection.name
    }

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setTitle(stringResource(R.string.collection_select))
        .setPositiveButton(stringResource(R.string.select)) { dialog, which ->
            onSelect(selectedCollection.intValue)
        }
        .setNegativeButton(stringResource(R.string.cancel)) { dialog, which ->
            onCancel()
        }
        .setSingleChoiceItems(
            collectionNames, if (selectedIdx > -1) selectedIdx else 0
        ) { dialog, which ->
            selectedCollection.intValue = which
        }

    val dialog: AlertDialog = builder.create()
    dialog.show()
}