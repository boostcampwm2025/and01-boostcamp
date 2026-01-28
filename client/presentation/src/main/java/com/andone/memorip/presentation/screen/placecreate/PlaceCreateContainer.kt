package com.andone.memorip.presentation.screen.placecreate

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.placecreate.PlaceCreateContainerDimens.BAR_WIDTH_FRACTION
import com.andone.memorip.presentation.screen.placecreate.component.PlaceCreateTopBar
import com.andone.memorip.presentation.screen.placecreate.component.StepProgressBar
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateStep
import com.andone.memorip.presentation.screen.selectcategory.SelectCategoryScreen
import com.andone.memorip.presentation.screen.selectgroup.SelectGroupScreen
import com.andone.memorip.presentation.screen.selectgroup.model.toGroupUiModel
import com.andone.memorip.presentation.screen.selectimage.SelectImageScreen
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationScreen
import com.andone.memorip.presentation.theme.MemoripSpace

private object PlaceCreateContainerDimens {
    const val BAR_WIDTH_FRACTION = 0.4f
}

private object PlaceCreateNavGraphConstants {
    const val TOTAL_STEP_SIZE = 3
}

@Composable
fun PlaceCreateContainer(
    onNavigateToHome: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<PlaceCreateViewModel>()

    var currentStep by rememberSaveable { mutableStateOf(PlaceCreateStep.SelectImage) }

    val handleBackAction: () -> Unit = {
        when (currentStep) {
            PlaceCreateStep.SelectImage -> onBackClick()
            PlaceCreateStep.SelectLocation -> currentStep = PlaceCreateStep.SelectImage
            PlaceCreateStep.PlaceCreate -> currentStep = PlaceCreateStep.SelectLocation
            PlaceCreateStep.SelectCategory -> currentStep = PlaceCreateStep.PlaceCreate
            PlaceCreateStep.SelectGroup -> currentStep = PlaceCreateStep.PlaceCreate
        }
    }

    BackHandler { handleBackAction() }

    AnimatedContent(
        targetState = currentStep.stepIndex != null,
        modifier = modifier,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
            } else {
                (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
            }
        },
    ) { isMainStep ->
        if (isMainStep) {
            PlaceCreateMainStep(
                step = currentStep,
                onImagesChange = { images, thumbnailImageRatio ->
                    viewModel.updateImages(images, thumbnailImageRatio)
                },
                onLocationChange = viewModel::updateLocation,
                onStepChange = { currentStep = it },
                onNavigateToHome = onNavigateToHome,
                onBackClick = handleBackAction,
                viewModel = viewModel
            )
        } else {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            PlaceCreateSubStep(
                step = currentStep,
                onCategoryChange = viewModel::updateCategory,
                onGroupChange = { group ->
                    viewModel.onAction(PlaceCreateAction.OnGroupSelect(group))
                },
                onStepChange = { currentStep = it },
                currentGroup = uiState.group
            )
        }
    }
}

@Composable
fun PlaceCreateMainStep(
    step: PlaceCreateStep,
    onImagesChange: (List<Uri>, Float) -> Unit,
    onLocationChange: (LocationUiModel) -> Unit,
    onStepChange: (PlaceCreateStep) -> Unit,
    onNavigateToHome: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceCreateViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PlaceCreateTopBar(
                currentStep = step,
                onBackClick = onBackClick
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                StepProgressBar(
                    currentStep = step.stepIndex,
                    modifier = Modifier.fillMaxWidth(BAR_WIDTH_FRACTION)
                )
            }

            Spacer(modifier = Modifier.height(MemoripSpace.SpaceSmall))
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
                    }
                }
            ) { step ->
                when (step) {
                    PlaceCreateStep.SelectImage -> {
                        SelectImageScreen(
                            onBack = onBackClick,
                            onImageSelect = { images, thumbnailImageRatio ->
                                onImagesChange(images, thumbnailImageRatio)
                                onStepChange(PlaceCreateStep.SelectLocation)
                            }
                        )
                    }

                    PlaceCreateStep.SelectLocation -> {
                        SelectLocationScreen(
                            onLocationSelect = { location ->
                                onLocationChange(location)
                                onStepChange(PlaceCreateStep.PlaceCreate)
                            }
                        )
                    }

                    PlaceCreateStep.PlaceCreate -> {
                        PlaceCreateScreen(
                            onCategoryClick = { onStepChange(PlaceCreateStep.SelectCategory) },
                            onLocationClick = { onStepChange(PlaceCreateStep.SelectLocation) },
                            onGroupClick = { onStepChange(PlaceCreateStep.SelectGroup) },
                            onNavigateToHome = onNavigateToHome,
                            viewModel = viewModel
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun PlaceCreateSubStep(
    step: PlaceCreateStep,
    onCategoryChange: (List<TagUiModel>) -> Unit,
    onGroupChange: (GroupUiModel) -> Unit,
    onStepChange: (PlaceCreateStep) -> Unit,
    modifier: Modifier = Modifier,
    currentGroup: GroupUiModel? = null
) {
    when (step) {
        PlaceCreateStep.SelectCategory -> {
            SelectCategoryScreen(
                onCategorySelect = { category ->
                    onCategoryChange(category)
                    onStepChange(PlaceCreateStep.PlaceCreate)
                },
                onBackClick = { onStepChange(PlaceCreateStep.PlaceCreate) },
                modifier = modifier
            )
        }

        PlaceCreateStep.SelectGroup -> {
            SelectGroupScreen(
                onGroupSelect = { selectGroup ->
                    onGroupChange(selectGroup.toGroupUiModel())
                    onStepChange(PlaceCreateStep.PlaceCreate)
                },
                onBackClick = { onStepChange(PlaceCreateStep.PlaceCreate) },
                placeId = null,
                initialSelectedGroupId = currentGroup?.id,
                modifier = modifier
            )
        }

        else -> {}
    }
}