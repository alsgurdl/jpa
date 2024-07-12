package com.example.final_project_java.config;

import com.example.final_project_java.filter.JwtAuthFilter;
import com.example.final_project_java.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final RequestProperties properties;

    // 시큐리티 기본 설정 (권한 처리, 초기 로그인 화면 없애기 ....)
    @Bean // 라이브러리 클래스 같은 내가 만들지 않은 객체를 등록해서 주입받기 위한 아노테이션.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // yml에서 가져온 허용 url 리스트를 jwtAuthFilter에게 전달.
        jwtAuthFilter.setPermitAllPatterns(properties.getPermitAllPatterns());
        log.info("리스트: {}", properties.getPermitAllPatterns());
        log.info("배열로 변환: {}", Arrays.toString(properties.getPermitAllPatterns().toArray()));

        http
                .csrf(csrfConfig -> csrfConfig.disable()) // CSRF 토큰공격을 방지하기 위한 장치 해제.
                .cors(Customizer.withDefaults())
                // 세션 관리 상태를 STATELESS로 설정해서 spring security가 제공하는 세션 생성 및 관리 기능 사용하지 않겠다.
                .sessionManagement(SessionManagement ->
                        SessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // spring에서 제공하는 기본 로그인 폼 사용 안함, http 기반 기본 인증도 안 쓰겠다.
                .formLogin(form -> form.disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                // 우리가 만든 jwtAuthFilter를 UsernamePasswordAuthenticationFilter보다 먼저 동작하도록 설정.
                // security를 사용하면, 서버가 가동될 때 기본적으로 제공하는 여러가지 필터가 세팅이 되는데,
                // jwtAuthFilter를 먼저 배치해서, 얘를 통과하면 인증이 완료가 되도록 처리
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Exception filter를 Auth filter 앞에 배치를 하겠다는 뜻.
                // 예외 처리만을 전담하는 필터를 생성새서, 예외가 발생하는 필터 앞단에 배치하면, 발생된 예외가
                // 먼저 배치된 필터로 넘어가서 처리가 가능하게 됩니다.
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(HttpMethod.GET, "/googleLogin").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/kakaoLogin").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/naverLogin").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/car/review").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/car/review/{id}").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/car/review").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/car/{id}").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/car/{id}").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/car").authenticated()
                                        .requestMatchers(HttpMethod.PUT, "/api/auth/update").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/api/auth/delete").authenticated()
                                        .requestMatchers("/api/auth/load-profile").authenticated()
                                        .requestMatchers("/api/auth/logout").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/noti/{id}").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/noti/{id}").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/mypage").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/mypage").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/admin").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/events").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/events/list/{no}").permitAll()
                                        .requestMatchers(HttpMethod.DELETE, "/events/{no}").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/events/{no}").authenticated()
                                        .requestMatchers("/api/auth/load-profile").authenticated()
                                        .requestMatchers("/api/auth/logout").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/review").authenticated()
//                              .requestMatchers(HttpMethod.POST, "/review/charge").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/rentcar/reslist").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/rentcar/reservation").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/rentcar/resInfo/user/{userId}").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/rentcar/resInfo/car/{carNo}").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/rentcar/delete/{carNo}").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/rentcar/{carNo}").authenticated()
                                        .requestMatchers(HttpMethod.GET,"/rentcar/{carId}").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/noti/info").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/noti/info/{id}").permitAll()
                                        .requestMatchers(HttpMethod.PATCH, "/noti/views/{id}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/car/res").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/car/res/{id}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/events").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/events/{no}").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/auth/googleLogin").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/confirm").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/review/list").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/review/list/{no}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/rentcar/{carId}").permitAll()

                                        .requestMatchers(Arrays.toString(properties.getPermitAllPatterns().toArray()).split(", "))
                                        .permitAll()

                                        // 위에서 따로 설정하지 않은 나머지 요청들은 권한 검사가 필요하다.
                                        .anyRequest().authenticated()
                )
                .exceptionHandling(ExceptionHandling -> {
                    // 인증 과정에서 예외가 발생한 경우 예외를 전달한다. (401)
                    // ExceptionHandling.authenticationEntryPoint(entryPoint);
                    // 인가 과정에서 예외가 발생한 경우 예외를 전달한다. (403)
                    ExceptionHandling.accessDeniedHandler(accessDeniedHandler);
                });

        return http.build();
    }

    // 비밀번호 암호화 객체를 빈 등록
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}












































