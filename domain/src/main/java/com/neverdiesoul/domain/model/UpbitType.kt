package com.neverdiesoul.domain.model

/**
 * 웹소켓을 통한 실시간 데이터 요청을 위해 presentation->domain->data까지 전달되어야 할 파라미터 객체
 */
data class UpbitType(val type: String, val codes: List<String>)
