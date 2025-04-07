package dev.elbullazul.linkguardian

import android.content.Context
import android.widget.Toast

fun ShowToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}