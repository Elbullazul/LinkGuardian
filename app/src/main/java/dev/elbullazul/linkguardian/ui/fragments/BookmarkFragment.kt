package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenCollection
import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.backends.linkwarden.LinkwardenTag
import dev.elbullazul.linkguardian.backends.generic.Bookmark
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun BookmarkFragment(link: Bookmark, serverUrl: String = "") {
    link as LinkwardenLink

    val uriHandler = LocalUriHandler.current
    val linkDescription = link.shortDescription()

    val annotatedString = buildAnnotatedString {
        append(link.name)
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textDecoration = TextDecoration.Underline
            ),
            start = 0,
            end = link.name.length
        )
    }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(5.dp, 2.dp)
            .clickable {
                uriHandler.openUri(link.url)
            },
    ) {
        Row(Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp)) {
            // TODO: when enabled, make showing previews a toggleable option
            // TODO: model requires the resource's complete URL
//            AsyncImage(
//                model = "https://docs.linkwarden.app/img/logo.png",
//                model = "$serverUrl/${link.preview}",
//                contentDescription = "",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier.height(80.dp).width(80.dp).padding(end = 8.dp)
//            )
            Column {
                Row {
                    Text(annotatedString, Modifier.fillMaxWidth())
                    Icon(Icons.Filled.MoreVert, "")
                }
                Text(linkDescription.ifEmpty { stringResource(id = R.string.no_description) })
                TagFragment(link)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkCardPreview() {
    LinkGuardianTheme(darkTheme = true) {
        BookmarkFragment(
            link = LinkwardenLink(
                id = 0,
                name = "Demo",
                description = "Link description",
                url = "https://example.org",
                preview = "https://github.com/coil-kt/coil/raw/main/logo.svg",
                tags = listOf(
                    LinkwardenTag(
                        id = 0,
                        name = "Test tag 1"
                    ),
                    LinkwardenTag(
                        id = 0,
                        name = "Test tag 2"
                    )
                ),
                collection = LinkwardenCollection(
                    id = 0,
                    name = "Nameless collection"
                )
            )
        )
    }
}