package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.likelion.bebc25.jpasns.common.entity.BasePostReaction;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_post_like_member_post", columnNames = {"member_id", "post_id"}))
public class PostLike extends BasePostReaction {

    public PostLike(Post post, Member member) {
        // 부모 생성자 호풀
        super(post, member);

    }
}
