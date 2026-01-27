package com.andone.memorip.common.exception

import org.springframework.http.HttpStatus

interface ApiResponseCode {
    val status: HttpStatus
    val message: String
}

enum class CommonExceptionCode(
    override val status: HttpStatus,
    override val message: String
) : ApiResponseCode {

    // 공통에러
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "필수 필드가 누락되었습니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에 문제가 발생했습니다. 다시 시도해주세요."),

    // 도메인 별로
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "장소를 찾을 수 없습니다."),

    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    GROUP_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 그룹에 대한 권한이 없습니다."),
    GROUP_CANNOT_DELETE_DEFAULT(HttpStatus.BAD_REQUEST, "기본 그룹은 삭제할 수 없습니다."),
    TAG_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 태그명입니다")
}