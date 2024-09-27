package dev.elbullazul.linkguardian.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.api.API_CURSOR_SIZE
import dev.elbullazul.linkguardian.api.LinkwardenAPI
import dev.elbullazul.linkguardian.api.objects.Link
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LinkList() {
    val page = remember { mutableIntStateOf(0) }
    val loading = remember { mutableStateOf(false) }
    val itemList = remember { mutableStateListOf<Link>() }
    val listState = rememberLazyListState()
    val apiWrapper = LinkwardenAPI(LocalContext.current)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(itemList) { link ->
                LinkFragment(link = link)
            }

            // show indicator when loading
            item {
                if (loading.value) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(50.dp), strokeWidth = 2.dp)
                    }
                }
            }
        }

        LaunchedEffect(key1 = page.intValue) {
            loading.value = true
            itemList.addAll(apiWrapper.getLinks(page.intValue * API_CURSOR_SIZE).response)
            loading.value = false
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collectLatest { index ->
                    if (!loading.value && index != null && index >= itemList.size - 5) {
                        page.intValue++
                    }
                }
        }
        val context = LocalContext.current
        FloatingActionButton(onClick = { ShowToast(context,"Hello world!") }) {}
    }
}

@Preview(showBackground = true)
@Composable
fun LinkListPreview() {
    LinkGuardianTheme {
        LinkList()
    }
}