package com.andone.memorip.domain.user.repository

import com.andone.memorip.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID>