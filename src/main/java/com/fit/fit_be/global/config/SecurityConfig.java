package com.fit.fit_be.global.config;

import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.auth.PrincipalDetails;
import com.fit.fit_be.global.auth.jwt.JwtFilter;
import com.fit.fit_be.global.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        //.requestMatchers("/boards/**").authenticated()
                        .requestMatchers("/members/my-profile").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
                            String accessToken = jwtTokenProvider.createToken(userDetails.getMemberId());
                            String refreshToken = jwtTokenProvider.createRefreshToken(userDetails.getMemberId());
                            response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
                            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                                    .path("/")
                                    .sameSite("None")
                                    .httpOnly(false)
                                    .secure(true)
                                    .maxAge(60 * 60 * 24)
                                    .build();

                            response.addHeader("Set-Cookie", cookie.toString());

                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Login failed");
                            response.getWriter().flush();
                        }))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Unauthorized: " + authException.getMessage());
                            response.getWriter().flush();
                        }))
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .addLogoutHandler((request, response, authentication) -> {
                            Member member = (Member) authentication.getPrincipal();
                            jwtTokenProvider.deleteRefreshToken(member.getId());
                            ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                                    .path("/")
                                    .maxAge(0)
                                    .build();
                            response.addHeader("Set-Cookie", cookie.toString());
                        }))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, LogoutFilter.class)
                .cors(cors -> cors
                        .configurationSource(configurationSource()))
                .build();

    }
}
