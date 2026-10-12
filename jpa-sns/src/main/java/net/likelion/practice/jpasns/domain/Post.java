package net.likelion.practice.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.likelion.practice.jpasns.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 테이블 관점
//    @Column(nullable = false)
//    private Long memberId;

    // 객체 지향적 관점
    // 테이블 관점으로 가져오지 말고 Member 객체를 통으로 필드에 가져오자
    // 테이블 관점과 객체 지향적 관점의 차이가 발생해서
    // JPA가 이 차이를 극복하기 위해 지원하는 게 있다.
    // 그때 사용되는 어노테이션 @ManyToOne, @JoinColumn
    // fetch: 조인 시점 EAGER가 기본값
    // eager: post를 조회할 때 같이 조인해서 가져온다(즉시로딩)
    // lazy: memberId만 가져온다 id만 가지고 있는 가짜 객체를 만든다.(지연로딩)
    // post.member할 때 그때 조인해서 가져온다.
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;


    // 컬럼타입을 기본으로 제공하지 않는 것을 지정하고 싶을 때
    @Column(columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    private int likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Post(Member member, String content) {
        this(member, content, null);
    }

    public Post(Member member, String content, String imageUrl) {
        this.member = member;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likeCount = 0;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.assignPost(this);
    }
}
