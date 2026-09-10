package net.likelion.bebc25.sns.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
    // expiresIn : 토큰이 만료되기 전에 다시 토큰을 발행하는 로직을 구현하기 위해 생성
    public static TokenResponse of(String accessToken, String refreshToken, Long expiresIn) {
        return new TokenResponse(accessToken, refreshToken, "Bearer", expiresIn);
    }
}
