package com.andone.memorip.domain.group.dto.request

import com.andone.memorip.domain.group.entity.Visibility

data class GroupCreateRequest(
    val title: String,
    val visibility: Visibility = Visibility.PRIVATE
)