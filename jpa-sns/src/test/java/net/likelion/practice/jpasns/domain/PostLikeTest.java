package net.likelion.practice.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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
    @Commit
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
        PostLike findPostLike = em.find(PostLike.class, postLikeId);

        assertThat(findPostLike).isNotNull();
        assertThat(findPostLike.getMember().getId()).isEqualTo(memberId);
        assertThat(findPostLike.getPost().getId()).isEqualTo(postId);

        assertThat(findPostLike.getMember().getNickname()).isEqualTo("하루");
        assertThat(findPostLike.getPost().getContent()).isEqualTo("멍멍");
        assertThat(findPostLike.getCreatedAt()).isNotNull();

    }
}
