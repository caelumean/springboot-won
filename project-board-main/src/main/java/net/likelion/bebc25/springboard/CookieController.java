package net.likelion.bebc25.springboard;

import jakarta.servlet.http.HttpServletResponse;
import net.likelion.bebc25.springboard.member.dto.MemberDto;
import net.likelion.bebc25.springboard.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Controller
@RequestMapping("/cookie")
public class CookieController {

    private MemberService memberService;
    public CookieController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/create")  // http://localhost:8080/cookie/create?memberId=3 응답
    // HttpServletResponse - 응답할때 응답받는 객체
    // HttpServletRequest - 요청할 때 요청하는 객체
    public String createCookie(@RequestParam String memberId, HttpServletResponse response) {
        // cookie 저장
        // Duration.ofHours 1 - 1시간만 쿠키를 유지시키겠다
        // F12 - Application - 왼쪽에 cookies 누르면 확인 가능 (http://localhost:8080/cookie/create?memberId=3 접속)
        // 쿠키의 치명적인 단점 - 다 보임 / 위변조가 다 가능함(개발자 도구에서 수정가능)
        // 중요한 정보는 - 쿠키에 저장하면 안된다. 그래서 서버측에 보관한다
        ResponseCookie memberIdCookie = ResponseCookie.from("memberId",memberId)
                .maxAge(Duration.ofHours(1))
                .path("/")  // 경로 설정
                .httpOnly(true) // 자바스크립트 접근 불가
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, memberIdCookie.toString());
        ResponseCookie usernameCookie = ResponseCookie.from("username","haru")
                .maxAge(Duration.ofHours(1))
                .path("/")  // 경로 설정
                .httpOnly(true) // 자바스크립트 접근 불가
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, memberIdCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, usernameCookie.toString());

        return "redirect:/";    // view 엔진
    }

    @GetMapping("/view")    // http://localhost:8080/cookie/view 응답
    @ResponseBody
    public String viewCookie(@CookieValue(name = "memberId", required = false) Integer memberId,
                             @CookieValue(name = "username", required = false) String username) {

        if(memberId == null){
            return "<p>memberId 쿠키가 없습니다. </p>";
        }
        MemberDto memberInfo = memberService.getMember(memberId);
        return """
                <ul>
                    <li>이름: %s</li>
                    <li>번호: %s</li>
                    <li>권한: %s</li>
                </ul>
                """.formatted(memberInfo.getId(),memberInfo.getUsername(),memberInfo.getRole());

    }
    @GetMapping("/delete")  // http://localhost:8080/cookie/delete 응답
    public String deleteCookie() {
        return "redirect:/";

    }
}
