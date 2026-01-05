package com.andone.memorip.common.config

import com.andone.memorip.common.converter.StringToUuidConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val stringToUuidConverter: StringToUuidConverter
) : WebMvcConfigurer {
    
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(stringToUuidConverter)
    }
}