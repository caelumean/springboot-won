package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {

    @Id
    @Column(name="member_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Embedded
    private Address address;

    @Column(length = 1, nullable = false)
    private String marketingAgreed;

    // 마케팅 동의하면 해당 값까지 전달받는 생성자
    public MemberDetail(Member member, String introduction, Address address, String marketingAgreed) {
        this.member = member;
        this.introduction = introduction;
        this.address = address;
        this.marketingAgreed = marketingAgreed;
    }

    // 마케팅 동의 안하는 생성자
    public MemberDetail(Member member, String introduction, Address address) {

        this(member, introduction, address, "N");
    }
}
