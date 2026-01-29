package com.andone.memorip.presentation.screen.groupdetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map as pagingMap
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.navigation.GroupDetail
import com.andone.memorip.presentation.component.map.MapClusterManager
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailEvent
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailUiState
import com.andone.memorip.presentation.screen.groupdetail.model.MapBottomSheetStep
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Projection
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = GroupDetailViewModel.Factory::class)
class GroupDetailViewModel @AssistedInject constructor(
    @Assisted route: GroupDetail,
    @param:ApplicationContext private val context: Context,
    private val repository: GroupRepository
) : ViewModel() {

    private val groupId: String = route.groupId

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<GroupDetailEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    private val clusterManager = MapClusterManager(context)

    val placesPagingFlow: Flow<PagingData<Place>> =
        repository.getGroupPlaces(groupId)
            .map { pagingData ->
                pagingData.pagingMap { it.toUiModel() }
            }
            .cachedIn(viewModelScope)

    private val _placesListStateFlow = MutableStateFlow<List<Place>>(emptyList())
    val placesListStateFlow: StateFlow<List<Place>> = _placesListStateFlow.asStateFlow()

    private val _projectionStateFlow = MutableStateFlow<Projection?>(null).apply {
    }
    val projectionStateFlow: StateFlow<Projection?> = _projectionStateFlow.asStateFlow()

    private val _zoomStateFlow = MutableStateFlow(0.0).apply {
    }
    val zoomStateFlow: StateFlow<Double> = _zoomStateFlow.asStateFlow()

    private val _isMapInitialized = MutableStateFlow(false)
    val isMapInitialized: StateFlow<Boolean> = _isMapInitialized.asStateFlow()

    val clusteredItemsStateFlow: StateFlow<List<MapClusterManager.ClusterItem>> =
        combine(
            placesListStateFlow,
            projectionStateFlow,
            zoomStateFlow,
            isMapInitialized
        ) { places, projection, zoom, isInitialized ->
            if (!isInitialized || places.isEmpty() || projection == null) {
                return@combine emptyList()
            }

            withContext(Dispatchers.Default) {
                val placeClusterData = places.map { place ->
                    MapClusterManager.PlaceClusterData(
                        id = place.id,
                        position = LatLng(place.latitude, place.longitude),
                        imageUrl = place.thumbnailImage.url,
                        placeData = place
                    )
                }
                clusterManager.cluster(
                    places = placeClusterData,
                    projection = projection
                )
            }
        }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = emptyList()
            )

    init {
        viewModelScope.launch {
            repository.getGroupById(groupId).onSuccess { group ->
                _uiState.update { it.copy(groupName = group.title) }
            }
        }
    }

    fun onAction(action: GroupDetailAction) {
        when (action) {
            GroupDetailAction.OnMenuClick -> {
                _uiState.update { it.copy(expanded = true) }
            }

            GroupDetailAction.OnBackClick -> {
                _event.trySend(element = GroupDetailEvent.NavigateBack)
            }

            is GroupDetailAction.OnPlaceClick -> {
                _event.trySend(element = GroupDetailEvent.NavigateToPlaceDetail(id = action.id))
            }

            GroupDetailAction.OnSearchClick -> {
                /** Search 버튼 눌렀을 때의 event 처리 */
            }

            is GroupDetailAction.OnTabClick -> {
                _uiState.update { it.copy(currentTab = action.currentTab) }
            }

            is GroupDetailAction.OnMapPlaceClick -> {
                _uiState.update {
                    it.copy(
                        mapSelectedPlace = action.place,
                        mapBottomSheetContent = MapBottomSheetStep.PlaceDetail
                    )
                }
            }

            GroupDetailAction.OnMapPlaceClose -> {
                _uiState.update {
                    it.copy(
                        mapSelectedPlace = null,
                        mapBottomSheetContent = MapBottomSheetStep.PlaceList
                    )
                }
            }

            is GroupDetailAction.OnMapPlacesUpdate -> {
                updatePlacesList(action.places)
            }

            GroupDetailAction.OnMapInitialized -> {
                _isMapInitialized.value = true
            }

            is GroupDetailAction.OnMapCameraChange -> {
                onCameraChange(action.projection, action.zoom)
            }
        }
    }

    fun updatePlacesList(places: List<Place>) {
        _placesListStateFlow.value = places
    }

    fun onCameraChange(projection: Projection?, zoom: Double) {
        val oldZoom = _zoomStateFlow.value
        val needsUpdate =
            projection != null && (projection != _projectionStateFlow.value || zoom != oldZoom)

        if (needsUpdate) {
            _projectionStateFlow.value = projection
            _zoomStateFlow.value = zoom
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: GroupDetail): GroupDetailViewModel
    }
}
