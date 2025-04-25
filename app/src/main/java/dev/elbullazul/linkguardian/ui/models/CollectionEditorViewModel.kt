package dev.elbullazul.linkguardian.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ShowToast
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.data.DataFactory
import dev.elbullazul.linkguardian.data.extensions.Describable
import dev.elbullazul.linkguardian.data.extensions.OwnableByMany
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.storage.PreferencesManager

class CollectionEditorViewModel: ViewModel() {
    var id by mutableStateOf("0")   // default value doesn't matter, as long as it's not -1
        private set
    var name by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set

    var initialized by mutableStateOf(false)
        private set
    var update by mutableStateOf(false)
        private set

    fun load(collectionId: String?, context: Context) {
        initialized = true

        if (collectionId.isNullOrBlank()) {
            return
        }

        val backend = backend(context)
        val collection = backend.getCollection(collectionId)

        id = collectionId
        name = collection.name

        if (collection is Describable)
            description = collection.description.toString()

        update = true
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun backend(context: Context): Backend {
        val preferencesManager = PreferencesManager(context)
        val factory = DataFactory(preferencesManager.getBackendType())

        return factory.backend(
            preferencesManager.getScheme(),
            preferencesManager.getDomain(),
            preferencesManager.getToken()
        )
    }

    fun submit(context: Context, onSuccess: () -> Unit) {
        val backend = backend(context)
        val collection = toCollection(context)

        if (update && !backend.updateCollection(collection)) {
            ShowToast(context, context.getString(R.string.submission_failed))

            return
        } else if (!update && !backend.createCollection(collection)) {
            ShowToast(context, context.getString(R.string.submission_failed))

            return
        }

        ShowToast(context, context.getString(R.string.submission_successful))
        onSuccess()
    }

    fun delete(context: Context, onSuccess: () -> Unit) {
        val backend = backend(context)
        val collection = toCollection(context)

        if (!backend.deleteCollection(collection)) {
            ShowToast(context, context.getString(R.string.submission_failed))

            return
        }

        onSuccess()
    }

    private fun toCollection(context: Context): Collection {
        val factory = DataFactory(PreferencesManager(context).getBackendType())
        val ogCollection = backend(context).getCollection(id)

        if (ogCollection is OwnableByMany)
            println(ogCollection.ownershipData)

        val collection = factory.collection(
            id = id,
            name = name,
            description = description,
            ownershipData = if (ogCollection is OwnableByMany) ogCollection.ownershipData else listOf()
        )

        return collection!!
    }
}