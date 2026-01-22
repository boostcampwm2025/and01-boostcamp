package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    isDeleteMode: Boolean = false,
    onDeleteClick: () -> Unit = {},
    onDismissClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(value = false) }

    CenterAlignedTopAppBar(
        title = {
            if (!isDeleteMode) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TopBarTitleButton(
                        title = title,
                        expanded = expanded,
                        modifier = Modifier.clickable(onClick = { expanded = !expanded })
                    )
                }
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (isDeleteMode) {
                IconButton(onClick = onDismissClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.plan_cancel)
                    )
                }
            }
        },
        actions = {
            if (isDeleteMode) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_delete_24),
                        contentDescription = stringResource(R.string.plan_delete),
                        tint = MemoripTheme.colors.error
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MemoripTheme.colors.background,
            scrolledContainerColor = MemoripTheme.colors.background,
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PlanTopAppBarPreview() {
    MemoripTheme {
        PlanTopAppBar(title = "그룹 1")
    }
}