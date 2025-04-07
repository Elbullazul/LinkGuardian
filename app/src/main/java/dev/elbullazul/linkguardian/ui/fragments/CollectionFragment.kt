package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.generic.Collection
import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun ComposableFragment(collection: Collection) {
    collection as LinkwardenCollection

    Row {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = collection.name,
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(vertical = 6.dp).weight(1.0f)
                )
                Text(
                    text = "${collection.bookmarkCount()} ${stringResource(R.string.links_or_link)}"
                )
            }
//            Text(text = collection.shortDescription())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionfragmentPreview() {
    LinkGuardianTheme(darkTheme = true) {
        ComposableFragment(LinkwardenCollection(
            id = 0,
            name = "test collection",
            description = "test collection, internal use only. very long to test the short description function, so keep typing until we hit the ellipsis..."
        ))
    }
}