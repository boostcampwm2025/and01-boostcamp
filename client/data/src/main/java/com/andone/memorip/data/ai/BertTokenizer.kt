class BertTokenizer(
    private val vocab: Map<String, Int>,
    private val maxLen: Int
) {

    private val unk = "[UNK]"
    private val cls = "[CLS]"
    private val sep = "[SEP]"
    private val pad = "[PAD]"

    private val unkId = vocab[unk]!!
    private val padId = vocab[pad]!!

    data class Encoded(
        val inputIds: IntArray,
        val attentionMask: IntArray,
        val tokenTypeIds: IntArray
    )

    private fun wordpiece(word: String): List<String> {
        if (vocab.containsKey(word)) return listOf(word)

        val result = mutableListOf<String>()
        var start = 0

        while (start < word.length) {
            var end = word.length
            var cur: String? = null

            while (start < end) {
                var piece = word.substring(start, end)
                if (start > 0) piece = "##$piece"

                if (vocab.containsKey(piece)) {
                    cur = piece
                    break
                }
                end--
            }

            if (cur == null) return listOf(unk)

            result.add(cur)
            start = end
        }

        return result
    }


    fun encode(text: String): Encoded {
        val words = text.trim().split(Regex("\\s+"))

        val tokens = mutableListOf<String>()
        tokens.add(cls)

        for (w in words) {
            tokens.addAll(wordpiece(w))
        }

        tokens.add(sep)

        val inputIds = IntArray(maxLen) { padId }
        val mask = IntArray(maxLen)
        val typeIds = IntArray(maxLen)

        val len = minOf(tokens.size, maxLen)

        for (i in 0 until len) {
            inputIds[i] = vocab[tokens[i]] ?: unkId
            mask[i] = 1
        }

        return Encoded(inputIds, mask, typeIds)
    }
}