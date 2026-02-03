package com.andone.memorip.presentation.screen.selectlocation

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.andone.memorip.domain.repository.KakaoSearchRepository
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationViewModelConstants.TIMEOUT_MILLS
import com.andone.memorip.presentation.screen.selectlocation.model.SelectLocationAction
import com.andone.memorip.presentation.screen.selectlocation.model.SelectLocationEvent
import com.andone.memorip.presentation.screen.selectlocation.model.SelectLocationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume

private object SelectLocationViewModelConstants {
    const val TIMEOUT_MILLS = 500L
}

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
    private val searchRepository: KakaoSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectLocationUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectLocationEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    @OptIn(FlowPreview::class)
    private val searchQueryFlow = _uiState
        .map { it.query }
        .distinctUntilChanged()
        .debounce(TIMEOUT_MILLS)

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationsPagingFlow: Flow<PagingData<LocationUiModel>> = searchQueryFlow
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchRepository.searchLocations(query)
                    .map { pagingData -> pagingData.map { dto -> dto.toUiModel() } }
            }
        }
        .cachedIn(viewModelScope)

    fun onAction(action: SelectLocationAction) {
        when (action) {
            is SelectLocationAction.OnQueryChange -> {
                changeQuery(query = action.query)
            }

            is SelectLocationAction.OnLocationClick -> {
                _uiState.update { it.copy(location = action.location) }
            }

            is SelectLocationAction.OnMapClick -> {
                _uiState.update {
                    it.copy(
                        location = LocationUiModel(
                            latitude = action.location.latitude,
                            longitude = action.location.longitude
                        )
                    )
                }
            }

            is SelectLocationAction.OnLocationSelect -> {
                viewModelScope.launch {
                    val selectedLocation = if (action.location.address.isEmpty()) {
                        getAddressFromLatLng(
                            action.context,
                            action.location.latitude,
                            action.location.longitude
                        )
                    } else {
                        action.location
                    }

                    selectedLocation?.let { location ->
                        _event.trySend(SelectLocationEvent.SelectLocation(location))
                    }
                }
            }
        }
    }

    private fun changeQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    private suspend fun getAddressFromLatLng(
        context: Context,
        latitude: Double,
        longitude: Double
    ): LocationUiModel = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context, Locale.KOREA)
        return@withContext suspendCancellableCoroutine { continuation ->
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        val firstAddress = addresses.firstOrNull() ?: error("Address not found")
                        continuation.resume(firstAddress.toLocationUiModel())
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    val firstAddress = addresses?.firstOrNull() ?: error("Address not found")
                    continuation.resume(firstAddress.toLocationUiModel())
                }
            } catch (_: Exception) {
                continuation.resume(
                    LocationUiModel(
                        latitude = latitude,
                        longitude = longitude
                    )
                )
            }
        }
    }

    private fun Address.toLocationUiModel(): LocationUiModel {
        return LocationUiModel(
            id = UUID.randomUUID().toString(),
            address = getAddressLine(0),
            latitude = latitude,
            longitude = longitude
        )
    }
}