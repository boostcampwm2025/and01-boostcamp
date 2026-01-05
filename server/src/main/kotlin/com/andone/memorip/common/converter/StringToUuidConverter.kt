package com.andone.memorip.common.converter

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class StringToUuidConverter : Converter<String, UUID> {
    
    override fun convert(id: String): UUID {
        return try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw BusinessException(
                code = CommonExceptionCode.INVALID_PARAMETER,
                fields = listOf("UUID 형식이 올바르지 않습니다: $id")
            )
        }
    }
}