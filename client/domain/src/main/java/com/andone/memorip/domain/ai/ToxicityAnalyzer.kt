package com.andone.memorip.domain.ai

interface ToxicityAnalyzer {
    fun predict(text: String): List<Pair<String, Float>>
}