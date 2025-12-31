package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.place_list_search)
            )
        },
        singleLine = true,
        shape = memoripShapes.roundedXLarge,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MemoripTheme.colors.offWhite,
            unfocusedContainerColor = MemoripTheme.colors.offWhite,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun SearchTextFieldPreview(){
    MemoripTheme {
        SearchTextField(
            value = "검색",
            onValueChange = {}
        )
    }
}
