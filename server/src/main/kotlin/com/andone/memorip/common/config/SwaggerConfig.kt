package com.andone.memorip.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI() = OpenAPI()
        .info(
            Info()
                .title("Memorip API")
                .version("v1")
                .description("여행 장소 기록 및 공유 서비스 API")
        )
}