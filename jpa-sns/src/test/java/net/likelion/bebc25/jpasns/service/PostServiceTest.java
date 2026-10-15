package net.likelion.bebc25.jpasns.service;

import net.likelion.bebc25.jpasns.domain.Member;
import net.likelion.bebc25.jpasns.dto.CommentDto;
import net.likelion.bebc25.jpasns.dto.PostDto;
import net.likelion.bebc25.jpasns.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private PostService postService;

    // MemberService로 주입받아서 하면 Service가 실패하면 Service를 계속 수정해야하기 때문에
    // 더 로우 레벨의 Repository로 하면 된다.
    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;

    @BeforeEach
    void setUp(){
        Member member = new Member("haru@lion.com","1234","하루");
        memberRepository.save(member);
        memberId = member.getId();
    }

    @Test
    @DisplayName("게시글 작성 및 댓글 등록")
    void createPostAndComments(){
        PostDto.CreateRequest postRequest = new PostDto.CreateRequest("멍멍");
        PostDto.Response postResponse = postService.createPost(memberId, postRequest);

        postService.addComment(postResponse.id(), memberId, new CommentDto.CreateRequest("왈왈"));
        postService.addComment(postResponse.id(), memberId, new CommentDto.CreateRequest("야옹"));

        PostDto.DetailResponse response = postService.getPost(postResponse.id());

        // 게시글 내용 비교
        assertThat(response.content()).isEqualTo("멍멍");

        // 작성자 닉네임 비교
        assertThat(response.authorNickname()).isEqualTo("하루");

        // 코멘트가 2개인지 확인
        assertThat(response.comments()).hasSize(2);

        // 첫번째 코멘트의 id가 1인지 확인
        assertThat(response.comments().get(0).id()).isEqualTo(1);

        // 두번째 코멘트의 내용이 야옹인지 확인
        assertThat(response.comments().get(1).content()).isEqualTo("야옹");

    }
}
