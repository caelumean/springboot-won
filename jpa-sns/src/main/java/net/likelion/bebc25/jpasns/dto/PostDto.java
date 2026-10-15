package net.likelion.bebc25.jpasns.dto;

import net.likelion.bebc25.jpasns.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {

    // 게시글 등록 요청시 전달되는 DTO
    public record CreateRequest(
            String content
    ) { }

    // 게시글 응답용 DTO(댓글 미포함)
    public record Response(
            Long id,
            String content,
            String authorNickname,
            LocalDateTime createdAt

    ) {
        // Post 엔티티를 Response DTO로 변환
        public static Response from(Post post) {
            return new Response(
                    post.getId(),
                    post.getContent(),
                    post.getMember().getNickname(),
                    post.getCreatedAt()
            );
        }
    }

    // 게시글 응답용 DTO(댓글 포함)
    public record DetailResponse(
            Long id,
            String content,
            String authorNickname,
            LocalDateTime createdAt,
            // CommentDto의 Response 타입
            // 엔티티가 Response에 포함되면 안된다!!(중요)
            List<CommentDto.Response> comments

    ) {
        // Post 엔티티를 Response DTO로 변환
        public static DetailResponse from(Post post) {
            return new DetailResponse(
                    post.getId(),
                    post.getContent(),
                    post.getMember().getNickname(),
                    post.getCreatedAt(),
                    // CommentDto의 Response 타입이기 때문에 stream을 쓴다.
                    post.getComments().stream()
                            .map(CommentDto.Response::from)
                            .toList()
            );
        }
    }

    public record SummaryResponse(
            Long postId,
            String memberNickname,
            String content,
            int likeCount
    ) { }
}
