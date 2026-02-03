package com.andone.memorip.feature.place.service

import com.andone.memorip.infra.storage.StorageService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PlaceImageService(private val storageService: StorageService) {
    private val MAX_FILE_SIZE = 5 * 1024 * 1024

    fun upload(file: MultipartFile): String {

        require(file.size <= MAX_FILE_SIZE) {
            "이미지 용량은 5MB를 초과할 수 없습니다."
        }

        return storageService.upload(
            file = file,
            dir = "place"
        )
    }
}