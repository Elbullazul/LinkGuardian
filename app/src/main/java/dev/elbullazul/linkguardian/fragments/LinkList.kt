package dev.elbullazul.linkguardian.fragments

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.elbullazul.linkguardian.api.APIWrapper
import dev.elbullazul.linkguardian.api.objects.Link
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun LinkList(wrapper: APIWrapper) {
    var cursor = 0;

    val listState = rememberLazyListState()

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - cursor
        }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            cursor += 48
            wrapper.linkData(cursor)

            // TODO: load and display more links
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        for (link: Link in wrapper.linkData().response) {
            item {
                LinkFragment(link = link)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkListPreview() {
    LinkGuardianTheme {
        LinkList(
            wrapper = APIWrapper(LocalContext.current, "","")
        )
    }
}