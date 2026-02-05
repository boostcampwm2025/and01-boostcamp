package com.andone.memorip.presentation.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.andone.memorip.domain.model.Tag
import com.andone.memorip.presentation.util.toHexString
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
@TypeParceler<Color, ColorParceler>
data class TagUiModel(
    val id: String,
    val name: String,
    val color: Color
) : Parcelable

object ColorParceler : Parceler<Color> {
    override fun create(parcel: android.os.Parcel): Color {
        return Color(parcel.readLong().toULong())
    }

    override fun Color.write(parcel: android.os.Parcel, flags: Int) {
        parcel.writeLong(this.value.toLong())
    }
}

fun Tag.toUiModel(): TagUiModel = TagUiModel(
    id = id,
    name = name,
    color = Color(color.toColorInt())
)

fun TagUiModel.toDomainModel(): Tag = Tag(
    id = id,
    name = name,
    color = color.toHexString()
)