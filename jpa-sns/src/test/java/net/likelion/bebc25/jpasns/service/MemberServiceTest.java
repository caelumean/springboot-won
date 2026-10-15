package net.likelion.bebc25.jpasns.service;

import net.likelion.bebc25.jpasns.domain.Role;
import net.likelion.bebc25.jpasns.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 및 회원 조회")
    @Transactional
    void signUpAndGetMember(){
        MemberDto.CreateRequest request = new MemberDto.CreateRequest(
                "haru@lion.com",
                "1234",
                "하루"
        );
        MemberDto.Response response = memberService.signUp(request);

        MemberDto.Response findMember = memberService.getMember(response.id());

        assertThat(findMember).isNotNull();
        assertThat(findMember.nickname()).isEqualTo(request.nickname());
        assertThat(findMember.role()).isEqualTo(Role.USER.name());
    }

    @Test
    @DisplayName("이메일 중복 가입시 예외 발생")
    void duplicateEmailException(){
        MemberDto.CreateRequest request1 = new MemberDto.CreateRequest(
                "haru@lion.com",
                "1234",
                "하루"
        );
        MemberDto.CreateRequest request2 = new MemberDto.CreateRequest(
                "haru@lion.com",
                "5678",
                "나무"
        );

        memberService.signUp(request1);

        // 기대하는 건 이메일 중복가입 시 오류
        assertThatThrownBy(() -> memberService.signUp(request2))
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("사용중");
        memberService.signUp(request2);


    }
}
