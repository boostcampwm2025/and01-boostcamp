package com.andone.memorip.presentation.screen.placeedit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditStep
import com.andone.memorip.presentation.screen.selectcategory.SelectCategoryScreen
import com.andone.memorip.presentation.screen.selecttrip.SelectTripScreen
import com.andone.memorip.presentation.screen.selecttrip.model.toTripUiModel

@Composable
fun PlaceEditContainer(
    place: PlaceUiModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by rememberSaveable { mutableStateOf(PlaceEditStep.PlaceEdit) }

    val viewModel = hiltViewModel<PlaceEditViewModel, PlaceEditViewModel.Factory>(
        key = place.hashCode().toString(),
        creationCallback = { factory ->
            factory.create(place)
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = currentStep != PlaceEditStep.PlaceEdit) {
        currentStep = PlaceEditStep.PlaceEdit
    }

    AnimatedContent(
        targetState = currentStep,
        modifier = modifier,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
            } else {
                (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
            }
        },
    ) { targetStep ->
        when (targetStep) {
            PlaceEditStep.PlaceEdit -> {
                PlaceEditScreen(
                    onCategoryClick = { currentStep = PlaceEditStep.SelectCategory },
                    onLocationClick = { currentStep = PlaceEditStep.SelectLocation },
                    onTripClick = { currentStep = PlaceEditStep.SelectTrip },
                    onNavigateBack = onNavigateBack,
                    viewModel = viewModel
                )
            }

            PlaceEditStep.SelectLocation -> {

            }

            PlaceEditStep.SelectTrip -> {
                SelectTripScreen(
                    onTripSelect = { trips ->
                        viewModel.updateTrip(trips = trips.map { it.toTripUiModel() })
                        currentStep = PlaceEditStep.PlaceEdit
                    },
                    onBackClick = { currentStep = PlaceEditStep.PlaceEdit },
                    initialSelectedTripIds = uiState.trips.map { it.id }
                )
            }

            PlaceEditStep.SelectCategory -> {
                SelectCategoryScreen(
                    onCategorySelect = { tags ->
                        viewModel.updateTag(tags = tags)
                        currentStep = PlaceEditStep.PlaceEdit
                    },
                    onBackClick = { currentStep = PlaceEditStep.PlaceEdit },
                    selectedTags = uiState.tags
                )
            }
        }
    }
}