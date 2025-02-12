package com.jose.springboot.backend.eventos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

	@Bean
	PageableHandlerMethodArgumentResolverCustomizer pageableResolverCustomizer() {
		return pageableResolver -> {
            pageableResolver.setOneIndexedParameters(true);
            pageableResolver.setMaxPageSize(20);
		};
	}
}
