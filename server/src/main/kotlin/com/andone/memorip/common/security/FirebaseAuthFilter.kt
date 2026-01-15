package com.andone.memorip.common.security

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class FirebaseAuthFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (header != null && header.startsWith("Bearer ")) {
            val idToken = header.substring(7)

            try {
                val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
                request.setAttribute("uid", decodedToken.uid)

            } catch (e: FirebaseAuthException) {
                throw BusinessException(CommonExceptionCode.UNAUTHORIZED)
            }
        }

        filterChain.doFilter(request, response)
    }
}
