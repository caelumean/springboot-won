package net.likelion.bebc25.sns.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostResponse(
        @Schema(hidden = true)
        Long id,

        @Schema(description = "멤버 아이디", example = "하루", requiredMode = Schema.RequiredMode.REQUIRED)
        Long memberId,

        @Schema(description = "게시글 본문 내용", example = "밀크티 만드는 방법", requiredMode = Schema.RequiredMode.REQUIRED)
        String content,

        @Schema(description = "첨부 이미지 URL", example = "https://sample.com/images/hello.png", nullable = true)
        String imageUrl,

        @Schema(description = "좋아요", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        int likeCount,

        @Schema(description = "게시글 등록 날짜", example = "2026.08.29", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime createAt,

        @Schema(description = "게시글 수정 날짜", example = "수정 날짜: 2026.08.30", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updateAt
) {
    // 신규 게시글 등록 요청 DTO로 부터 게시글 응답 DTD를 생성하는 팩토리 메서드
    public static PostResponse from(PostCreateRequest dto){
        return new PostResponse(
                dto.getId(),
                dto.getMemberId(),
                dto.getContent(),
                dto.getImageUrl(),
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
