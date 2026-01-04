package com.andone.memorip.domain.repository

import com.andone.memorip.domain.model.Location

interface NaverSearchRepository {
    suspend fun searchLocations(query: String): Result<List<Location>>
}