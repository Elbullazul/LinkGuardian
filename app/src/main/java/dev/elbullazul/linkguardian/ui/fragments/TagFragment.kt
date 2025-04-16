package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
fun TagFragment(tag: Tag, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick(tag.getId()) }
            .padding(end = 6.dp)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(7.dp)
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 0.dp, horizontal = 9.dp),
            color = MaterialTheme.colorScheme.tertiary,
            text = tag.name
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TagFragmentPreview() {
    LinkGuardianTheme {
        TagFragment(
            LinkwardenTag(
                id = -1,
                name = "tag1"
            )
        ) {}
    }
}