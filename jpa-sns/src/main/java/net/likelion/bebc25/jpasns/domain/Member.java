package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.likelion.bebc25.jpasns.common.entity.BaseTimeEntity;


@Entity
@Getter
// ctrl + space 사용할 수 있는 명령어 뜸
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 필수로 JPA에서 호출해서 사용함
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Null을 허용하지 않겠다.
    // UKmbmcqelty0fbrvxp1q58dn57t JPA에서 지정해주는 이름이다.
    // 명확하게 이름을 지정할려면 테이블 수준에서 이름을 지정해줘야한다.
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    // Null 허용
    @Column(length = 50)
    private String nickname;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)  // ORDINAL = 내부에 숫자로 저장 / STRING = 문자로 저장
    private Role role;

    private String profileImage;

    // updatable: 한번 최초 insert문에서만 사용
    // 읽기 전용으로 컬럼을 만들고 싶을 때 updatable를 사용한다.
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;

    // 일반 사용자가 입력할 경우
    public Member(String email, String password, String nickname) {
        this(email, password, nickname, Role.USER);
    }

    // 관리자가 입력할 경우
    public Member(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
