package dev.elbullazul.linkguardian.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.elbullazul.linkguardian.R
import dev.elbullazul.linkguardian.ui.models.CollectionEditorViewModel
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

@Composable
fun CollectionEditorPage(
    collectionId: String? = null,
    collectionEditorViewModel: CollectionEditorViewModel = viewModel(),
    onSubmit: () -> Unit
) {
    val context = LocalContext.current

    if (!collectionEditorViewModel.initialized)
        collectionEditorViewModel.load(collectionId, context)

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        CollectionEditorLayout(
            name = collectionEditorViewModel.name,
            description = collectionEditorViewModel.description,
            onNameChange = { collectionEditorViewModel.updateName(it) },
            onDescriptionChange = { collectionEditorViewModel.updateDescription(it) }
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            if (collectionEditorViewModel.update) {
                Button(
                    onClick = { collectionEditorViewModel.delete(context, onSubmit) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) { Text(text = stringResource(R.string.delete)) }
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Button(onClick = { collectionEditorViewModel.submit(context, onSubmit) }) {
                Text(text = stringResource(R.string.submit))
            }
        }
    }
}

@Composable
fun CollectionEditorLayout(
    name: String,
    description: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        label = { Text(stringResource(id = R.string.collection_name)) },
        onValueChange = onNameChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
    OutlinedTextField(
        value = description,
        label = { Text(stringResource(R.string.description)) },
        placeholder = { Text(stringResource(R.string.description_placeholder)) },
        onValueChange = onDescriptionChange,
        singleLine = false,
        minLines = 3,
        maxLines = 3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun CollectionEditorPreview() {
    LinkGuardianTheme(darkTheme = true) {
        CollectionEditorPage {}
    }
}