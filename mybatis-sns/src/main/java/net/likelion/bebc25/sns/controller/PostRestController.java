package net.likelion.bebc25.sns.controller;

import jakarta.validation.Valid;
import net.likelion.bebc25.sns.dto.PostCreateRequest;
import net.likelion.bebc25.sns.dto.PostResponse;
import net.likelion.bebc25.sns.dto.PostSearchRequest;
import net.likelion.bebc25.sns.dto.PostUpdateRequest;
import net.likelion.bebc25.sns.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// RestController에 ResponseBody가 포함되어있다.
// RestController를 주석으로 막으면 Bean으로 등록이 안되기 때문에 컨트롤러로 동작을 못한다.
//@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> getPostList(@ModelAttribute PostSearchRequest searchRequest) {
        // 검색어에 해당하는 게시글 목록 조회
        List<PostResponse> posts = postService.searchPosts(searchRequest);
        return ResponseEntity.ok(posts);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("X-Member-Id") Long memberId, // 임시로 헤더에서 추출
            @Valid @RequestBody PostCreateRequest request) // JSON 요청 바디를 객체로 자동 매핑
    {
        request.setMemberId(memberId);
        PostResponse createdPost = postService.createPost(request);
        URI location = URI.create("/api/v1/posts/" + createdPost.id());
        return ResponseEntity.created(location).body(createdPost);
    }

    // 게시글 한 건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable("id") Long id)
    {
        // 전달받은 id를 이용해서 서비스 레이어의 게시글 한 건 조회 메서드를 호출
        PostResponse post = postService.getPostById(id);
        // 응답 받은 게시글 DTO를 ResponseEntity를 이용해 응답 상태 코드 200으로 응답
        return ResponseEntity.ok(post);

    }

    // 회원 정보 수정할 때에는 비밀번호 따로 주소 따로 수정하기 때문에 Path를 쓴다.
    // PathVariable 방법
    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("id") Long id,
            @RequestHeader("X-Member-Id") Long memberId,
            @Valid @RequestBody PostUpdateRequest request)
    {
        // 수정 전에 게시글 정보 조회
        PostResponse post = postService.getPostById(id);

        // 본인의 게시글인지 확인
        if(!post.memberId().equals(memberId)){
            // 404로 보내기
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            throw new IllegalStateException("본인의 게시글만 수정이 가능합니다.");
        }

        // 수정 작업
        postService.updatePost(id, request);

        // 수정된 게시글 조회
        PostResponse updatedPost = postService.getPostById(id);

        // 200 응답 상태 코드와 수정된 게시글 정보로 전달
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("id") Long id,
            @RequestHeader("X-Member-Id") Long memberId)
    {
        // 삭제 전에 게시글 정보 조회
        PostResponse post = postService.getPostById(id);

        // 본인의 게시글인지 확인
        if(!post.memberId().equals(memberId)){
            // 404로 보내기
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 삭제 작업
        postService.deletePost(id);

        // 200 응답 상태 코드와 수정된 게시글 정보로 응답
        return ResponseEntity.noContent().build();
    }

}
