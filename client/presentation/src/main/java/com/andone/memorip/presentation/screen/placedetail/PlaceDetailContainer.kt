package com.andone.memorip.presentation.screen.placedetail

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
import androidx.compose.ui.res.stringResource
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailStep
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.screen.placeedit.PlaceEditContainer
import com.andone.memorip.presentation.screen.selecttrip.SelectTripScreen

@Composable
fun PlaceDetailContainer(
    route: PlaceDetail,
    onNavigateBack: () -> Unit,
    onNavigateToTripList: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by rememberSaveable { mutableStateOf(PlaceDetailStep.PlaceDetail) }
    var place by rememberSaveable { mutableStateOf(PlaceUiModel()) }


    BackHandler(enabled = currentStep != PlaceDetailStep.PlaceDetail) {
        currentStep = PlaceDetailStep.PlaceDetail
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
    ) { targetState ->
        when (targetState) {
            PlaceDetailStep.PlaceDetail -> {
                PlaceDetailScreen(
                    route = route,
                    onNavigateBack = onNavigateBack,
                    onNavigateToSelectTrip = {
                        currentStep = PlaceDetailStep.SelectTrip
                    },
                    onNavigateToPlaceEdit = {
                        place = it
                        currentStep = PlaceDetailStep.PlaceEdit
                    },
                    onNavigateToTripList = onNavigateToTripList,
                    modifier = Modifier,
                )
            }

            PlaceDetailStep.PlaceEdit -> {
                PlaceEditContainer(
                    place = place,
                    onNavigateBack = { currentStep = PlaceDetailStep.PlaceDetail }
                )
            }

            PlaceDetailStep.SelectTrip -> {
                SelectTripScreen(
                    onTripSelect = { },
                    onBackClick = { currentStep = PlaceDetailStep.PlaceDetail },
                    title = stringResource(R.string.select_trip_add_to_my_trip_title),
                    placeId = route.placeId,
                    modifier = modifier
                )
            }
        }
    }
}