package com.andone.memorip.presentation.screen.placecreate

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceCreate
import com.andone.memorip.presentation.screen.selectcategory.SelectCategoryScreen
import com.andone.memorip.presentation.screen.selectgroup.SelectGroupScreen
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationScreen

private enum class PlaceCreateStep {
    PlaceCreate,
    SelectCategory,
    SelectLocation,
    SelectGroup
}

fun NavBackStack<NavKey>.navigateToPlaceCreate() {
    add(PlaceCreate)
}

fun EntryProviderScope<NavKey>.placeCreate(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceCreate> {
        val viewModel = hiltViewModel<PlaceCreateViewModel>()

        var currentStep by rememberSaveable { mutableStateOf(PlaceCreateStep.PlaceCreate) }

        BackHandler(enabled = currentStep != PlaceCreateStep.PlaceCreate) {
            currentStep = PlaceCreateStep.PlaceCreate
        }

        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState == PlaceCreateStep.PlaceCreate) {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                } else {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                }
            }
        ) { step ->
            when (step) {
                PlaceCreateStep.PlaceCreate -> {
                    PlaceCreateScreen(
                        onCategoryClick = { currentStep = PlaceCreateStep.SelectCategory },
                        onLocationClick = { currentStep = PlaceCreateStep.SelectLocation },
                        onGroupClick = { currentStep = PlaceCreateStep.SelectGroup },
                        onBackClick = onBackClick,
                        modifier = modifier,
                        viewModel = viewModel
                    )
                }

                PlaceCreateStep.SelectCategory -> {
                    SelectCategoryScreen(
                        onCategorySelect = { category ->
                            viewModel.updateCategory(category)
                            currentStep = PlaceCreateStep.PlaceCreate
                        },
                        onBackClick = { currentStep = PlaceCreateStep.PlaceCreate },
                        modifier = modifier
                    )
                }

                PlaceCreateStep.SelectLocation -> {
                    SelectLocationScreen(
                        onLocationSelect = { location ->
                            viewModel.updateLocation(location)
                            currentStep = PlaceCreateStep.PlaceCreate
                        },
                        onBackClick = { currentStep = PlaceCreateStep.PlaceCreate },
                        modifier = modifier
                    )
                }

                PlaceCreateStep.SelectGroup -> {
                    SelectGroupScreen(
                        onGroupSelect = { group ->
                            viewModel.updateGroup(group)
                            currentStep = PlaceCreateStep.PlaceCreate
                        },
                        onBackClick = { currentStep = PlaceCreateStep.PlaceCreate },
                        modifier = modifier
                    )
                }
            }
        }
    }
}