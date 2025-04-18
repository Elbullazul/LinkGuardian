package dev.elbullazul.linkguardian.ui.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R

@Composable
fun BottomSheetAction(imageVector: ImageVector, iconTint: Color, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .clickable {
                onClick()
            }) {
        Icon(
            tint = iconTint,
            imageVector = imageVector,
            contentDescription = text
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetActionPreview() {
    Column {
        BottomSheetAction(
            imageVector = Icons.Default.Delete,
            iconTint = MaterialTheme.colorScheme.error,
            text = stringResource(R.string.delete),
            onClick = {})
    }
}