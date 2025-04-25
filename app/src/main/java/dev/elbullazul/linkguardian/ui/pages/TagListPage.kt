package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.elbullazul.linkguardian.ui.models.TagListViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagListPage(
    onEdit: (String) -> Unit,
    onClick: (String) -> Unit,
    tagListViewModel: TagListViewModel = viewModel()
) {
    val context = LocalContext.current

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 6.dp)
    ) {
        items(tagListViewModel.getTags(context)) { tag ->
            Text(
                text = tag.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 11.dp, vertical = 5.dp)
                    .combinedClickable(
                        onClick = { onClick(tag.getId()) },
                        onLongClick = { onEdit(tag.getId()) }
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TagListPagePreview() {
    TagListPage(onEdit = {}, onClick = {})
}