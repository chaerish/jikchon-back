package smu.likelion.jikchon.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // 회원가입, 로그인 등 인증이 필요없는 요청에 대해서는 전체허용 (permitAll)
                // 그 외 모든 요청 (any) 에 대해서는 인증 요구
                .authorizeHttpRequests((authorizeRequest) ->
                        authorizeRequest
                                .anyRequest().permitAll()
                )

                // Rest 방식으로 로그인을 할 것이므로 form 로그인 사용 안함
                .formLogin(AbstractHttpConfigurer::disable)

                // logout url 설정. 인증을 위해 사용하는 쿠키인 JSESSIONID를 삭제하여 로그아웃 처리
                .logout((configurer) ->
                        configurer
                                .logoutUrl("/api/users/logout")
                                .deleteCookies("JSESSIONID")
                )

                //인증되지 않은 자원에 접근했을 때
                .exceptionHandling((configurer) ->
                        configurer
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));


        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}