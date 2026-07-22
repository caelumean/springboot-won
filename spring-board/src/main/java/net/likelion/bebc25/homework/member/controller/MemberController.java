package net.likelion.bebc25.homework.member.controller;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.homework.member.dto.MemberDto;
import net.likelion.bebc25.homework.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관련 요청(회원 가입, 로그인, 정보 수정, 탈퇴 등)을 처리하여 해당 화면 또는 동작으로 분기하는 컨트롤러 클래스입니다.
 */
@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  /**
   * 생성자를 통해 MemberService 의존성을 주입받습니다.
   *
   * @param memberService 주입받을 MemberService 스프링 빈 객체
   */
  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  /**
   * 전체 회원 목록을 조회하고 회원 목록 정적 화면으로 유도합니다.
   *
   * @param model 화면에 전달할 데이터를 담는 Model 객체
   * @return 회원 목록 화면으로의 redirect 경로
   */
  @GetMapping("/list.html")
  public String getMemberList(Model model) {
    // 실습 영역
    return "member/list";
  }

  /**
   * 회원 가입 양식 화면으로 유도합니다.
   *
   * @return 회원 가입 화면으로의 redirect 경로
   */
  @GetMapping("/register.html")
  public String getRegisterForm(@ModelAttribute("memberForm") MemberDto member) {
    // 실습 영역
    return "member/register";
  }

  /**
   * 신규 회원 가입 요청 데이터를 받아 등록 처리를 수행합니다.
   *
   * @param memberDto 회원 가입 폼 입력 데이터 DTO
   * @return 로그인 화면으로의 redirect 경로
   */
  @PostMapping("/register")
  public String register(@ModelAttribute MemberDto memberDto) {
    // 실습 영역
    return "redirect:/member/login.html";
  }

  /**
   * 로그인 양식 화면으로 유도합니다.
   *
   * @return 로그인 화면으로의 redirect 경로
   */
  @GetMapping("/login.html")
  public String getLoginForm() {
    // 실습 영역
    return "member/login";
  }

  /**
   * 로그인 인증 요청을 처리합니다.
   *
   * @param username 로그인 요청 사용자 아이디(고유 식별 ID)
   * @param password 로그인 요청 사용자 비밀번호
   * @return 회원 목록 화면으로의 redirect 경로
   */
  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password) {
    // 실습 영역
    return "redirect:/member/list.html";
  }

  /**
   * 회원 정보 수정 화면으로 유도합니다.
   *
   * @param id 수정할 회원의 일련번호
   * @param model 화면에 전달할 데이터를 담는 Model 객체
   * @return 회원 정보 수정 화면으로의 redirect 경로
   */
  @GetMapping("/edit.html")
  public String getEditForm(@RequestParam int id, Model model) {
    // 실습 영역
    return "member/edit";
  }

  /**
   * 회원 정보 수정 요청 데이터를 받아 반영 처리를 수행합니다.
   *
   * @param memberDto 수정 요청 데이터 DTO
   * @return 회원 목록 화면으로의 redirect 경로
   */
  @PostMapping("/edit")
  public String edit(@ModelAttribute MemberDto memberDto) {
    // 실습 영역
    return "redirect:/member/list.html";
  }

  /**
   * 회원 탈퇴 요청을 받아 삭제 처리를 수행합니다.
   *
   * @param id 탈퇴할 회원의 일련번호
   * @return 회원 목록 화면으로의 redirect 경로
   */
  @PostMapping("/withdraw")
  public String withdraw(@RequestParam int id) {
    // 실습 영역
    return "redirect:/member/list.html";
  }
}
