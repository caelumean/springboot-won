package net.likelion.bebc25.board02.post.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.board02.post.dto.PostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/02/board")
public class BoardController {

    private final List<PostDto> fakePosts;

    public BoardController() {
        fakePosts = new ArrayList<PostDto>();

        PostDto post1 = new PostDto();
        post1.setId(1);
        post1.setTitle("1번 게시글");
        post1.setContent("1번 게시글 내용입니다.");
        post1.setAuthor("하루");
        post1.setSecret(true);
        post1.setCreatedAt(LocalDateTime.now());

        PostDto post2 = new PostDto();
        post2.setId(2);
        post2.setTitle("2번 게시글");
        post2.setContent("2번 게시글 내용입니다.");
        post2.setAuthor("나무");
        post2.setSecret(false);
        post2.setCreatedAt(LocalDateTime.now());

        fakePosts.add(post1);
        fakePosts.add(post2);
    }

    // 모든 게시글 목록을 반환한다.
    public List<PostDto> getPosts() {
        List<PostDto> list = fakePosts;
        return list;
    }


    // 게시글 목록 조회하는 컨트롤러
    @GetMapping("/list.html")
    public String getBoardList(Model model) {
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = getPosts();

        model.addAttribute("posts", posts);

        return "board/list";
    }

    // 게시글 상세 조회하는 컨트롤러
    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model) {
        // 모델 불러오기
        PostDto post = getPost(id);
        // 뷰엔진에게 보내기
        model.addAttribute("post", post);
        // 템플릿 파일 경로
        return "board/detail";
    }
    // 지정한 id의 게시글을 반환한다.
    public PostDto getPost(int id) {
//        PostDto targetPost = null;
        List<PostDto> posts = getPosts();
        for(PostDto org : posts){
            if(org.getId() == id){
//                targetPost = org;
//                break;
                return org;
            }
        }
        throw new IllegalArgumentException(id + "번 게시글은 존재하지 않습니다.");
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
        PostDto post = getPost(id);

        model.addAttribute("postForm", post);
        return "board/write";
    }

    // 게시글 등록 요청을 처리하는 컨트롤러 @ModelAttribute PostDto post
    @PostMapping("/write")
//    public String writePost(@RequestParam("title") String title,
//                            @RequestParam("content") String content,
//                            @RequestParam("author") String author)
    // @Valid : 유효성 검증
    public String writePost(@Valid @ModelAttribute("postForm") PostDto post, // Validation 검증 대상 객체
                            BindingResult bindingResult){   // Validation 검증 결과 저장(대상 객체 뒤에 기술해야 함(중요!!!))
//        PostDto post = new PostDto(title, content, author);
        log.debug(post.toString());
        // 검증에 실패했을 경우
        if(bindingResult.hasErrors()){
            // 작성중이던 페이지로 다시 보낸다.
            return "board/write";
        }
        savePost(post);

        return "redirect:list.html"; // 브라우저에 list.html로 재요청하라고 응답
    }

    // 게시글을 등록한다.
    public void savePost(PostDto post) {
        PostDto lastPost = getPosts().getLast();
        post.setId(lastPost.getId() + 1);
        post.setCreatedAt(LocalDateTime.now());
        fakePosts.add(post);
    }

    // 게시글 수정 요청을 처리하는 컨트롤러
    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute("postForm") PostDto post,
                           BindingResult bindingResult)
    {
        log.debug(post.toString());

        if(bindingResult.hasErrors()){
            return "board/write";
        }
        updatePost(post);
        return "redirect:detail.html?id=" + post.getId();
    }

    // 게시글을 수정한다.
    public void updatePost(PostDto post) {
        PostDto targetPost = null;
        List<PostDto> posts = getPosts();
        for (PostDto org : posts) {
            if (org.getId() == post.getId()) {
                targetPost = org;
                break;
            }
        }

        targetPost.setTitle(post.getTitle());
        targetPost.setContent(post.getContent());
        targetPost.setAuthor(post.getAuthor());
    }

    // 게시글을 삭제 요청하는 컨트롤러
    // a 태그는 기본적으로 get방식이다
    // get방식은 조회할 때만 사용한다
    // post방식으로 할려면 form으로 해야한다
    //@GetMapping("/delete")
    @PostMapping("/delete")
    public String delete(@RequestParam int id) {
        deletePost(id);
        return "redirect:list.html";
    }
    // 게시글을 삭제한다.
    public void deletePost(int id) {
        List<PostDto> posts = getPosts();
        for(PostDto org : posts){
            if(org.getId() == id){
                posts.remove(org);
                break;
            }
        }
    }
}