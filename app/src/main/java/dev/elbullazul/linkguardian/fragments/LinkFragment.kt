package dev.elbullazul.linkguardian.fragments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.objects.Link
import dev.elbullazul.linkguardian.api.objects.Collection
import dev.elbullazul.linkguardian.api.objects.Tag
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun LinkFragment(link: Link) {
    val uriHandler = LocalUriHandler.current

    // trim description to 150 characters
    val linkDescription = if (link.description.length > 150)
            link.description.substring(0,150) + "..."
        else
            link.description

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 2.dp)
            .clickable {
                uriHandler.openUri(link.url)
            },
    ) {
        Column(
            modifier = Modifier.padding(10.dp, 5.dp)
        ) {
            Text(
                text = annotatedString,
            )
            Text(
                text = linkDescription.ifEmpty { stringResource(id = R.string.no_description) },
            )
            Row {
                for (tag in link.tags) {
                    Text(
                        modifier = Modifier.padding(top = 7.dp, end = 12.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        text = tag.name
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkCardPreview() {
    LinkGuardianTheme(darkTheme = true) {
        var testTags: Array<Tag> = arrayOf()
        testTags += Tag(id = 0, ownerId = 0, createdAt = "yesterday", updatedAt = "now", name = "Test tag 1")
        testTags += Tag(id = 0, ownerId = 0, createdAt = "yesterday", updatedAt = "now", name = "Test tag 2")

        LinkFragment(
            link = Link(
                id = 0,
                name = "Demo",
                type = "link",
                description = "Link description",
                collectionId = 0,
                url = "https://example.org",
                textContent = null,
                preview = "https://remote.tld/image.png",
                image = "unavailable",
                pdf = "unavailable",
                readable = "unavailable",
                monolith = "aerolith",
                lastPreserved = "now",
                importDate = null,
                createdAt = "yesterday",
                updatedAt = "now",
                tags = testTags,
                collection = Collection(
                    id = 0,
                    name = "Nameless collection",
                    description = "Description",
                    color = "#ff0000",
                    parentId = null,
                    isPublic = false,
                    ownerId = 1,
                    createdAt = "now",
                    updatedAt = "now"
                ),
                pinnedBy = arrayOf()
            )
        )
    }
}