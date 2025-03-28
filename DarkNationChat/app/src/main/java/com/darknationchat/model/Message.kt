package com.darknationchat.model

import java.util.Date

data class Message(
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Date = Date()
) {
    companion object {
        const val TYPE_USER = 1
        const val TYPE_BOT = 2
    }

    fun getViewType(): Int {
        return if (isFromUser) TYPE_USER else TYPE_BOT
    }
}