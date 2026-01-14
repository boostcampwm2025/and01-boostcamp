package com.andone.memorip.common.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileInputStream
import org.springframework.beans.factory.annotation.Value

@Configuration
class FirebaseConfig(
    @Value("\${firebase.credentials.path}")
    private val credentialsPath: String
) {

    @PostConstruct
    fun init() {
        if (FirebaseApp.getApps().isNotEmpty()) return

        val file = File(credentialsPath)
            .toPath()
            .toAbsolutePath()
            .normalize()
            .toFile()

        require(file.exists()) {
            "Firebase credentials not found: ${file.absolutePath}"
        }

        val options = FirebaseOptions.builder()
            .setCredentials(
                GoogleCredentials.fromStream(FileInputStream(file))
            )
            .build()

        FirebaseApp.initializeApp(options)
    }
}