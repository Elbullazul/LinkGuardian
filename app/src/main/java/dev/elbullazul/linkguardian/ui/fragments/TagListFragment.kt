package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun TagListFragment(tags: List<Tag>, onTagClick: (String) -> Unit) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 25.dp),
        modifier = Modifier.height(25.dp)
    ) {
        items(tags) { tag ->
            TagFragment(tag, onTagClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagListFragmentPreview() {
    LinkGuardianTheme {
        TagListFragment(
            listOf(
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
        ) {}
    }
}