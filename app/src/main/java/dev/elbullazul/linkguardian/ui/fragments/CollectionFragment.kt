package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.data.extensions.Describable
import dev.elbullazul.linkguardian.data.extensions.ParentOfMany
import dev.elbullazul.linkguardian.data.generic.Collection
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun CollectionFragment(collection: Collection, onClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .clickable { onClick(collection.getId()) }
        ) {
            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = collection.name,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (collection is Describable) {
                    Text(text = collection.truncatedDescription())
                }
            }

            if (collection is ParentOfMany) {
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    text = "${collection.getMemberCount()} ${stringResource(R.string.links_or_link)}"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionfragmentPreview() {
    LinkGuardianTheme(darkTheme = true) {
        CollectionFragment(
            onClick = {},
            collection = LinkwardenCollection(
                id = 0,
                name = "test collection",
                description = "test collection, internal use only. very long to test the short description function, so keep typing until we hit the ellipsis...",
            )
        )
    }
}