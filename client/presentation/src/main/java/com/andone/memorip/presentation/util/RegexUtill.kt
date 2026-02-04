package com.andone.memorip.presentation.util

fun splitSentences(text: String): List<String> {
    return text
        .split(Regex("[.!?。！？\n]"))
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}