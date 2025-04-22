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

        if (description!!.length > SHORT_LENGTH)
            return description!!.substring(0, SHORT_LENGTH) + "..."

        return description!!
    }

    companion object {
        val SHORT_LENGTH = 150
    }
}