package com.andone.memorip.common.exception

class BusinessException(
    val code: ApiResponseCode,
    val fields: List<String>? = null
) : RuntimeException(code.message)
