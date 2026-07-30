package net.likelion.bebc25.springboard.config;

import net.likelion.bebc25.springboard.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring Web에 관련된 Bean 설정
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Interceptor를 등록하는 곳
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 여기 좀 더 자료 찾기 필요
        // order - 우선순위
        // member/list(회원 목록 조회할 때) - 여기서 인터셉터를 실행하겠다
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                // member/* - member 아래에 있는 모든 기능들
                .addPathPatterns("/member/*", "/post/write")
                .excludePathPatterns("/member/login", "/member/register","/css/**","/js/**","/*.ico","/error");

    }

}
