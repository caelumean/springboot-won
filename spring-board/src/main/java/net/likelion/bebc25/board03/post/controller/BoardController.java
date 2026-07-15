package net.likelion.bebc25.board03.post.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.board03.post.dto.PostDto;
import net.likelion.bebc25.board03.post.repository.PostRepository;
import net.likelion.bebc25.board03.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/03/board")
public class BoardController {
    private final PostService postService;

    public BoardController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 조회하는 컨트롤러
    @GetMapping("/list.html")
    public String getBoardList(Model model) {
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = postService.getPosts();

        model.addAttribute("posts", posts);

        return "board/list";
    }

    // 게시글 상세 조회하는 컨트롤러
    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model) {
        // 모델 불러오기
        PostDto post = postService.getPost(id);
        // 뷰엔진에게 보내기
        model.addAttribute("post", post);
        // 템플릿 파일 경로
        return "board/detail";
    }

    // 게시글 등록 화면을 요청하는 컨트롤러

//    public String getWriteForm(Model model)
    // 모델에 자동으로 주입까지 됨
    // 주의 : ModelAttribute 따로 지정해주지 않으면 (postDto 이름으로) 주입이 된다.
    @GetMapping("/write.html")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post) {
//        model.addAttribute("postForm", new PostDto());
        return "board/write";
    }

    // 게시글 수정 화면을 요청하는 컨트롤러
    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model) {
        PostDto post = postService.getPost(id);

        model.addAttribute("postForm", post);
        return "board/write";
    }

    // 게시글 등록 요청을 처리하는 컨트롤러 @ModelAttribute PostDto post
    @PostMapping("/write")
    // @Valid : 유효성 검증
    public String writePost(@Valid @ModelAttribute("postForm") PostDto post, // Validation 검증 대상 객체
                            BindingResult bindingResult){   // Validation 검증 결과 저장(대상 객체 뒤에 기술해야 함(중요!!!))
        log.debug(post.toString());
        // 검증에 실패했을 경우
        if(bindingResult.hasErrors()){
            // 작성중이던 페이지로 다시 보낸다.
            return "board/write";
        }
        postService.writePost(post);

        return "redirect:list.html"; // 브라우저에 list.html로 재요청하라고 응답
    }

    // 게시글 수정 요청을 처리하는 컨트롤러
    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute("postForm") PostDto post,
                           BindingResult bindingResult)
    {
//        log.debug(post.toString());
        if(bindingResult.hasErrors()){
            return "board/write";
        }
        postService.editPost(post);
        return "redirect:detail.html?id=" + post.getId();
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int id) {
        postService.removePost(id);
        return "redirect:list.html";
    }

}