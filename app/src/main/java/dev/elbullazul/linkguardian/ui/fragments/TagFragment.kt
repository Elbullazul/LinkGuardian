package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.data.generic.Tag
import dev.elbullazul.linkguardian.data.linkwarden.LinkwardenTag
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun TagFragment(tags: List<Tag>) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 28.dp),
        modifier = Modifier.height(28.dp)
    ) {
        items(tags) { tag ->
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(3.dp)
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    text = tag.name
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagFragmentPreview() {
    LinkGuardianTheme {
        TagFragment(
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
        )
    }
}