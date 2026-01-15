import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.dagger.hilt.root)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.andone.memorip.presentation"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Naver Map Key 주입
        manifestPlaceholders["NAVER_MAP_NCP_KEY_ID"] = getLocalProperty(propertyKey = "NAVER_MAP_NCP_KEY_ID")

        // client ID
        val webClientId = getLocalProperty("LOGIN_WEB_CLIENT_ID")
        buildConfigField("String", "LOGIN_WEB_CLIENT_ID", "\"$webClientId\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase Analytics & Crashlytics
    implementation(platform(libs.firebase))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Navigation
    implementation(libs.bundles.navigation3)

    // Immutable
    implementation(libs.kotlinx.collections.immutable)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Naver Map
    implementation(libs.naver.map.sdk)
    implementation(libs.naver.map.compose)
    implementation(libs.naver.map.location)

    // Java 8+ API desugaring (java.time 라이브러리 지원)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    
    // Paging
    implementation(libs.androidx.paging.compose)

    // Credential Manager core
    implementation(libs.androidx.credentials)

    // Google 계정 연동용 확장
    implementation(libs.androidx.credentials.play.services.auth)

    // Google Identity (ID Token 발급용)
    implementation(libs.googleid)
}

fun getLocalProperty(propertyKey: String): String {
    val properties = gradleLocalProperties(rootDir, providers)
    return properties.getProperty(propertyKey) ?: run {
        println("Warning: $propertyKey not found in local.properties")
        ""
    }
}