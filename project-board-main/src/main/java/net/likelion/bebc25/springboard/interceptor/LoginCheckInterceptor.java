package net.likelion.bebc25.springboard.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    // Controller 실행되기 전에 체크할 것임
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String requestUri = request.getRequestURI();
       HttpSession session = request.getSession(false);

       // 로그인 안된 상태
       if(session == null || session.getAttribute("loginMember") == null){
           log.info("로그인 안된 사용자의 요청: " + requestUri);
           // 미인증 사용자일 경우 로그인 페이지로 리다이렉트 시킴
           response.sendRedirect("/member/login");

           // HandlerInterceptor가 false를 리턴할 경우 컨트롤러 핸드러를 싱핼하지 않음
           return false;
       }

       // HandlerInterceptor가 true를 리턴할 경우 다음 HandlerInterceptor나 컨트롤러 핸들러를 실행함
       return true;
    }
}
