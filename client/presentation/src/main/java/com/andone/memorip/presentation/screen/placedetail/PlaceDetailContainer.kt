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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailStep
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.screen.placeedit.PlaceEditContainer
import com.andone.memorip.presentation.screen.selectgroup.SelectGroupScreen

@Composable
fun PlaceDetailContainer(
    route: PlaceDetail,
    onNavigateBack: () -> Unit,
    onNavigateGroupList: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by rememberSaveable { mutableStateOf(PlaceDetailStep.PlaceDetail) }

    var place by remember { mutableStateOf(PlaceUiModel()) }

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
                    onNavigateSelectGroup = {
                        currentStep = PlaceDetailStep.SelectGroup
                    },
                    onNavigateToPlaceEdit = {
                        place = it
                        currentStep = PlaceDetailStep.PlaceEdit
                    },
                    onNavigateGroupList = onNavigateGroupList,
                    modifier = Modifier,
                )
            }

            PlaceDetailStep.PlaceEdit -> {
                PlaceEditContainer(
                    place = place,
                    onNavigateBack = { currentStep = PlaceDetailStep.PlaceDetail }
                )
            }

            PlaceDetailStep.SelectGroup -> {
                SelectGroupScreen(
                    onGroupSelect = { },
                    onBackClick = { currentStep = PlaceDetailStep.PlaceDetail },
                    title = stringResource(R.string.select_group_add_to_my_group_title),
                    placeId = route.placeId,
                    modifier = modifier
                )
            }
        }
    }
}