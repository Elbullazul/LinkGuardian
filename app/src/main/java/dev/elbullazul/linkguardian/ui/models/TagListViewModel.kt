package dev.elbullazul.linkguardian.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.data.DataFactory
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.storage.PreferencesManager

class TagListViewModel: ViewModel() {
    var tagId by mutableStateOf("-1")
        private set

    fun getTags(context: Context): List<Tag> {
        return backend(context).getTags()
    }

    private fun backend(context: Context): Backend {
        with (PreferencesManager(context)) {
            return DataFactory(getBackendType()).backend(getScheme(), getDomain(), getToken())
        }
    }
}