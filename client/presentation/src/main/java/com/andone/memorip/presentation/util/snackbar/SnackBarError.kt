package com.andone.memorip.presentation.util.snackbar

import androidx.annotation.StringRes
import com.andone.memorip.presentation.R

data class SnackBarAction(
    @get:StringRes val labelResId: Int,
    val onAction: suspend () -> Unit
)

enum class SnackBarEvent(
    @get:StringRes val messageResId: Int,
    val action: SnackBarAction? = null
) {
    // 공통 에러
    NETWORK_ERROR(messageResId = R.string.snackbar_network_error),
    VALIDATION_ERROR(messageResId = R.string.snackbar_validation_error),
    NOT_FOUND_ERROR(messageResId = R.string.snackbar_not_found_error),
    DATA_LOAD_FAILED(messageResId = R.string.snackbar_data_load_failed),
    DATA_SAVE_FAILED(messageResId = R.string.snackbar_data_save_failed),
    IMAGE_UPLOAD_FAILED(messageResId = R.string.snackbar_image_upload_failed),
    UNKNOWN_ERROR(messageResId = R.string.snackbar_unknown_error),

    // UI Error
    PLAN_DAYS_VALIDATION_ERROR(messageResId = R.string.snackbar_plan_days_validation_error),
    PLAN_INVALID_ERROR(messageResId = R.string.snackbar_plan_invalid_error),
    PLAN_DATE_INVALID_ERROR(messageResId = R.string.snackbar_plan_date_invalid_error),
    IMAGE_COUNT_ERROR(messageResId = R.string.snackbar_image_count_error),

    PLACE_MUST_HAVE_ONE_TRIP(messageResId = R.string.snackbar_place_must_have_one_trip),
    TRIP_NO_PLACES(messageResId = R.string.snackbar_trip_no_places),

    // 성공
    SUCCESS(messageResId = R.string.snackbar_success),

    // Auth Error
    EMAIL_ALREADY_EXISTS(messageResId = R.string.snackbar_email_already_exists),
    INVALID_EMAIL(messageResId = R.string.snackbar_invalid_email),
    WEAK_PASSWORD(messageResId = R.string.snackbar_weak_password),
    USER_NOT_FOUND(messageResId = R.string.snackbar_user_not_found),
    WRONG_PASSWORD(messageResId = R.string.snackbar_wrong_password),
    AUTH_NETWORK_ERROR(messageResId = R.string.snackbar_auth_network_error),
}