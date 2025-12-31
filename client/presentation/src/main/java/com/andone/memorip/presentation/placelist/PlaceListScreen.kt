package com.andone.memorip.presentation.placelist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.andone.memorip.presentation.grouplist.GroupListViewModel

@Composable
fun PlaceListScreen(
    onGroupClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceListViewModel = hiltViewModel(),
){

}