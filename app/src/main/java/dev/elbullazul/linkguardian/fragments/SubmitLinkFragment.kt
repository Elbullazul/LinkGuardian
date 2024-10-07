package dev.elbullazul.linkguardian.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.LinkwardenAPI
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun SubmitLinkFragment(onSubmit: () -> Unit) {
    val linkUrl = remember { mutableStateOf("") }
    val tags = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = linkUrl.value,
            onValueChange = { linkUrl.value = it },
            placeholder = { Text(stringResource(id = R.string.server_url_placeholder)) },
            label = { Text(stringResource(id = R.string.link_url)) }
        )
        TextButton(
            onClick = {
                /*TODO*/
            }
        ) {
            Text(text = stringResource(id = R.string.collection))
        }
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            value = tags.value,
            onValueChange = { tags.value = it },
            placeholder = { Text(stringResource(R.string.tags_placeholder)) },
            label = { Text(stringResource(R.string.tags))}
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(onClick = {
                val api = LinkwardenAPI(context)

                if (api.postLink(submitUrl = linkUrl.value, linkTags = tags.value.split(" ").toTypedArray())) {
                    ShowToast(context, context.getString(R.string.link_submit_success))
                    onSubmit()
                } else {
                    ShowToast(context, context.getString(R.string.link_submit_failed));
                }
            }) {
                Text(text = stringResource(id = R.string.submit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubmitLinkPreview() {
    LinkGuardianTheme(darkTheme = true) {
        SubmitLinkFragment({})
    }
}