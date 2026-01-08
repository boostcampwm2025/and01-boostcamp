package com.andone.memorip.data.place.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andone.memorip.data.place.datasource.PlaceListPagingSource
import com.andone.memorip.data.place.datasource.PlaceService
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.request.PlaceCreateRequest
import com.andone.memorip.domain.model.response.PlaceCreateResponse
import com.andone.memorip.domain.model.response.PlaceImageUploadResponse
import com.andone.memorip.domain.repository.PlaceListRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PlaceListRepositoryImpl @Inject constructor(
    private val placeService: PlaceService
) : PlaceListRepository {

    override fun getPlaceList(): Flow<PagingData<PlaceListItem>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = FIRST_PAGE_SIZE
            ),
            pagingSourceFactory = {
                PlaceListPagingSource(
                    service = placeService,
                    pageSize = DEFAULT_PAGE_SIZE
                )
            }
        ).flow

    override suspend fun uploadImage(file: File): Result<PlaceImageUploadResponse> {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val result = placeService.uploadImage(body)
            if (result.data != null) {
                Result.success(result.data)
            } else {
                Result.failure(Exception(result.error?.message ?: "알 수 없는 오류"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPlace(place: PlaceCreateRequest): Result<PlaceCreateResponse> {
        return try {
            val result = placeService.createPlace(place)
            if (result.data != null) {
                Result.success(result.data)
            } else {
                Result.failure(Exception(result.error?.message ?: "알 수 없는 오류"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_SIZE = 20
        private const val DEFAULT_PAGE_SIZE = 10
    }
}
