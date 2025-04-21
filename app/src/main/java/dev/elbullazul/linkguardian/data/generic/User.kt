package dev.elbullazul.linkguardian.data.generic

interface User {
    val username: String

    fun getId(): String
}