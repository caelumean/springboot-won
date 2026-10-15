package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class MemberPostTest {
    // Entity 테스르를 할 때에는 EntityManager를 주입받아서 테스트를 수행한다.
    @Autowired
    private EntityManager em;

    private Long newMemberId;

    @BeforeEach // 사전에 실행될 것들
    void setUp(){
        // 회원등록
        Member member = new Member("test1@test.com", "1234","테스터1");
        // DB에 member를 저장해라
        // persist 지속하다
        em.persist(member);

        newMemberId = member.getId();

        // 실제 쿼리 실행, DB와 동기화
        em.flush();
        // 캐시 삭제
        em.clear();

    }
    // 멤버 하나 찾고 조회
    @Test
    @DisplayName("회원 조회")
    void memberFind(){
        // 멤버 객체를 찾아라 select
        Member findMember = em.find(Member.class, newMemberId);

        assertThat(findMember).isNotNull();
        assertThat(findMember.getEmail()).isEqualTo("test1@test.com");
        assertThat(findMember.getRole()).isEqualTo(Role.USER);
        assertThat(findMember.getId()).isEqualTo(newMemberId);

    }

    // 게시글 하나 찾고 조회
    @Test
    @DisplayName("게시글 등록 및 조회")
    void postSaveAndFind(){

        // 1. 게시글 등록
        // member select
        Member member = em.find(Member.class, newMemberId);
        // post insert
        Post post = new Post(member,"첫번째 게시글");

        em.persist(post);

        em.flush();
        em.clear();

        // 2. 등록한 게시글 조회
        // post select
        Post findPost = em.find(Post.class, post.getId());
        assertThat(findPost).isNotNull();
        assertThat(findPost.getContent()).isEqualTo("첫번째 게시글");
        assertThat(findPost.getMember().getId()).isEqualTo(newMemberId);

        // 실제 member 테이블에서 select를 수행한 후 Member 객체로 주입
        assertThat(findPost.getMember().getNickname()).isEqualTo("테스터1");
        assertThat(findPost.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("임베디드 주소 값 타입 매핑 확인")
    void memberDetailAddressMapping(){
        Address address = new Address("12345","서울시 강남구 역삼동1","101-202");
        Member member = em.find(Member.class, newMemberId);
        MemberDetail memberDetail = new MemberDetail(member, "안녕하세요", address);

        // 해당 데이터가 있으면 수정 없으면 insert
        em.persist(memberDetail);

        em.flush();
        em.clear();

        MemberDetail findMember = em.find(MemberDetail.class, memberDetail.getId());

        assertThat(findMember).isNotNull();
        assertThat(findMember.getAddress().getZipcode()).isEqualTo("12345");
        assertThat(findMember.getAddress().getRoadAddress()).isEqualTo("서울시 강남구 역삼동1");
        assertThat(findMember.getAddress().getDetailAddress()).isEqualTo("101-202");
    }

    @Test
    @DisplayName("BaseTimeEntity 상속 동작 확인")
//    @Commit
    void baseTimeAuditing(){
        Member findMember = em.find(Member.class, newMemberId);

        LocalDateTime createdAt = findMember.getCreatedAt();    // 등록일
        LocalDateTime updatedAt = findMember.getCreatedAt();    // 수정일

        assertThat(findMember.getCreatedAt()).isNotNull();  // 등록일 주입 여부
        assertThat(findMember.getUpdatedAt()).isNotNull();  // 수정일 주입 여부
        assertThat(createdAt).isEqualTo(updatedAt);         // 등록일과 수정일 같은지 여부

        findMember.changeNickname("테스터2");  //  update 쿼리 생성(버퍼)

        em.flush(); // update 실행
        em.clear();

        Member updatedMember = em.find(Member.class, newMemberId);

        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getNickname()).isEqualTo("테스터2"); // 수정된 닉네임 확인
        assertThat(updatedMember.getCreatedAt()).isEqualTo(createdAt);      // 등록일 수정되지 않은
        assertThat(updatedMember.getUpdatedAt()).isNotEqualTo(updatedAt);   // 수정일은 수정됨

    }

    @Test
    @DisplayName("회원 상세 정보 매핑(1:1)")
    void memberDetailMapping(){
        // 회원 조회
        Member member = em.find(Member.class, newMemberId);

        // 회원 상세정보 등록
        Address address = new Address("12345", "서울시 강남구", "역삼동");
        MemberDetail memberDetail = new MemberDetail(member, "반갑습니다.", address);

        em.persist(memberDetail);

        em.flush();
        em.clear();

        // 상세정보를 제외한 회원 정보만 조회
        Member onlyMember = em.find(Member.class, newMemberId);
        assertThat(onlyMember).isNotNull();
        assertThat(onlyMember.getNickname()).isEqualTo("테스터1");

        // 상세정보를 포함한 회원 정보 조회
        MemberDetail withMemberDetail = em.find(MemberDetail.class, newMemberId);
        assertThat(withMemberDetail).isNotNull();
        assertThat(withMemberDetail.getMember().getNickname()).isEqualTo("테스터1");
        assertThat(withMemberDetail.getIntroduction()).isEqualTo("반갑습니다.");
        assertThat(withMemberDetail.getAddress().getDetailAddress()).isEqualTo("역삼동");
    }

    @Test
    @DisplayName("게시글-태그 연결 엔티티 등록 및 다대일 객체 탐색 검증")
    void postHashtagSaveAndGraphTraversal() {
        // 1. 작성자 회원 및 게시글 생성
        Member member = em.find(Member.class, newMemberId);
        Post post = new Post(member, "해시태그 테스트 게시글");
        em.persist(post);

        // 2. 독립 Tag 엔티티 생성 및 영속화
        Tag springTag = new Tag("SpringBoot");
        Tag jpaTag = new Tag("JPA");
        em.persist(springTag);
        em.persist(jpaTag);

        // 3. 다대다 연결 엔티티(PostHashtag) 생성 및 영속화
        PostHashtag postHashtag1 = new PostHashtag(post, springTag);
        PostHashtag postHashtag2 = new PostHashtag(post, jpaTag);
        em.persist(postHashtag1);
        em.persist(postHashtag2);

        em.flush();
        em.clear();

        // 4. 연결 엔티티 조회를 통한 다대일 단방향 객체 탐색 검증
        PostHashtag findHashtag = em.find(PostHashtag.class, postHashtag1.getId());
        assertThat(findHashtag).isNotNull();
        assertThat(findHashtag.getPost().getId()).isEqualTo(post.getId());
        assertThat(findHashtag.getPost().getContent()).isEqualTo("해시태그 테스트 게시글");
        assertThat(findHashtag.getTag().getId()).isEqualTo(springTag.getId());
        assertThat(findHashtag.getTag().getName()).isEqualTo("SpringBoot");
    }

    @Test
    @DisplayName("동일 게시글에 동일 태그 중복 연결 시 복합 유니크 제약조건 위반 검증")
    void duplicatePostHashtagThrowsException() {
        // 1. 게시글 및 태그 생성
        Member member = em.find(Member.class, newMemberId);
        Post post = new Post(member, "중복 태그 테스트 게시글");
        em.persist(post);

        Tag tag = new Tag("Java");
        em.persist(tag);

        // 2. 첫 번째 해시태그 연결 등록
        PostHashtag firstHashtag = new PostHashtag(post, tag);
        em.persist(firstHashtag);
        em.flush();

        // 3. 동일한 게시글에 동일한 태그로 두 번째 연결 등록 시도 시 예외 발생 검증
        PostHashtag duplicateHashtag = new PostHashtag(post, tag);

        // IDENTITY 전략은 PK 채번을 위해 em.persist() 호출 즉시 INSERT 쿼리가 실행되므로 persist 시점에 복합 유니크 위반 예외 발생
        assertThatThrownBy(() -> {
            em.persist(duplicateHashtag);
            em.flush();
        }).isInstanceOf(Exception.class);
    }
}
