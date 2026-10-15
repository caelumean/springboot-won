package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CommentTest {

    @Autowired
    private EntityManager em;

    private Long newMemberId;
    private Long newPostId;
    private Long newCommentId;

    @BeforeEach     // 각 테스트 케이스 실행 전에 실행 코드
    void setUp(){
        // 1. 회원 등록
        Member member = new Member("haru@test.com", "1234", "하루");
        em.persist(member); // insert 쿼리문이 실행되어 회원이 등록됨
        this.newMemberId = member.getId();  // 새로 등록된 회원 id

        // 2. 게시글 등록
        Post post = new Post(member, "멍멍");
        em.persist(post);   // insert 쿼리문이 생성되어 게시글이 등록됨
        this.newPostId = post.getId();  // 새로 등록된 게시글 id

        // 3. 댓글 등록
        Comment comment = new Comment("댓글1",member);
        // comment랑 post 연결
        comment.assignPost(post);
        em.persist(comment);    // insert 쿼리문이 실행되어 댓글이 등록됨
        this.newCommentId = comment.getId();    // 새로 등록된 댓글 id

        em.clear(); // 캐시 삭제

    }

    @Test
    @DisplayName("댓글 등록")
//    @Commit // 테스트 종료 후 롤백 시키지 않고 커밋(실제 DB에 반영)
    void create(){
        Post findPost = em.find(Post.class, this.newPostId);
        Member findMember = em.find(Member.class, this.newMemberId);

        Comment comment = new Comment("댓글2", findMember);
        comment.assignPost(findPost);
        em.persist(comment);    // insert 쿼리문이 실행되어 댓글이 등록됨

        em.clear(); // 캐시를 비우기

        // 등록한 댓글을 조회
        Comment findComment = em.find(Comment.class, comment.getId());  // select 쿼리문이 실행되어 댓글이 조회됨

        // 조회한 댓글 정보를 이용해서 댓글 등록 검증
        assertThat(findComment).isNotNull();
        assertThat(findComment.getId()).isEqualTo(comment.getId());
        assertThat(findComment.getPost().getId()).isEqualTo(newPostId);
        assertThat(findComment.getMember().getNickname()).isEqualTo("하루"); // 댓글의 작성자가 "하루" 가 맞나?
        assertThat(findComment.getCreatedAt()).isNotNull();

    }

    @Test
    @DisplayName("댓글 조회")
    void read(){
        Comment findComment = em.find(Comment.class, newCommentId);

        // 영속성 컨텍스트의 1차 캐시값에서 즉시 반환되며 select 쿼리를 실행하지 않음
        Comment findComment2 = em.find(Comment.class, newCommentId);

        // 동일한 주소를 가지고 있는 객체인지 여부(==)
        assertThat(findComment).isSameAs(findComment2);

        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo("댓글1");
        assertThat(findComment.getMember().getId()).isEqualTo(newMemberId);
        assertThat(findComment.getPost().getId()).isEqualTo(newPostId);

    }

    @Test
    @DisplayName("댓글 수정")
    void update(){
        Comment targetComment = em.find(Comment.class, newCommentId);

        // 준영속 상태로 엔티티 변환
//        em.detach(targetComment);

        // 엔티티의 속성이 수정되면 update 쿼리가 생성
        // 실행은 바로 되지 않고 버퍼에 저장되고 커밋 직전에 실행됨(쓰기 지연)
        targetComment.changeContent("수정된 댓글");

        // 버퍼에 있는 데이터를 실제로 DB에 전송해라(실제 update 실행)
        em.flush();
        em.clear();

        // 버퍼에 저장된 쿼리는 커밋 직전에 실행됨
        Comment updateComment = em.find(Comment.class, newCommentId);
        assertThat(updateComment.getContent()).isEqualTo("수정된 댓글");
    }

    @Test
    @DisplayName("댓글 삭제")
    void delete(){
        Comment targetComment = em.find(Comment.class, newCommentId);
        em.remove(targetComment);   // delete 쿼리문 생성해서 쓰기 지연 SQL 버퍼에 저장

        em.flush();
        em.clear(); // clear하면 버퍼에 그냥 비우는 것

        Comment findComment = em.find(Comment.class, newCommentId);
        assertThat(findComment).isNull();

    }

    @Test
    @DisplayName("게시글-댓글 양방향 연관관계 매핑")
    void postCommentBiDirectionalMapping(){
        // 사용자, 게시글 조회
        Member member = em.find(Member.class, newMemberId);
        Post post = em.find(Post.class, newPostId);

        Comment comment2 = new Comment("댓글2", member);
        Comment comment3 = new Comment("댓글3", member);

        post.addComment(comment2);
        post.addComment(comment3);

        em.flush();
        em.clear();

        // 게시글 조회시 댓글 목록 확인(역방향 1 -> N)
        Post findPost = em.find(Post.class, newPostId);
        assertThat(findPost).isNotNull();
        // 댓글이 총 3개인지 확인
        assertThat(findPost.getComments()).hasSize(3);
        assertThat(findPost.getComments())
                .extracting(Comment::getContent) // 컬랙션 내 엔티티들의 특정 필드를 추출
                .containsExactly("댓글1", "댓글2", "댓글3"); // 추출된 값들이 내용과 순서가 일치하는지 확인
        em.flush();
        em.clear();


        // 댓글 조회시 게시글 확인(정방향 N -> 1)
        Comment findComment = em.find(Comment.class, comment2.getId());
        assertThat(findComment).isNotNull();
        assertThat(findComment.getPost().getId()).isEqualTo(post.getId());
        assertThat(findComment.getPost().getMember().getNickname()).isEqualTo("하루");
        assertThat(findComment.getPost().getContent()).isEqualTo("멍멍");
        assertThat(findComment.getMember().getEmail()).isEqualTo("haru@test.com");
    }

    @Test
    @DisplayName("Post를 통한 Comment 삭제")
    void postCommentBiDirectionalMappingDelete(){
        Post findPost = em.find(Post.class, newPostId);
        assertThat(findPost).isNotNull();
        assertThat(findPost.getComments()).hasSize(1);

        // 댓글 삭제
        findPost.getComments().removeFirst();
        em.flush();
        em.clear();

        Post findPost2 = em.find(Post.class, findPost.getId());
        assertThat(findPost2).isNotNull();
        assertThat(findPost2.getComments()).hasSize(0);
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


}
