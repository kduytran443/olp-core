package com.kduytran.olpcommon.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor userContextInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getTokenValue();
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwt));
                addHandleForRequestTemplate(requestTemplate);
            }
        };
    }

    public void addHandleForRequestTemplate(RequestTemplate requestTemplate) {
        // Default - No additional things
    }
}
