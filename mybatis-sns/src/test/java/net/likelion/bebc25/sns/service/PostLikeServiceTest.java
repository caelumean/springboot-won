package net.likelion.bebc25.sns.service;

import net.likelion.bebc25.sns.dto.LikeToggleResponse;
import net.likelion.bebc25.sns.dto.PostResponse;
import net.likelion.bebc25.sns.mapper.PostLikeMapper;
import net.likelion.bebc25.sns.mapper.PostMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

//@MybatisTest도 가능 SpringBootTest쓰면 Transactional도 써야한다.
@SpringBootTest
@Transactional
public class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

//    @Autowired
    // 실제 PostMapper의 동작은 유지되지만 필요하다면 특정 메서드만 모킹하여 강제 예외를 발생시킬 수 있다.
    @MockitoSpyBean
    private PostMapper postMapper;

    @Autowired
    private PostLikeMapper postLikeMapper;

    //@RepeatedTest(2)    // 2번 테스트해라
    @Test
    @DisplayName("좋아요 토글 등록, 카운트 수 변경 테스트")
    void toggleLikeAddTest(){
        // given: 1번 회원이 2번 게시글에 좋아요 시도 (미등록 상태)
        Long memberId = 1L;
        Long postId = 2L;

        PostResponse beforePost = postMapper.findById((postId));
        int beforeLikeCount = beforePost.likeCount();
        boolean beforeLiked = postLikeMapper.countLike(memberId, postId) > 0;


        // when: 좋아요 토글
        LikeToggleResponse result = postLikeService.toggleLike(memberId,postId);

        // then:
        PostResponse afterPost = postMapper.findById((postId));
        boolean afterLiked = postLikeMapper.countLike(memberId, postId) > 0;
        if(beforeLiked){    // 토글 이전에 좋아요 상태일 경우라면 토글 이후에는 좋아요 상태가 아니고 좋아요 수는 -1이 되어야 함
            assertThat(result.liked()).isFalse();
            assertThat(result.likeCount()).isEqualTo(beforeLikeCount - 1);
            assertThat(afterLiked).isFalse();
            assertThat(afterPost.likeCount()).isEqualTo(beforeLikeCount - 1);
        }else{  // 토글에 이전에 좋아요 상태가 아닌 경우라면 토글 이후에는 좋아요 상태가 되고 좋아요 수는 +1읻 되어야 함
            assertThat(result.liked()).isTrue();
            assertThat(result.likeCount()).isEqualTo(beforeLikeCount + 1);
            assertThat(afterLiked).isTrue();
            assertThat(afterPost.likeCount()).isEqualTo(beforeLikeCount + 1);


        }
    }
    @Test
    @DisplayName("좋아요 토글, 카운트 수 변경 중 예외 발생시 롤백 테스트")
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // 트랜잭션 사용안할것이다.
    void toggleLikeRollbackTest(){
        // given: 1번 회원이 2번 게시글에 좋아요 시도 (미등록 상태)
        Long memberId = 1L;
        Long postId = 2L;
        boolean beforeLiked = postLikeMapper.countLike(memberId, postId) > 0;

        // 좋아요가 되어 있다면 삭제하고 시작
        postLikeMapper.deleteLike(memberId,postId);

//        postMapper.increaseLikeCount(postId);   // 정상 실행

        // 네트워크 장애가 발생했을 경우에 이런 동작을 하는 테스트
        doThrow(new RuntimeException("데이터베이스 네트워크 장애 발생"))
                .when(postMapper).increaseLikeCount(postId);

        // postMapper.increaseLikeCount(postId); // 내부적으로 예외가 발생한다.

        // when: 좋아요 토글
        // toggleLike에서 예외가 발생
        // RunTime Exception이 발생하기를 기대한다.
        assertThatThrownBy(() -> postLikeService.toggleLike(memberId,postId)).isInstanceOf(RuntimeException.class);
//        LikeToggleResponseDto result = postLikeService.toggleLike(memberId,postId);

        boolean liked = postLikeMapper.countLike(memberId, postId) > 0;
        assertThat(liked).isFalse();

    }

}
