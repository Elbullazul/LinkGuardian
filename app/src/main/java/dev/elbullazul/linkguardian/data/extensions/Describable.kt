package dev.elbullazul.linkguardian.data.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.elbullazul.linkguardian.R

interface Describable {
    val description: String?

    @Composable
    fun truncatedDescription(): String {
        if (description == null || description!!.isEmpty())
            return stringResource(R.string.no_description)

        if (description!!.length > 150)
            return description!!.substring(0,150) + "..."

        return description!!
    }
}