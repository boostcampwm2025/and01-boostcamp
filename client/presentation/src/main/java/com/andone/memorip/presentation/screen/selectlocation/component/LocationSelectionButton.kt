package com.andone.memorip.presentation.screen.selectlocation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripShadow
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun LocationSelectionButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MemoripTheme.colors.primary,
            contentColor = MemoripTheme.colors.onSurface,
            disabledContainerColor = MemoripTheme.colors.primaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = MemoripShadow.Medium,
            pressedElevation = MemoripShadow.Large,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_location_on),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.select_location_button),
                style = MemoripTheme.typography.labelMedium14
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LocationSelectionButtonEnableTruePreview() {
    MemoripTheme {
        Column {
            LocationSelectionButton(
                enabled = true,
                onClick = {}
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LocationSelectionButtonEnableFalsePreview() {
    MemoripTheme {
        Column {
            LocationSelectionButton(
                enabled = false,
                onClick = {}
            )
        }
    }
}