package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PostLikeTest {

    @Autowired
    private EntityManager em;

    private Long memberId;
    private Long postId;

    @BeforeEach
    void setUp(){
        Member member = new Member("haru@test.com", "1234","하루");
        em.persist(member);

        this.memberId = member.getId();

        Post post = new Post(member, "멍멍");
        em.persist(post);
        this.postId = post.getId();

        em.flush();
        em.clear();

    }

    // 자기가 자기 글에 좋아요 한 것
    @Test
//    @Commit
    @DisplayName("좋아요 등록과 조회")
    void postLikeSaveAndFind(){

        // 1. 회원, 게시글 조회
        Member findMember =  em.find(Member.class, memberId);
        Post findPost = em.find(Post.class, postId);

        PostLike postLike = new PostLike(findPost, findMember);
        em.persist(postLike);

        Long postLikeId = postLike.getId();

        em.flush();
        em.clear();

        // 좋아요 조회 후 작성자 정보, 게시글 정보가 등록한 정보와 일치하는지 검증
        PostLike findLike = em.find(PostLike.class, postLikeId);

        // 작성자 정보가 등록한 정보와 일치하는 지 검증
        assertThat(findLike).isNotNull();
        assertThat(findLike.getMember().getId()).isEqualTo(findMember.getId());
        assertThat(findLike.getMember().getNickname()).isEqualTo(findMember.getNickname());

        // 게시글 정보가 등록한 정보와 일치하는 지 검증
        assertThat(findLike.getPost().getId()).isEqualTo(postId);
        assertThat(findLike.getPost().getContent()).isEqualTo("멍멍");
        assertThat(findLike.getPost().getMember().getId()).isEqualTo(memberId);
        assertThat(findLike.getPost().getMember().getNickname()).isEqualTo("하루");

        // 생성일시, 수정일시가 존재하는지 검증
        assertThat(findLike.getCreatedAt()).isNotNull();
        assertThat(findLike.getUpdatedAt()).isNotNull();

    }

    @Test
    @DisplayName("동일 회원의 동일 게시글에 대한 좋아요 중복")
    void duplicatePostLikeThrowException(){
        // 1. 회원, 게시글 조회
        Member findMember =  em.find(Member.class, memberId);
        Post findPost = em.find(Post.class, postId);

        // 2. 좋아요 생성, 등록
        PostLike postLike = new PostLike(findPost, findMember);
        em.persist(postLike);

        em.flush();

        // 3. 동일한 사용자가 동일한 게시글에 대한 좋아요 생성
        PostLike duplicatepostLike = new PostLike(findPost, findMember);

        // 4. 좋아요 등록(유니크 제약 위배 예외 발생 검증)
        assertThatThrownBy(() ->{
            em.persist(duplicatepostLike);
            em.flush();
        }).isInstanceOf(Exception.class);

    }
}
