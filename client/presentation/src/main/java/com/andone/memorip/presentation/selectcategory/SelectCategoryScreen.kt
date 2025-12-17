package com.andone.memorip.presentation.selectcategory

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.dialog.MemoripCategoryInputDialog
import com.andone.memorip.presentation.selectcategory.Constants.MAX_SELECTABLE_COUNT
import com.andone.memorip.presentation.selectcategory.component.CategoryItem
import com.andone.memorip.presentation.selectcategory.model.Category
import com.andone.memorip.presentation.theme.LocalMemoripColors
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingMedium
import com.andone.memorip.presentation.theme.MemoripTheme
import org.andone.memorip.presentation.theme.LocalMemoripTypography

private object Constants {
    val MAX_SELECTABLE_COUNT = 3
}

@Composable
fun SelectCategoryScreen() {
    val categories = remember{
        mutableStateListOf(
            Category(
                id = 0L,
                category = "맛집",
                color = Color(0xFF000000)
            ),
            Category(
                id = 1L,
                category = "카페",
                color = Color(0xFAA8F0F0)
            ),
            Category(
                id = 2L,
                category = "액티비티",
                color = Color(0xFFCCDD66)
            ),
        )
    }
    val checkedSet = remember{ mutableStateSetOf<Long>() }
    var showDialog by remember{ mutableStateOf(false) }

    SelectCategoryContent(
        categories = categories,
        checkedList = checkedSet,
        onShowDialog = { showDialog = true },
        onCheckedChange = { id, checked ->
            if (checked) {
                if (checkedSet.size < MAX_SELECTABLE_COUNT) checkedSet.add(id)
//                else /** TODO snackbar로 3개까지만 담을 수 있다고 알려주기 */
            } else {
                checkedSet.remove(id)
            }
        }
    )

    if (showDialog) {
        MemoripCategoryInputDialog(
            title = stringResource(R.string.select_category_dialog_title),
            onConfirmClick = { category, color ->
                val newCategory = Category(
                    id = (categories.maxOfOrNull { it.id } ?: -2L) + 1L,
                    category =  category,
                    color = color
                )
                categories.add(newCategory)
                showDialog = false
            },
            onCancelClick = { showDialog = false },
            onDismissRequest = { showDialog = false }
        )
    }
}

@Composable
private fun SelectCategoryContent(
    categories: List<Category>,
    checkedList: Set<Long>,
    onShowDialog: () -> Unit,
    onCheckedChange: (id: Long, checked: Boolean) -> Unit
) {
    Scaffold(
        topBar = { SelectCategoryTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onShowDialog,
            ) {
                Icon(
                    /** TODO 아이콘 변경해야 함 */
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.select_category_create_category_button_description)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = PaddingMedium)
        ) {
            items(
                items = categories,
                key = { it.id }
            ) { category ->
                CategoryItem(
                    category = category,
                    checked = category.id in checkedList,
                    onCheckedChange = { onCheckedChange(category.id, it) }
                )
            }
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
                style = LocalMemoripTypography.current.headline2
            )
        },
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.back_ic),
                contentDescription = stringResource(R.string.select_category_back_button_description)
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