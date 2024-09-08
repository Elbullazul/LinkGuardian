package dev.elbullazul.linkguardian

import android.content.Context
import android.widget.Toast

const val API_CURSOR_SIZE = 25

fun ShowToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}