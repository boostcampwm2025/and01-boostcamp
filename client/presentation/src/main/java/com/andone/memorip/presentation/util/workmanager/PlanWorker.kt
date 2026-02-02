package com.andone.memorip.presentation.util.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.andone.memorip.domain.repository.TripRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import java.io.IOException

@HiltWorker
class PlanWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: TripRepository
) : CoroutineWorker(applicationContext, params) {

    override suspend fun doWork(): Result {
        val groupPlaceId = inputData.getString(ID) ?: return Result.failure()
        val startAt = inputData.getString(START_AT) ?: return Result.failure()
        val endAt = inputData.getString(END_AT) ?: return Result.failure()

        return try {
            repository.updatePlaceTime(groupPlaceId, startAt, endAt)
            Result.success()
        } catch (e: CancellationException) {
            throw e
        } catch(e: IOException) {
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val ID = "GROUP_PLACE_ID"
        const val START_AT = "START_AT"
        const val END_AT = "END_AT"
    }
}