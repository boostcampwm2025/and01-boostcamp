package com.andone.memorip.domain.model

/**
 * 네트워크 연결 상태를 나타내는 sealed interface
 * 
 * 도메인 레벨의 네트워크 상태 추상화로, 플랫폼에 독립적입니다.
 */
sealed interface NetworkStatus {
    /**
     * 네트워크가 정상적으로 연결된 상태
     */
    data object Available : NetworkStatus

    /**
     * 네트워크 연결을 사용할 수 없는 상태
     */
    data object Unavailable : NetworkStatus

    /**
     * 네트워크 신호가 약해지고 있는 상태
     */
    data object Losing : NetworkStatus

    /**
     * 네트워크 연결이 완전히 끊어진 상태
     */
    data object Lost : NetworkStatus
}

