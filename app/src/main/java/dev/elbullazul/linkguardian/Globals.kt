package dev.elbullazul.linkguardian

import android.content.Context
import android.widget.Toast

const val PREFERENCES_KEY_FILE = "com.elbullazul.linkguardian.PREFERENCES_KEY_FILE"
const val PREF_SERVER_URL = "SERVER_URL"
const val PREF_API_TOKEN = "API_TOKEN"
const val EMPTY_STRING = ""

fun ShowToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}