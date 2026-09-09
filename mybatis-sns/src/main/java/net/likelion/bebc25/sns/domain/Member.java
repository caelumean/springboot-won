package net.likelion.bebc25.sns.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드 가지고 있는 생성자
@ToString   // toSring
@Builder    // Builder
public class Member {
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String profileImage;
    @Builder.Default    // 값을 빼먹으면 기본값으로 ROLE_USER를 넣어라
    private String role = "ROLE_USER";
    private LocalDateTime createdAt;
}
