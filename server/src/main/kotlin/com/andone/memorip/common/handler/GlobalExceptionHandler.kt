package com.andone.memorip.common.handler

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.common.response.ApiResult
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException) = 
        ResponseEntity.status(e.code.status)
            .body(ApiResult.error<Nothing>(e.code.toString(), e.code.message, e.fields))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResult<Nothing>> {
        val fields = e.bindingResult.fieldErrors.map { it.field }
        val message = e.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        val code = CommonExceptionCode.INVALID_INPUT
        
        return ResponseEntity.status(code.status)
            .body(ApiResult.error(code.name, message, fields))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(e: HttpMessageNotReadableException): ResponseEntity<ApiResult<Nothing>> {
        val code = CommonExceptionCode.INVALID_INPUT
        logger.error(e.message, e)
        return ResponseEntity.status(code.status)
            .body(ApiResult.error(code.name, "잘못된 요청 형식입니다."))
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ApiResult<Nothing>> {
        val code = CommonExceptionCode.INVALID_PARAMETER
        return ResponseEntity.status(code.status)
            .body(ApiResult.error(code.name, code.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException
    ): ResponseEntity<ApiResult<Nothing>> {
        val code = CommonExceptionCode.INVALID_PARAMETER
        return ResponseEntity.status(code.status)
            .body(ApiResult.error(code.name, e.message ?: code.message))
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<ApiResult<Nothing>> {
        val code = CommonExceptionCode.PLACE_NOT_FOUND
        return ResponseEntity.status(code.status)
            .body(ApiResult.error(code.name, e.message ?: code.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResult<Nothing>> {
        logger.error("예상치 못한 예외 발생", e)

        val errorDetails = listOfNotNull(
            e::class.simpleName,
            e.message
        )
        
        val code = CommonExceptionCode.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(code.status)
            .body(ApiResult.error(code.name, code.message, errorDetails))
    }
}
