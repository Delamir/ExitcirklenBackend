package kea.gruppe1.exitcirklenbackend.config;

import com.azure.spring.cloud.autoconfigure.aad.AadResourceServerHttpSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
@EnableWebSecurity
@EnableMethodSecurity
public class AadOAuth2LoginSecurityConfig {

    /**
     * Add configuration logic as needed.
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());
        return http.build();
    }


    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.cors().configurationSource(corsConfigurationSource());
        http.authorizeRequests((auth) -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        // Do some custom configuration.
    }
*/
    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*", "**"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}