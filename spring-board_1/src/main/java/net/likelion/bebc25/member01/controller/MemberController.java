package net.likelion.bebc25.member01.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/01/member")
public class MemberController {

    @GetMapping("/login.html")
    public String login(){
        return "member/login";
    }

}
