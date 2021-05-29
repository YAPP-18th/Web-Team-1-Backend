package com.yapp18.retrospect.security.oauth2;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) { // parameter에 들어오는 name과 일치하는 쿠키를 Optional 타입으로 반환한다
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly, int maxAge) {
        Cookie cookie = new Cookie(name, value); // 패러미터로 들어온 name과 value를 기반으로 새로운 쿠키를 생성 
        cookie.setPath("/");
        cookie.setHttpOnly(httpOnly);
//        cookie.setSecure(true); // secure을 적용하면 https 접속에서만 동작한다.
        cookie.setMaxAge(maxAge); // Expire 설정
        response.addCookie(cookie); // response에 새로운 쿠키 추가
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) { // parameter에 들어오는 name과 일치하는 쿠키를
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // setValue, setPath, setMaxAge를 통해 초기화시켜주고
                    response.addCookie(cookie); // response에 넣는다.
                }
            }
        }
    }
    //쿠키는 단일 값이기 때문에 ByteArrayInput, OuputStream이나 ObjectInput, OutputStream을 사용할 필요가 없다

    //직렬화(Serialize)
    // 자바 시스템 내부에서 사용되는 Object 또는 Data를 외부의 자바 시스템에서도 사용할 수 있도록 byte 형태로 데이터를 변환하는 기술.
    // 또는 JVM(Java Virtual Machine 이하 JVM)의 메모리에 상주(힙 또는 스택)되어 있는 객체 데이터를 바이트 형태로 변환하는 기술
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    //역직렬화(Deserialize)
    //byte로 변환된 Data를 원래대로 Object나 Data로 변환하는 기술을 역직렬화(Deserialize)라고 부릅니다.
    //직렬화된 바이트 형태의 데이터를 객체로 변환해서 JVM으로 상주시키는 형태.
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}