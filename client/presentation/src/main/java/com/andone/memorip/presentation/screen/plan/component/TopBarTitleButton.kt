package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun TopBarTitleButton(
    title: String,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.width(width = 4.dp))

        Icon(
            painter = if (expanded)
                painterResource(R.drawable.ic_outline_arrow_drop_up_24)
            else
                painterResource(R.drawable.ic_outline_arrow_drop_down_24),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun TopBarTitleButtonPreview() {
    MemoripTheme {
        TopBarTitleButton(
            title = "그룹1",
            expanded = false
        )
    }
}
