package com.andone.memorip.presentation.util.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.TripRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import java.io.IOException

@HiltWorker
class TripWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: TripRepository
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        val flag = inputData.getString(FLAG) ?: return Result.failure()

        return when (flag) {
            UPDATE_FLAG -> {
                val tripId = inputData.getString(ID) ?: return Result.failure()
                val title = inputData.getString(TITLE) ?: return Result.failure()
                val startAt = inputData.getString(START_AT)
                val endAt = inputData.getString(END_AT)

                try {
                    repository.updateTrip(
                        tripId = tripId,
                        title = title,
                        visibility = Visibility.PRIVATE,
                        startDate = startAt,
                        endDate = endAt
                    )
                    Result.success()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: IOException) {
                    Result.retry()
                } catch (e: Exception) {
                    Result.failure()
                }
            }
            DELETE_FLAG -> {
                val tripId = inputData.getString(ID) ?: return Result.failure()

                try {
                    repository.clearPlaceTime(tripPlaceId = tripId)
                    Result.success()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: IOException) {
                    Result.retry()
                } catch (e: Exception) {
                    Result.failure()
                }
            }
            else -> {
                Result.failure()
            }
        }
    }

    companion object {
        const val ID = "GROUP_ID"
        const val TITLE = "title"
        const val START_AT = "START_AT"
        const val END_AT = "END_AT"
        const val FLAG = "FLAG"
        const val UPDATE_FLAG = "UPDATE_FLAG"
        const val DELETE_FLAG = "DELETE_FLAG"
    }
}