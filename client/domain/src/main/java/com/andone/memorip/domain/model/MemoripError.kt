package com.andone.memorip.domain.model

abstract class MemoripError(message: String?): Throwable(message)

sealed class NetworkError(message: String?): MemoripError(message) {
    data class InvalidRequestError(override val message: String?): NetworkError(message)
    data class NotFoundError(override val message: String?): NetworkError(message)
    data class InternalServerError(override val message: String?): NetworkError(message)
    data class UnknownError(override val message: String?): NetworkError(message)
}

sealed class UserError(override val message: String?): MemoripError(message) {

}

class UnexpectedError(override val message: String?): MemoripError(message)