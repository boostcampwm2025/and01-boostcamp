package com.andone.memorip.data.ai

import android.content.Context
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

object AssetLoader {

    fun loadVocab(context: Context, name: String): Map<String, Int> {
        val map = HashMap<String, Int>()

        context.assets.open(name)
            .bufferedReader()
            .readLines()
            .forEachIndexed { i, line ->
                map[line.trim()] = i
            }

        return map
    }

    fun loadLines(context: Context, name: String): List<String> =
        context.assets.open(name).bufferedReader().readLines()

    fun loadModel(context: Context, name: String): ByteBuffer {
        val fd = context.assets.openFd(name)
        return fd.createInputStream().channel.map(
            FileChannel.MapMode.READ_ONLY,
            fd.startOffset,
            fd.declaredLength
        )
    }
}
