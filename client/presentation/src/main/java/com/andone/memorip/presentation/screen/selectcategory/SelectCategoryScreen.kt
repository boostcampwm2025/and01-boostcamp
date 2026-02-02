package com.andone.memorip.presentation.screen.selectcategory

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.EmptyText
import com.andone.memorip.presentation.component.MemoripPagingList
import com.andone.memorip.presentation.component.dialog.MemoripCategoryInputDialog
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.selectcategory.component.CategoryItem
import com.andone.memorip.presentation.screen.selectcategory.component.SelectCategoryTopBar
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryAction
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryEvent
import com.andone.memorip.presentation.screen.selectcategory.model.toErrorMessage
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun SelectCategoryScreen(
    onCategorySelect: (List<TagUiModel>) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedTags: List<TagUiModel> = emptyList(),
    viewModel: SelectCategoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tagsPagingItems = viewModel.tagsPagingFlow.collectAsLazyPagingItems()

    var showDialog by remember { mutableStateOf(false) }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            SelectCategoryEvent.NavigateBack -> {
                onBackClick()
            }

            is SelectCategoryEvent.SelectCategory -> {
                onCategorySelect(event.categories)
            }

            SelectCategoryEvent.ShowDialog -> {
                showDialog = true
            }

            SelectCategoryEvent.DismissDialog -> {
                tagsPagingItems.refresh()
                showDialog = false
            }

            is SelectCategoryEvent.ShowSnackBar -> {
                Log.d(
                    "UI TEST",
                    "show snackbar : ${event.message.toErrorMessage(context = context)}"
                )
            }
        }
    }

    LaunchedEffect(selectedTags) {
        viewModel.onAction(SelectCategoryAction.OnInitialTags(selectedTags))
    }

    SelectCategoryContent(
        tagsPagingItems = tagsPagingItems,
        checkedCategories = uiState.checkedCategories,
        onAction = viewModel::onAction,
        modifier = modifier
    )

    if (showDialog) {
        MemoripCategoryInputDialog(
            title = stringResource(R.string.select_category_dialog_title),
            onConfirmClick = { category, color ->
                viewModel.onAction(
                    action = SelectCategoryAction.OnDialogConfirmClick(
                        category = category,
                        color = color
                    )
                )
            },
            onCancelClick = { viewModel.onAction(SelectCategoryAction.OnDialogCancelClick) },
            onDismissRequest = { viewModel.onAction(SelectCategoryAction.OnDialogCancelClick) }
        )
    }
}

@Composable
private fun SelectCategoryContent(
    tagsPagingItems: LazyPagingItems<TagUiModel>,
    checkedCategories: List<TagUiModel>,
    onAction: (SelectCategoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SelectCategoryTopBar(
                checkEnabled = checkedCategories.isNotEmpty(),
                onConfirmClick = { onAction(SelectCategoryAction.OnConfirmClick) },
                onBackClick = { onAction(SelectCategoryAction.OnBackClick) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(SelectCategoryAction.OnFABClick) },
                containerColor = MemoripTheme.colors.primary,
                contentColor = MemoripTheme.colors.onSurface
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.select_category_create_category_button_description)
                )
            }
        }
    ) { innerPadding ->
        MemoripPagingList(
            pagingItems = tagsPagingItems,
            itemKey = { it.id },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MemoripPadding.AppHorizontalPadding),
            emptyContent = {
                EmptyText(
                    text = stringResource(R.string.place_list_empty),
                    modifier = Modifier.fillMaxSize()
                )
            },
            itemContent = { tag ->
                CategoryItem(
                    tagUiModel = tag,
                    checked = tag in checkedCategories,
                    onCheckedChange = { checked ->
                        onAction(
                            SelectCategoryAction.OnCategoryItemClick(
                                tagUiModel = tag,
                                checked = checked
                            )
                        )
                    }
                )
            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SelectCategoryPreview() {
    MemoripTheme {
        SelectCategoryScreen(
            selectedTags = emptyList(),
            onCategorySelect = {},
            onBackClick = {}
        )
    }
}