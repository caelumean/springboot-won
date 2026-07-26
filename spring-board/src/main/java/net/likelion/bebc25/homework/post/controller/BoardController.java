package net.likelion.bebc25.homework.post.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.homework.post.dto.PostDto;
import net.likelion.bebc25.homework.post.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/homework/board")
public class BoardController {

    private final PostService postService;
    private final String uploadDir = Paths.get("D:","Programming","TUI_Editor","upload").toString();

    public BoardController(PostService postService){
        this.postService = postService;
    }

    // 게시글 목록 조회하는 컨트롤러
    @GetMapping("/list.html")
    public String getBoardList(Model model){
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = postService.getPosts();
        model.addAttribute("posts", posts);
        return "board/list";
    }

    // 게시글 상세 조회하는 컨트롤러
    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model){
        PostDto post = postService.getPost(id);
        model.addAttribute("post", post);
        return "board/detail"; // 템플릿 파일 경로
    }

    // 게시글 등록 화면을 요청하는 컨트롤러
    @GetMapping("/write.html")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post){ // 모델에 자동으로 주입까지 됨(postDto 이름으로)
        return "board/write";
    }

    // 게시글 수정 화면을 요청하는 컨트롤러
    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model){
        PostDto post = postService.getPost(id);
        model.addAttribute("postForm", post);
        return "board/write";
    }

    // 게시글 등록 요청을 처리하는 컨트롤러
    @PostMapping("/write")
    public String writePost(@Valid @ModelAttribute("postForm") PostDto post, // Validation 검증 대상 객체
                            BindingResult bindingResult){ // Validation 검증 결과 저장 객체(대상 객체 뒤에 기술해야 함)
        if(bindingResult.hasErrors()){ // 검증에 실패했을 경우
            return "board/write"; // 작성중이던 페이지로 다시 보낸다.
        }
        log.info("post = {}",post);
        log.info("content length = {}", post.getContent().length());
        log.info("content = {}", post.getContent());
        postService.writePost(post);
        return "redirect:list.html"; // 브라우저에 list.html로 재요청하라고 응답
    }

    // 게시글 수정 요청을 처리하는 컨트롤러
    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute("postForm") PostDto post,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "board/write";
        }
        postService.editPost(post);
        return "redirect:detail.html?id=" + post.getId();
    }

    // 게시글 삭제 요청을 처리하는 컨트롤러
    @PostMapping("/delete")
    public String deletePost(@RequestParam int id){
        postService.removePost(id);
        return "redirect:list.html";
    }

    /**
     * 에디터 이미지 업로드
     * @param image 파일 객체
     * @return 업로드된 파일명
     */
    @PostMapping("/image-upload")
    @ResponseBody
    public String uploadEditorImage(@RequestParam("image") MultipartFile image) {

        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();

        String uuid = UUID.randomUUID().toString().replace("-", "");

        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);

        String saveFilename = uuid + "." + extension;

        String fileFullPath = Paths.get(uploadDir, saveFilename).toString();

        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            image.transferTo(new File(fileFullPath));

            return saveFilename;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 디스크에 업로드된 파일을 byte[]로 반환
     * @param filename 디스크에 업로드된 파일명
     * @return image byte array
     */
    @GetMapping(value = "/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    @ResponseBody
    public byte[] printEditorImage(@RequestParam final String filename) {
        // 업로드된 파일의 전체 경로
        String fileFullPath = Paths.get(uploadDir, filename).toString();

        // 파일이 없는 경우 예외 throw
        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }

        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }
}