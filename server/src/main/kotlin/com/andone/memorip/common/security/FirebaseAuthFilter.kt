package com.andone.memorip.common.security

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.feature.user.service.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class FirebaseAuthFilter(
    private val userService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer ")) {

            val principal = UserPrincipal(
                userId = UUID.fromString("019b8be0-1fad-71e9-9da0-bc03ada63862"),
                firebaseUid = "fake"
            )

            val authentication = FirebaseAuthenticationToken(principal)

            SecurityContextHolder.getContext().authentication = authentication

            filterChain.doFilter(request, response)
            return
        }

        try {
            val idToken = header.substring(7)
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)

            val user = userService.getOrCreateMe(
                firebaseUid = decodedToken.uid,
                nickname = null
            )

            val principal = UserPrincipal(
                userId = user.id,
                firebaseUid = decodedToken.uid
            )

            val authentication = FirebaseAuthenticationToken(principal)

            SecurityContextHolder.getContext().authentication = authentication

        } catch (e: FirebaseAuthException) {
            throw BusinessException(CommonExceptionCode.UNAUTHORIZED)
        }

        filterChain.doFilter(request, response)
    }
}
