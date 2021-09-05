package com.sara.security.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * This configuration class is used Configures our application with Spring Security 
 * to restrict access to our API end points.
 *
 */
@EnableWebFluxSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig {

	private final String returnTo;
	private final String logoutBaseUrl;
	private final String clientId;
	private final String issuerUrl;


    public SecurityConfig(@Value("${spring.security.oauth2.client.registration.auth0.client-id}") String clientId,
                          @Value("${auth0.logout.url}") String logoutBaseUrl,
                          @Value("${return.to.base.url}") String returnTo,
                          @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}") String issuerUrl) {
        this.clientId = clientId;
        this.logoutBaseUrl = logoutBaseUrl;
        this.returnTo = returnTo;
        this.issuerUrl = issuerUrl;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {
    	
    	http.authorizeExchange()
        .pathMatchers("/login", "/images/**").permitAll()
        .anyExchange().authenticated()
        .and().oauth2Login()
        .and().logout().logoutSuccessHandler(logoutSuccessHandler())
        .and()
        .oauth2ResourceServer()
        .jwt();
    	
    	return http.build();
    	
    }
    
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUrl);
    }


    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
       
        // Build the URL to log the user out of Auth0 and redirect them to the home page.
        // URL will look like https://YOUR-DOMAIN/v2/logout?clientId=YOUR-CLIENT-ID&returnTo=http://localhost:3000
        String logoutUrl = UriComponentsBuilder
                .fromHttpUrl(logoutBaseUrl + "?client_id={clientId}&returnTo={returnTo}")
                .encode()
                .buildAndExpand(clientId, returnTo)
                .toUriString();

        RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
        handler.setLogoutSuccessUrl(URI.create(logoutUrl));
        return handler;
    }
}
