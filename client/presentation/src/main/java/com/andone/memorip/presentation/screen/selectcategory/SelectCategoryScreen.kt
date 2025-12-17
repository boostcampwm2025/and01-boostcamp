package com.andone.memorip.presentation.screen.selectcategory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.LocalMemoripColors
import com.andone.memorip.presentation.theme.MemoripTheme
import org.andone.memorip.presentation.theme.LocalMemoripTypography

@Composable
fun SelectCategoryScreen() {
    Scaffold(
        topBar = { SelectCategoryTopBar() },
        floatingActionButton = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectCategoryTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.select_category_title),
                style = LocalMemoripTypography.current.headline1
            )
        },
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.back_ic),
                contentDescription = stringResource(R.string.general_back_button_description)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalMemoripColors.current.offWhite,
            navigationIconContentColor = LocalMemoripColors.current.black,
            titleContentColor = LocalMemoripColors.current.black
        )
    )
}

@Preview
@Composable
private fun SelectCategoryPrev() {
    MemoripTheme {
        SelectCategoryScreen()
    }
}