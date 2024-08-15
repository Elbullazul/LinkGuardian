package dev.elbullazul.linkguardian.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.api.APIWrapper
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFragment(wrapper: APIWrapper) {
    // TODO: top bar and bottom bar for navigation
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            }
        )

        LinkList(wrapper = wrapper)

        BottomAppBar {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = stringResource(id = R.string.links)) },
                label = { Text(text = stringResource(id = R.string.links)) },
                selected = true,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    LinkGuardianTheme {
        MainFragment(
            wrapper = APIWrapper(LocalContext.current, "","")
        )
    }
}