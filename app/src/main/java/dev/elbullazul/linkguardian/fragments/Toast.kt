package dev.elbullazul.linkguardian.fragments

import android.content.Context
import android.widget.Toast

fun ShowToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}