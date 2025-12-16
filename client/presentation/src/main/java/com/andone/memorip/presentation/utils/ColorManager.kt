package com.andone.memorip.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import com.andone.memorip.presentation.R
import kotlin.random.Random

@Composable
fun rememberColorState(): ColorState {
    return rememberSaveable(saver = ColorState.ColorSaver) { ColorState() }
}

class ColorState(
    color: Color? = null,
    inputColor: String? = null
) {
    private val EXCLUDED_BRIGHT_COLOR_THRESHOLD = 0.8f
    private val MAX_LENGTH = 6

    private var _color = mutableStateOf(color ?: getRandomColor())
    val color get() = _color.value

    private var _inputColor = mutableStateOf(inputColor ?: this.color.toHexRgb())
    val inputColor get() = _inputColor.value

    fun refreshColor() {
        _color.value = getRandomColor()
        _inputColor.value = color.toHexRgb()
    }

    fun updateColor(newColor: String) {
        _inputColor.value = newColor.removePrefix("#")
        if (newColor.length != MAX_LENGTH) { return }

        val colorInt = inputColor.toInt(16)
        _color.value = Color(0xFF000000 or colorInt.toLong())
    }

    fun isValidColorInput(): Boolean {
        return inputColor.isValidHexRgb() && inputColor.length == MAX_LENGTH
    }

    @StringRes
    fun getErrMsg(): Int {
        return if (inputColor.length != MAX_LENGTH) R.string.category_dialog_max_length_err_hint
        else R.string.category_dialog_valid_color_err_hint
    }

    private fun String.isValidHexRgb(): Boolean {
        return all { it.isDigit() || it.uppercaseChar() in 'A'..'F' }
    }

    private fun getRandomColor(): Color {
        var newColor: Color?
        do {
            newColor = Color(
                red = Random.nextFloat(),
                green = Random.nextFloat(),
                blue = Random.nextFloat(),
                alpha = 1f
            )
        } while (isReadableOnWhite(newColor))

        return newColor
    }

    private fun Color.toHexRgb(): String {
        return "%06X".format(this.toArgb() and 0xFFFFFF)
    }

    private fun isReadableOnWhite(color: Color): Boolean {
        return color.luminance() <= EXCLUDED_BRIGHT_COLOR_THRESHOLD
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        val ColorSaver: Saver<ColorState, List<Any>> = Saver(
            save = {
                listOf(
                    it.color.value.toLong(),
                    it.inputColor
                )
            },
            restore = {
                ColorState(
                    color = Color(it[0] as Long),
                    inputColor = it[1] as String
                )
            }
        )
    }
}