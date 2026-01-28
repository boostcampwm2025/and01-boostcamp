package com.andone.memorip.presentation.component

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoripSearchBarInputField(
    query: String,
    expanded: Boolean,
    onQueryChange: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(value = LocalTextStyle provides MemoripTheme.typography.bodyMedium14) {
        SearchBarDefaults.InputField(
            query = query,
            onQueryChange = { onQueryChange(it) },
            onSearch = { onQueryChange(it) },
            expanded = expanded,
            onExpandedChange = { onExpandedChange(it) },
            modifier = modifier,
            placeholder = {
                Text(
                    text = stringResource(R.string.search_bar_placeholder),
                    style = MemoripTheme.typography.labelMedium14,
                )
            },
            leadingIcon = {
                if (expanded) {
                    IconButton(onClick = {
                        onExpandedChange(false)
                        onQueryChange("")
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.search_bar_go_back_content_description),
                            tint = MemoripTheme.colors.primary
                        )
                    }
                }
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.search_bar_close_content_description),
                            tint = MemoripTheme.colors.primary
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = MemoripTheme.colors.primary
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MemoripTheme.colors.primaryContainer,
                unfocusedContainerColor = MemoripTheme.colors.primaryContainer
            )
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarInputFieldPreview() {
    MemoripTheme {
        MemoripSearchBarInputField(
            query = "테스트 검색어",
            expanded = true,
            onQueryChange = {},
            onExpandedChange = {}
        )
    }
}