package net.likelion.bebc25.sns.dto;

import net.likelion.bebc25.sns.domain.Member;

import java.time.LocalDateTime;

public record MemberProfileResponse(
        Long id,
        String email,
        String nickname,
        String profileImage,
        String role,
        LocalDateTime createdAt
) {
    // Member를 받아서
    // 팩토리 메서드
    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getProfileImage(),
                member.getRole(),
                member.getCreatedAt()
        );
    }
}
