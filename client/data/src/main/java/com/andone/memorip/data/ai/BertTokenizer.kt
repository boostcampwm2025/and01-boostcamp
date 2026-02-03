package com.andone.memorip.data.ai

class BertTokenizer(
    private val vocab: Map<String, Int>,
    private val maxLen: Int
) {
    private val unk = "[UNK]"
    private val cls = "[CLS]"
    private val sep = "[SEP]"
    private val pad = "[PAD]"

    data class Encoded(
        val inputIds: IntArray,
        val attentionMask: IntArray,
        val tokenTypeIds: IntArray
    )

    fun encode(text: String): Encoded {
        val tokens = text.lowercase().split(" ")

        val finalTokens = mutableListOf(cls)
        finalTokens.addAll(tokens.take(maxLen - 2))
        finalTokens.add(sep)

        val inputIds = IntArray(maxLen) { vocab[pad] ?: 0 }
        val mask = IntArray(maxLen)

        for (i in finalTokens.indices) {
            inputIds[i] = vocab[finalTokens[i]] ?: vocab[unk]!!
            mask[i] = 1
        }

        return Encoded(inputIds, mask, IntArray(maxLen))
    }
}