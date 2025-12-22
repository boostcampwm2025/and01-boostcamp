package com.andone.memorip.presentation.selectcategory

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.dialog.MemoripCategoryInputDialog
import com.andone.memorip.presentation.selectcategory.component.CategoryItem
import com.andone.memorip.presentation.selectcategory.component.SelectCategoryTopBar
import com.andone.memorip.presentation.selectcategory.model.Category
import com.andone.memorip.presentation.selectcategory.model.SelectCategoryAction
import com.andone.memorip.presentation.selectcategory.model.SelectCategoryEvent
import com.andone.memorip.presentation.selectcategory.model.toErrorMessage
import com.andone.memorip.presentation.theme.MemoripPadding.PaddingMedium
import com.andone.memorip.presentation.theme.MemoripTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SelectCategoryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SelectCategoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                SelectCategoryEvent.onNavigateBack -> {
                    onBackClick()
                }

                is SelectCategoryEvent.onNavigateAddPlace -> {
                    Log.d("UI TEST", "navigation add place ${event.categories}")
                }

                SelectCategoryEvent.onShowDialog -> {
                    showDialog = true
                }

                SelectCategoryEvent.onDismissDialog -> {
                    showDialog = false
                }

                is SelectCategoryEvent.onShowSnackbar -> {
                    Log.d("UI TEST","show snackbar : ${event.message.toErrorMessage(context = context)}")
                }
            }
        }
    }

    SelectCategoryContent(
        categories = uiState.categories,
        checkedList = uiState.checkedSet,
        onAction = viewModel::onAction,
        modifier = modifier
    )

    if (showDialog) {
        MemoripCategoryInputDialog(
            title = stringResource(R.string.select_category_dialog_title),
            onConfirmClick = { category, color ->
                viewModel.onAction(
                    action = SelectCategoryAction.onConfirmDialogClick(
                        category = category,
                        color = color
                    )
                )
            },
            onCancelClick = { viewModel.onAction(SelectCategoryAction.onCancelDialogClick) },
            onDismissRequest = { viewModel.onAction(SelectCategoryAction.onCancelDialogClick) }
        )
    }
}

@Composable
private fun SelectCategoryContent(
    categories: ImmutableList<Category>,
    checkedList: ImmutableSet<Long>,
    onAction: (SelectCategoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SelectCategoryTopBar(
                checkEnabled = checkedList.isNotEmpty(),
                onConfirmClick = { onAction(SelectCategoryAction.onConfirmClick) },
                onBackClick = { onAction(SelectCategoryAction.onBackClick) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(SelectCategoryAction.onFABClick) },
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.black
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
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
                    onCheckedChange = { checked ->
                        onAction(
                            SelectCategoryAction.onCategoryItemClick(
                                category = category,
                                checked = checked
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectCategoryPrev() {
    MemoripTheme {
        SelectCategoryScreen(onBackClick = {})
    }
}