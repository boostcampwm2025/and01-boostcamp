package com.andone.memorip.presentation.screen.placecreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateStep
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun StepProgressBar(
    currentStep: Int?,
    modifier: Modifier = Modifier
) {
    val totalSteps = PlaceCreateStep.entries.count { it.stepIndex != null }
    val currentStep = currentStep ?: totalSteps

    val activeColor = MemoripTheme.colors.primary
    val inactiveColor = MemoripTheme.colors.lightGray

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (index in 1..totalSteps) {
            StepCircle(
                step = index,
                currentStep = currentStep,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )

            if (index < totalSteps) {
                StepLine(
                    isActive = currentStep > index,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StepLine(
    isActive: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    val color = if (isActive) activeColor else inactiveColor

    Box(
        modifier = modifier
            .height(MemoripLineWidth.Small)
            .padding(horizontal = MemoripPadding.PaddingXXSmall)
            .background(color)
    )
}

@Composable
private fun StepCircle(
    step: Int,
    currentStep: Int,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    val isCompleted = currentStep > step
    val isActive = currentStep == step

    val backgroundColor = if (isCompleted || isActive) activeColor else inactiveColor
    val contentColor =
        if (isCompleted || isActive) MemoripTheme.colors.white else MemoripTheme.colors.gray

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(MemoripIconSize.IconSizeSmall)
            .background(color = backgroundColor, shape = MemoripTheme.shapes.roundedMax)
    ) {
        if (isCompleted) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(MemoripIconSize.IconSizeXSmall),
                tint = MemoripTheme.colors.white
            )
        } else {
            Text(
                text = step.toString(),
                color = contentColor,
                style = MemoripTheme.typography.bodyBold12
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepProgressBarPreview() {
    StepProgressBar(
        currentStep = 2,
        totalSteps = 3,
        modifier = Modifier.fillMaxWidth(0.5f)
    )
}