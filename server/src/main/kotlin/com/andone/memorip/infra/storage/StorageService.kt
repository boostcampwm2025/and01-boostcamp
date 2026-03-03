package com.andone.memorip.infra.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.UUID

@Service
class StorageService(
    private val s3Client: S3Client,
    @Value("\${naver.s3.bucket}") private val bucket: String
) {
    fun check() {
        s3Client.headBucket { it.bucket(bucket) }
    }

    fun upload(
        file: MultipartFile,
        dir: String
    ): String {

        require(value = !file.isEmpty) { "빈 파일입니다" }

        val extension = file.originalFilename
            ?.substringAfterLast('.', "")
            ?.lowercase()
            ?: throw IllegalArgumentException("파일 확장자가 없습니다")

        val key = "$dir/${UUID.randomUUID()}.$extension"

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.contentType)
                .contentLength(file.size)
                .build(),
            RequestBody.fromInputStream(file.inputStream, file.size)
        )

        return s3Client.utilities()
            .getUrl { it.bucket(bucket).key(key) }
            .toExternalForm()
    }
}