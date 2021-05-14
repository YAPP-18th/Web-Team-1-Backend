package com.yapp18.retrospect.config;

import com.yapp18.retrospect.domain.user.Role;
import com.yapp18.retrospect.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정들을 활성화시켜 주며, 모든 엔드포인트에 접근 제한이 걸리게 됩니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final OauthService OAuthService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 여기에 EndPoint의 접근 제한을 커스텀할 수 있음.
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2-console 화면을 사용하기 위해 해당 옵션들을 disable 합니다.
                .and()
                .authorizeRequests() //URL 별 권한 관리를 설정하는 옵션의 시작점입니다. authorizeRequests가 선언되어야만 antMatchers 옵션을 사용할 수 있습니다.
                .antMatchers("/", "/css/**", "/image/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.MEMBER.name())
                /*권한 관리 대상을 지정하는 옵션입니다. URL, HTTP 메소드 별로 관리가 가능합니다.
                "/"등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한을 주었고,
                "/api/v1/**" 주소를 가진 API는 MEMBER 권한을 가진 사람만 가능하도록 했습니다.*/

                .anyRequest().authenticated() //설정된 값들 이외 나머지 URL들을 나타냅니다.
                //여기서는 authenticated()를 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용하게 합니다. (즉 로그인한 사용자들에게만 허용)
                .and()
                .logout()
                .logoutSuccessUrl("/") //로그아웃 기능에 대한 여러 설정의 진입점입니다. 성공시 / 주소로 이동합니다.
                .and()
                .oauth2Login()//OAuth2 로그인 기능에 대한 여러 설정의 진입점입니다. Spring security에서 제공하는 oauth2Login 메소드를 이용하여 로그인 코드를 가져오도록 합니다.
//                .and() // 인증이 진행되지 않은 상태에서 페이지에 접근할 경우,
//                .exceptionHandling()  자동으로 sign 페이지로 리다이렉트 되도록 authenticationEntryPoint 메소드에 URL을 /signin으로 맞춰줍니다.
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/signin")) // 현재 사용 불가
                .userInfoEndpoint()//OAuth2 로그인 성공 이후 사용자 정보를 가져올 떄의 설정들을 담당합니다.
                .userService(OAuthService); //소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록합니다.
        //리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있습니다.
    }
}
