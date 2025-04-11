package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.backends.Backend
import dev.elbullazul.linkguardian.backends.LinkwardenBackend
import dev.elbullazul.linkguardian.ui.fragments.CollectionFragment
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun CollectionsPage(backend: Backend) {
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)) {
        items(backend.getCollections()) { collection ->
            CollectionFragment(collection)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionListPreview() {
    LinkGuardianTheme(darkTheme = true) {
        CollectionsPage(LinkwardenBackend("","",""))
    }
}