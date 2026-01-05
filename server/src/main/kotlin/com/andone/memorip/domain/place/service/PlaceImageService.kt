package com.andone.memorip.domain.place.service

import com.andone.memorip.infra.storage.StorageService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PlaceImageService(
    private val storageService: StorageService
) {

    fun upload(file: MultipartFile): String {
        return storageService.upload(
            file = file,
            dir = "place"
        )
    }
}