package com.andone.memorip.data.ai

import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer

class TFLiteRunner(
    modelBuffer: ByteBuffer,
    maxLen: Int,
    threads: Int
) {
    private val interpreter = Interpreter(modelBuffer, Interpreter.Options().apply {
        setNumThreads(threads)
    })

    init {
        interpreter.resizeInput(0, intArrayOf(1, maxLen))
        interpreter.resizeInput(1, intArrayOf(1, maxLen))
        interpreter.resizeInput(2, intArrayOf(1, maxLen))
        interpreter.allocateTensors()
    }

    fun run(
        attention: Array<IntArray>,
        ids: Array<IntArray>,
        type: Array<IntArray>,
        labelSize: Int
    ): FloatArray {
        val logits = Array(1) { FloatArray(labelSize) }
        interpreter.runForMultipleInputsOutputs(
            arrayOf(attention, ids, type),
            mapOf(0 to logits)
        )
        return logits[0]
    }
}