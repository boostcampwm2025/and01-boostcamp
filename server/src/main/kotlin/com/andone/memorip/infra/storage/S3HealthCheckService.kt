package com.andone.memorip.infra.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client

@Service
class S3HealthCheckService(
    private val s3Client: S3Client,
    @Value("\${naver.s3.bucket}") private val bucket: String
) {
    fun check() {
        s3Client.headBucket { it.bucket(bucket) }
    }
}