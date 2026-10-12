package net.likelion.practice.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.likelion.practice.jpasns.common.BaseTimeEntity;
import net.likelion.practice.jpasns.domain.Post;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_post_like_member_post", columnNames = {"member_id", "post_id"}))
public class PostLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Post post;

    // 좋아요한 사람들의 목록을 보여준다면
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Member member;

    public PostLike(Post post, Member member) {
        this.post = post;
        this.member = member;

    }
}
