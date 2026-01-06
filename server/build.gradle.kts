plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	kotlin("plugin.jpa") version "2.2.21"
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.andone"
version = "0.0.1-SNAPSHOT"
description = "memorip project"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Web & Validation
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // JPA, PostgreSQL
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

	// UUID v7
	implementation("com.github.f4b6a3:uuid-creator:6.1.1")

	// Swagger/OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")
	
	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Naver, Storage
	implementation("software.amazon.awssdk:s3:2.25.60")
	implementation("software.amazon.awssdk:auth:2.25.60")
	
	// Dotenv
    val dotenvVersion = "5.1.0"
    implementation(platform("me.paulschwarz:spring-dotenv-bom:$dotenvVersion"))
    developmentOnly("me.paulschwarz:springboot4-dotenv:$dotenvVersion")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
