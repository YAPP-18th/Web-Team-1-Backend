package com.yapp18.retrospect.config;

import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.security.JwtAccessDeniedHandler;
import com.yapp18.retrospect.security.JwtAuthenticationEntryPoint;
import com.yapp18.retrospect.security.TokenAuthenticationFilter;
import com.yapp18.retrospect.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.yapp18.retrospect.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.yapp18.retrospect.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.yapp18.retrospect.service.CustomOAuth2UserService;
import com.yapp18.retrospect.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정들을 활성화시켜 주며, 모든 엔드포인트에 접근 제한이 걸리게 됩니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomUserDetailsService customUserDetailsService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final AppProperties appProperties;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // JWT를 사용하기 때문에 Session에 저장할 필요가 없어져, Authorization Request를 Based64 encoded cookie에 저장

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Map<String, List<String>> allUrls = appProperties.getValues().getAllUrls();
        Map<String, List<String>> memberUrls = appProperties.getValues().getMemberUrls();
        Map<String, List<String>> adminUrls = appProperties.getValues().getAdminUrls();
        Map<String, List<String>> anonymousUrls = appProperties.getValues().getAnonymousUrls();

        http.cors().configurationSource(corsConfigurationSource())
                .and()
                // 토큰을 사용하기 위해 sessionCreationPolicy를 STATELESS로 설정 (Session 비활성화)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .csrf().disable()
                    .exceptionHandling()
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                    .headers().frameOptions().disable() //h2-console 화면을 사용하기 위해 해당 옵션들을 disable 합니다.
                .and()
                    .authorizeRequests() // URL 별 권한 관리를 설정하는 옵션의 시작점입니다. authorizeRequests가 선언되어야만 antMatchers 옵션을 사용할 수 있습니다.
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // preflight는 인증하지 않고 pass(권한이 모두 null로 들어오기 때문에)

                    .antMatchers(HttpMethod.GET, allUrls.get("GET").toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.POST, allUrls.get("POST").toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.OPTIONS, allUrls.get("OPTIONS").toArray(new String[0])).permitAll()

                    .antMatchers(HttpMethod.GET, memberUrls.get("GET").toArray(new String[0])).hasRole(Role.MEMBER.name())
                    .antMatchers(HttpMethod.POST, memberUrls.get("POST").toArray(new String[0])).hasRole(Role.MEMBER.name())
                    .antMatchers(HttpMethod.PUT, memberUrls.get("PUT").toArray(new String[0])).hasRole(Role.MEMBER.name())
                    .antMatchers(HttpMethod.DELETE, memberUrls.get("DELETE").toArray(new String[0])).hasRole(Role.MEMBER.name())
                    .antMatchers(HttpMethod.PATCH, memberUrls.get("PATCH").toArray(new String[0])).hasRole(Role.MEMBER.name())

                    .antMatchers(HttpMethod.GET, anonymousUrls.get("GET").toArray(new String[0])).anonymous()

                    .anyRequest().authenticated() // 나머지 URL들은 모두 인증된 사용자들에게만 허용하게 합니다. (즉 로그인한 사용자들에게만 허용)
                .and()
                    .logout()
                    .logoutSuccessUrl(appProperties.getOauth2().getRedirectUri()) //로그아웃 기능에 대한 여러 설정의 진입점입니다. 성공시 / 주소로 이동합니다.
                .and()
                    .oauth2Login()//OAuth2 로그인 기능에 대한 여러 설정의 진입점입니다. Spring security에서 제공하는 oauth2Login 메소드를 이용하여 로그인 코드를 가져오도록 합니다.
                        .authorizationEndpoint()
                        .baseUri("/api/v1/oauth2")
                        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                    .userInfoEndpoint()//OAuth2 로그인 성공 이후 사용자 정보를 가져올 떄의 설정들을 담당합니다.
                        .userService(customOAuth2UserService) //소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록합니다.
                .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                // UsernamePasswordAuthenticationFilter 앞에 custom 필터 추가!
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있습니다.
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","OPTIONS","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(5300L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
