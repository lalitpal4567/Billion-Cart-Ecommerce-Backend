package com.billioncart.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class CorsConfig {
//	@Value("cors.allowedOrings")
//	private String allowedOrigins;

	public void addCorsMappings(CorsRegistry registry) {
//		final long MAX_AGE_SECS = 3600;

		registry.addMapping("/**")
//					  .allowedOrigins("*")
					  .allowedOrigins("http://localhost:3000")
					  .allowedMethods("GET", "POST", "PUT", "DELETE")
					  .allowedHeaders("*");
//					  .maxAge(MAX_AGE_SECS);
	}
}
