package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenBackend
import dev.elbullazul.linkguardian.ui.fragments.ComposableFragment
import dev.elbullazul.linkguardian.storage.PreferencesManager
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun CollectionsPage() {
    val context = LocalContext.current
    val prefs = PreferencesManager(context)
    prefs.load()

    val backend = LinkwardenBackend(prefs.scheme, prefs.domain, prefs.token)
    val collections = backend.getCollections()
    backend.getCollection(1)

    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)) {
        items(collections) { collection ->
            ComposableFragment(collection)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionListPreview() {
    LinkGuardianTheme(darkTheme = true) {
        CollectionsPage()
    }
}