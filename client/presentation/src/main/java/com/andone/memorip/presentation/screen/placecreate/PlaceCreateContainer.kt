package com.andone.memorip.presentation.screen.placecreate

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.andone.memorip.presentation.screen.placecreate.PlaceCreateNavGraphConstants.TOTAL_STEP_SIZE
import com.andone.memorip.presentation.screen.placecreate.component.PlaceCreateTopBar
import com.andone.memorip.presentation.screen.placecreate.component.StepProgressBar
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateStep
import com.andone.memorip.presentation.screen.selectcategory.SelectCategoryScreen
import com.andone.memorip.presentation.screen.selectgroup.SelectGroupScreen
import com.andone.memorip.presentation.screen.selectimage.SelectImageScreen
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationScreen

private object PlaceCreateNavGraphConstants {
    const val TOTAL_STEP_SIZE = 3
}

@Composable
fun PlaceCreateContainer(
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

    Scaffold(
        modifier = modifier,
        topBar = {
            PlaceCreateTopBar(
                currentStep = currentStep,
                onBackClick = handleBackAction
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            currentStep.step?.let {
                StepProgressBar(
                    currentStep = it,
                    totalSteps = TOTAL_STEP_SIZE,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedContent(
                targetState = currentStep,
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
                            onImageSelect = { images ->
                                viewModel.updateImages(images)
                                currentStep = PlaceCreateStep.SelectLocation
                            }
                        )
                    }

                    PlaceCreateStep.SelectLocation -> {
                        SelectLocationScreen(
                            onLocationSelect = { location ->
                                viewModel.updateLocation(location)
                                currentStep = PlaceCreateStep.PlaceCreate
                            },
                            onBackClick = { currentStep = PlaceCreateStep.SelectImage }
                        )
                    }

                    PlaceCreateStep.PlaceCreate -> {
                        PlaceCreateScreen(
                            onCategoryClick = { currentStep = PlaceCreateStep.SelectCategory },
                            onLocationClick = { currentStep = PlaceCreateStep.SelectLocation },
                            onGroupClick = { currentStep = PlaceCreateStep.SelectGroup },
                            onBackClick = onBackClick,
                            viewModel = viewModel
                        )
                    }

                    PlaceCreateStep.SelectCategory -> {
                        SelectCategoryScreen(
                            onCategorySelect = { category ->
                                viewModel.updateCategory(category)
                                currentStep = PlaceCreateStep.PlaceCreate
                            },
                            onBackClick = { currentStep = PlaceCreateStep.PlaceCreate }
                        )
                    }

                    PlaceCreateStep.SelectGroup -> {
                        SelectGroupScreen(
                            onGroupSelect = { group ->
                                viewModel.updateGroup(group)
                                currentStep = PlaceCreateStep.PlaceCreate
                            },
                            onBackClick = { currentStep = PlaceCreateStep.PlaceCreate }
                        )
                    }
                }
            }
        }
    }
}