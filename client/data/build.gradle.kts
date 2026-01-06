import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.root)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.andone.memorip.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Kakao Search
        val kakaoBaseUrl = getLocalProperty("KAKAO_BASE_URL")
        val kakaoRestApiKey = getLocalProperty("KAKAO_REST_API_KEY")
        val baseUrl = getLocalProperty("BASE_URL")
        buildConfigField("String", "KAKAO_BASE_URL", "\"$kakaoBaseUrl\"")
        buildConfigField("String", "KAKAO_REST_API_KEY", "\"$kakaoRestApiKey\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Interceptor
    implementation(libs.logging.interceptor)

    // Paging
    implementation(libs.androidx.paging.common)
}

fun getLocalProperty(propertyKey: String): String {
    val properties = gradleLocalProperties(rootDir, providers)
    return properties.getProperty(propertyKey) ?: run {
        println("Warning: $propertyKey not found in local.properties")
        ""
    }
}