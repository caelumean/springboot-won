package net.likelion.bebc25.jpasns.repository;

import net.likelion.bebc25.jpasns.domain.Member;
import net.likelion.bebc25.jpasns.domain.Post;
import net.likelion.bebc25.jpasns.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 직접 정의한 메서드
public interface PostRepository extends JpaRepository<Post,Long>{

    // 특정 회원이 작성한 게시글 목록을 조회(최신 등록 순으로)
    List<Post> findByMemberOrderByCreatedAtDesc(Member member);

    // 전체 게시글 목록을 최신 등록순으로 정렬 조회
    // findByOrderByCreatedAtDesc로도 가능하다.
    // 1순위으로 JPQL 정렬 조건으로 빠진다.
    // 2순위로는 Pageable 정렬 조건으로 빠진다.
    // 그래서 Pagealbe을 쓸 땐 메서드 이름에 OrderBy가 빠져야한다.(중요!!!)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 회원 식별자(PK)를 조건으로 게시글 목록 최신순 조회
    // MeberId는 Post엔티티에 MemeberId가 있기때문에 그걸 가져오기 때문에
    // Member 필드에 있는 Id 속성으로 조회해라라는 뜻으로 언더바를 쓴다.
    Page<Post> findByMember_IdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    // 본문에 키워드 포함 검색 최신 등록순으로 정렬 조회
    Page<Post> findByContentContainingOrderByCreatedAtDesc(String keyword, Pageable pageable);

    // 회원 식별자(PK)를 조건으로 본문에 키워드 포함 검색 최신 등록순으로 정렬 조회
    List<Post> findByMember_IdAndContentContainingOrderByCreatedAtDesc(Long memberId, String keyword);

    // 회원 식별자(PK)를 조건으로 본문에 키워드 포함 검색 최신 등록순으로 정렬 조회
    // Post는 자바의 엔티티이기 때문에 대소문자 정확히 입력해야한다.
    // 마찬가지로 Page를 쓴다면 Query에서 ORDER BY를 쓰지 말아야한다.
    @Query("SELECT p FROM Post p JOIN p.member m WHERE m.id = :memberId AND p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> findByMemberAndKeyword(@Param("memberId") Long memberId, @Param("keyword") String keyword, Pageable pageable);

    // 본문 키워드 포함 검색
    List<Post> findByContentContaining(String keyword);

    // 특정 좋아요 수 이상의 인기 게시글 조회
    List<Post> findByLikeCountGreaterThanEqual(int minLikeCount);

    // 특정 회원의 특정 좋아요 수 이상 게시글 최신순 정렬 조회(조건 누적으로 메서드명 길어진 쿼리 메서드)
    List<Post> findByMember_IdAndLikeCountGreaterThanEqualOrderByCreatedAtDesc(Long memberId, int minLikeCount);

    // 특정 회원의 좋아요 수 이상 게시글 최신순 정렬 조회
    @Query("SELECT p FROM Post p JOIN p.member m WHERE m.id = :memberId AND p.likeCount >= :minLikeCount ORDER BY p.createdAt DESC")
    List<Post> findByMemberAndMinLike(@Param("memberId") Long memberId, @Param("minLikeCount")int minLikeCount);


    // 내부 중첩 클래스는 $를 붙여줘야 JVM에서 인식을 할 수 있다.
    // 되도록이면 중첩클래스로 만들지 말고 클래스로 따로 빼야한다.
    @Query("""
        SELECT new net.likelion.bebc25.jpasns.dto.PostDto$SummaryResponse(
            p.id,
            m.nickname,
            p.content,
            p.likeCount
        )
        FROM Post p
        JOIN p.member m
        WHERE m.id = :memberId          
    """)
    List<PostDto.SummaryResponse> findSummariesByMemberId(@Param("memberId") Long memberId);
}
