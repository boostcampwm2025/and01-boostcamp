import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.root)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.andone.memorip.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Naver Search
        val naverOpenApi = getLocalProperty("NAVER_OPEN_API")
        val naverClientId = getLocalProperty("NAVER_SEARCH_CLIENT_ID")
        val naverClientSecret = getLocalProperty("NAVER_SEARCH_CLIENT_SECRET")
        buildConfigField("String", "NAVER_OPEN_API", "\"$naverOpenApi\"")
        buildConfigField("String", "NAVER_SEARCH_CLIENT_ID", "\"$naverClientId\"")
        buildConfigField("String", "NAVER_SEARCH_CLIENT_SECRET", "\"$naverClientSecret\"")
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
    implementation(libs.converter.gson)
}

fun getLocalProperty(propertyKey: String): String {
    val properties = gradleLocalProperties(rootDir, providers)
    return properties.getProperty(propertyKey) ?: run {
        println("Warning: $propertyKey not found in local.properties")
        ""
    }
}