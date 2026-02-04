package com.andone.memorip.data.ai

import BertTokenizer
import android.content.Context
import com.andone.memorip.domain.ai.ToxicityAnalyzer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.exp

class ToxicityClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) : ToxicityAnalyzer {

    private val labels = AssetLoader.loadLines(context, "label.txt")
    private val tokenizer =
        BertTokenizer(AssetLoader.loadVocab(context, "vocab.txt"), 128)

    private val runner =
        TFLiteRunner(AssetLoader.loadModel(context, "toxicity.tflite"), 128, 4)

    override fun predict(text: String): List<Pair<String, Float>> {

        val enc = tokenizer.encode(text)

        val logits = runner.run(
            arrayOf(enc.attentionMask),
            arrayOf(enc.inputIds),
            arrayOf(enc.tokenTypeIds),
            labels.size
        )

        val probes = FloatArray(logits.size)
        for (i in logits.indices) {
            probes[i] = 1f / (1f + exp(-logits[i]))
        }

        val cleanIdx = labels.indexOf("clean")
        val cleanProb = probes[cleanIdx]

        if (cleanProb > 0.45f) return emptyList()

        val result = mutableListOf<Pair<String, Float>>()

        for (i in probes.indices) {
            val label = labels[i]
            if (label == "clean") continue

            if (probes[i] > 0.8f) {
                result.add(label to probes[i])
            }
        }

        return result
    }
}
