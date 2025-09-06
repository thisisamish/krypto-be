package com.groupeight.krypto.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.groupeight.krypto.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String[] WHITELIST = { "/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**",
			"/swagger-ui.html" };

	private final UserDetailsServiceImpl userDetailsService;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:4200"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN", "X-Requested-With", "Accept"));
		config.setAllowCredentials(true);
		config.setExposedHeaders(List.of("Set-Cookie"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> {
		}).csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
				.ignoringRequestMatchers("/api/v1/auth/login", "/api/v1/auth/logout"))
				.authorizeHttpRequests(auth -> auth.requestMatchers(WHITELIST).permitAll()
						.requestMatchers("/api/v1/auth/login", "/api/v1/auth/register").permitAll()
						.requestMatchers("/api/v1/admin/**").hasRole("ADMIN").requestMatchers("/api/v1/products/**")
						.hasAnyRole("ADMIN", "CUSTOMER").anyRequest().authenticated())
				.formLogin(form -> form.loginProcessingUrl("/api/v1/auth/login").successHandler((req, resp, auth) -> {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.setContentType("application/json");

					Object principal = auth.getPrincipal();
					String role = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
							.collect(Collectors.joining());

					String jsonResp = String.format(
							"{\"message\":\"Login successful\", \"username\":\"%s\",\"role\":\"%s\"}", auth.getName(),
							role);
					resp.getWriter().write(jsonResp);
					resp.getWriter().flush();
				}).failureHandler((req, resp, ex) -> {
					resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					resp.setContentType("application/json");

					String errorMessage = "Authentication failed. Please check your credentials.";
					if (ex instanceof org.springframework.security.authentication.BadCredentialsException) {
						errorMessage = "Invalid username or password.";
					} else if (ex instanceof org.springframework.security.authentication.DisabledException) {
						errorMessage = "User account is disabled.";
					}
					resp.getWriter().write("{\"error\":\"" + errorMessage + "\"}");
				}))
				.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.logout(logout -> logout.logoutUrl("/api/v1/auth/logout").logoutSuccessHandler((req, resp, auth) -> {
					resp.setStatus(HttpServletResponse.SC_OK);
				})).headers(headers -> headers.frameOptions().disable());

		http.addFilterAfter(new com.groupeight.krypto.security.CsrfCookieFilter(), CsrfFilter.class);

		return http.build();
	}
}