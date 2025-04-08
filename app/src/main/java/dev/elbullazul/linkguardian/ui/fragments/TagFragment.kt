package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.data.generic.Bookmark
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenLink
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun TagFragment(link: Bookmark) {
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

@Preview(showBackground = true)
@Composable
fun TagFragmentPreview() {
    LinkGuardianTheme {
        TagFragment(
            LinkwardenLink(
                id = 0,
                name = "test link",
                url = "https://example.org",
                description = "test link, for internal use only",
                tags = listOf(
                    LinkwardenTag(
                        id = -1,
                        name = "tag1"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag2"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag3"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag4"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag5"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag6"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag7"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag8"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag9"
                    ),
                    LinkwardenTag(
                        id = -1,
                        name = "tag10"
                    )
                )
            )
        )
    }
}