package com.andone.memorip.presentation.util.snackbar

data class SnackBarAction(
    val label: String,
    val onAction: suspend () -> Unit
)

enum class SnackBarEvent(
    val message: String,
    val action: SnackBarAction? = null
) {
    // 공통 에러
    NETWORK_ERROR(message = "네트워크 연결을 확인해주세요"),
    VALIDATION_ERROR(message = "입력값을 확인해주세요"),
    NOT_FOUND_ERROR(message = "찾으시는 화면을 표시할 수 없습니다."),
    DATA_LOAD_FAILED(message = "데이터를 불러오지 못했습니다"),
    DATA_SAVE_FAILED(message = "데이터를 저장하지 못했습니다"),
    IMAGE_UPLOAD_FAILED(message = "이미지 업로드에 실패했습니다"),
    UNKNOWN_ERROR(message = "오류가 발생했습니다. 다시 시도해주세요"),

    // 성공
    SUCCESS(message = "성공적으로 완료되었습니다")
}