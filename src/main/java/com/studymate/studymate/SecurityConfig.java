package com.studymate.studymate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 필드 주입을 위해 필요합니다.
public class SecurityConfig {

    // CustomUserDetailsService를 주입받습니다.
    private final UserDetailsService userDetailsService;

    // 1. 비밀번호 인코더 빈 등록 (회원가입/로그인 시 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager 메서드는 인자로 받습니다.
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder) throws Exception {

        // AuthenticationManagerBuilder 객체를 얻습니다.
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // UserDetailsService와 PasswordEncoder를 설정합니다.
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        // 빌더의 build()를 호출합니다.
        return authenticationManagerBuilder.build();
    }

    // 3. HTTP 보안 설정 정의
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 및 CSRF 보호 비활성화 (API 테스트와 개발 편의를 위해)
                .csrf(csrf -> csrf.disable())

                // 요청에 대한 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/user", "/api/board").permitAll() // 해당 페이지 API는 모두 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증(로그인)이 필요
                )

                // 폼 로그인 설정 (우리가 만든 /login 페이지 사용)
                .formLogin(form -> form
                        .loginPage("/login") // 사용할 로그인 페이지 주소
                        .loginProcessingUrl("/login") // 로그인 폼이 데이터를 전송할 주소

                        // 폼에서 사용자명을 'email' 필드 이름으로 받겠다고 명시합니다.
                        .usernameParameter("email")
                        // 폼에서 비밀번호를 'password' 필드 이름으로 받겠다고 명시합니다.
                        .passwordParameter("password")

                        .defaultSuccessUrl("/", true) // 로그인 성공 시 루트 페이지('/')로 이동!
                        .failureUrl("/login?error") // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 요청 주소
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 이동할 페이지
                        .permitAll()
                );

        return http.build();
    }
}
