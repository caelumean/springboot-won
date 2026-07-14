package net.likelion.bebc25.board01.post.controller;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.board01.post.dto.PostDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class BoardController {

    private final List<PostDto> fakePosts;

    public BoardController(){
        fakePosts = new ArrayList<PostDto>();

        PostDto post1 = new PostDto();
        post1.setId(1);
        post1.setTitle("1번 게시글");
        post1.setContent("1번 게시글 내용입니다.");
        post1.setAuthor("하루");
        post1.setCreatedAt(LocalDateTime.now());

        PostDto post2 = new PostDto();
        post2.setId(2);
        post2.setTitle("2번 게시글");
        post2.setContent("2번 게시글 내용입니다.");
        post2.setAuthor("나무");
        post2.setCreatedAt(LocalDateTime.now());

        fakePosts.add(post1);
        fakePosts.add(post2);
    }

    // 모든 게시글 목록을 반환한다.
    public List<PostDto> getPosts(){
        List<PostDto> list = fakePosts;
        return list;
    }

    // index.html 요청을 처리하는 컨트롤러
    @RequestMapping(value = "/index", method= RequestMethod.GET)
    @ResponseBody
    public String getIndex(){
        String result = """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                  <meta charset="UTF-8">
                  <title>스프링 부트 게시판 데모 홈</title>
                  <link rel="stylesheet" href="/board/css/common.css">
                  <link rel="stylesheet" href="/board/css/index.css">
                </head>
                <body>
                  <div class="container">
                    <div class="welcome-card">
                      <h1>스프링 부트 게시판 v.02</h1>
                      <p>스프링 부트 게시판에 오신걸 환영합니다.</p>
                      <p><a href="/01/board/list.html">01 - Controller에서 HTML 하드코딩</a></p>
                      <p>현재 시간은 %s 입니다.</p>
                      <div>
                        <a href="board/list.html" class="btn-lg">게시글 목록으로 이동</a>
                      </div>
                    </div>
                  </div>
                </body>
                </html>
                """;

        result = result.formatted(LocalDateTime.now());

        return result;
    }

    // 게시글 목록 조회하는 컨트롤러
    @GetMapping("/01/board/list.html")
    @ResponseBody
    public String getBoardList(){
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = getPosts();

        // View
        String result = """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                  <meta charset="UTF-8">
                  <title>스프링 게시판 - 목록</title>
                  <link rel="stylesheet" href="/board/css/common.css">
                  <link rel="stylesheet" href="/board/css/list.css">
                </head>
                <body>
                  <div class="container">
                    <h1>게시글 목록</h1>
                    <div class="nav">
                      <a href="list.html">목록으로</a>
                      <a href="write.html">새 글 쓰기</a>
                    </div>
                
                    <table>
                      <thead>
                        <tr>
                          <th>번호</th>
                          <th>제목</th>
                          <th>작성자</th>
                          <th>작성일시</th>
                          <th>작업</th>
                        </tr>
                      </thead>
                      <tbody>
                """;

        for(PostDto post : posts){
            result += """
                    <tr>
                      <td>%s</td>
                      <td>
                        <a href="detail.html?id=%s">%s</a>
                      </td>
                      <td>%s</td>
                      <td>%s</td>
                      <td>
                        <a href="list.html" class="btn btn-danger">삭제</a>
                      </td>
                    </tr>
                    """.formatted(
                    post.getId(),
                    post.getId(),
                    post.getTitle(),
                    post.getAuthor(),
                    post.getCreatedAt()
            );
        }


        result += """   
                      </tbody>
                    </table>
                  </div>
                </body>
                </html>
                
                """;

        return result;
    }

    // 게시글 상세 조회하는 컨트롤러
    @GetMapping("/01/board/detail.html")
    @ResponseBody
    public String getDetail(@RequestParam("id") int id){
        PostDto post = getPosts().get(id-1);
        String result = """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                  <meta charset="UTF-8">
                  <title>스프링 게시판 - 상세 보기</title>
                  <link rel="stylesheet" href="/board/css/common.css">
                  <link rel="stylesheet" href="/board/css/detail.css">
                </head>
                <body>
                  <div class="container">
                    <h1>게시글 상세 정보</h1>
                    <div class="nav">
                      <a href="list.html">목록으로</a>
                      <a href="write.html">새 글 쓰기</a>
                    </div>
                
                    <table style="margin-bottom: 20px;">
                      <tr>
                        <th style="width: 60px;">번호</th>
                        <td>%s</td>
                      </tr>
                      <tr>
                        <th>제목</th>
                        <td>%s</td>
                      </tr>
                      <tr>
                        <th>작성자</th>
                        <td>%s</td>
                      </tr>
                      <tr>
                        <th>작성일시</th>
                        <td>%s</td>
                      </tr>
                      <tr>
                        <th>내용</th>
                        <td style="white-space: pre-wrap;">%s</td>
                      </tr>
                    </table>
                
                    <div>
                      <a href="edit.html" class="btn">수정하기</a>
                      <a href="list.html" class="btn btn-secondary">목록으로</a>
                    </div>
                  </div>
                </body>
                </html>
                
                """.formatted(
                post.getId(),
                post.getTitle(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getContent()
        );

        return result;
    }

    // 게시글 등록 화면을 요청하는 컨트롤러
    @GetMapping("/01/board/write.html")
    @ResponseBody
    public String getWriteForm(){
        String result = """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                  <meta charset="UTF-8">
                  <title>스프링 게시판 - 새 글 쓰기</title>
                  <link rel="stylesheet" href="/board/css/common.css">
                  <link rel="stylesheet" href="/board/css/write.css">
                </head>
                <body>
                  <div class="container">
                    <h1>게시글 등록</h1>
                    <div class="nav">
                      <a href="list.html">목록으로</a>
                      <a href="write.html">새 글 쓰기</a>
                    </div>
                
                    <form action="write" method="POST">
                      <div class="form-group">
                        <label for="title">제목</label>
                        <input type="text" id="title" name="title" placeholder="제목을 입력하세요" required>
                      </div>
                
                      <div class="form-group">
                        <label for="author">작성자</label>
                        <input type="text" id="author" name="author" placeholder="작성자 이름을 입력하세요" required>
                      </div>
                
                      <div class="form-group">
                        <label for="content">내용</label>
                        <textarea id="content" name="content" rows="10" placeholder="내용을 입력하세요" required></textarea>
                      </div>
                
                      <div style="margin-top: 20px;">
                        <button type="submit" class="btn">등록</button>
                        <a href="list.html" class="btn btn-secondary">취소</a>
                      </div>
                    </form>
                  </div>
                </body>
                </html>
                
                """;

        return result;
    }

    // 게시글 수정 화면을 요청하는 컨트롤러
    @GetMapping("/01/board/edit.html")
    @ResponseBody
    public String getEditForm(){
        String result = """
               <!DOCTYPE html>
               <html lang="ko">
               <head>
                 <meta charset="UTF-8">
                 <title>스프링 게시판 - 글 수정하기</title>
                 <link rel="stylesheet" href="/board/css/common.css">
                 <link rel="stylesheet" href="/board/css/write.css">
               </head>
               <body>
                 <div class="container">
                   <h1>게시글 수정</h1>
                   <div class="nav">
                     <a href="list.html">목록으로</a>
                     <a href="write.html">새 글 쓰기</a>
                   </div>
        
                   <form action="edit" method="POST">
                     <input type="hidden" name="id" value="1">
        
                     <div class="form-group">
                       <label for="title">제목</label>
                       <input type="text" id="title" name="title" value="세 번째 게시글 제목 샘플" required>
                     </div>
        
                     <div class="form-group">
                       <label for="author">작성자</label>
                       <input type="text" id="author" name="author" value="작성자3" required>
                     </div>
        
                     <div class="form-group">
                       <label for="content">내용</label>
                       <textarea id="content" name="content" rows="10" required>이것은 정적으로 추가된 세 번째 게시글의 상세 예시 본문입니다.
               스프링 MVC와 아키텍처 학습을 위해 모의 데이터를 채워두었습니다.</textarea>
                     </div>
        
                     <div style="margin-top: 20px;">
                       <button type="submit" class="btn">수정</button>
                       <a href="detail.html" class="btn btn-secondary">취소</a>
                     </div>
                   </form>
                 </div>
               </body>
               </html>
                
               """;

        return result;
    }


    // 게시글 등록 요청을 처리하는 컨트롤러
    @PostMapping("/01/board/write")
    public String writePost(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("author") String author){

        PostDto post = new PostDto(title, content, author);
        log.debug(post.toString());

        savePost(post);

        return "redirect:list.html"; // 브라우저에 list.html로 재요청하라고 응답
    }

    // 게시글을 등록한다.
    public void savePost(PostDto post){
        PostDto lastPost = getPosts().getLast();
        post.setId(lastPost.getId() + 1);
        post.setCreatedAt(LocalDateTime.now());
        fakePosts.add(post);
    }

    // 게시글 수정 요청을 처리하는 컨트롤러
    @PostMapping("/01/board/edit")
    public String editPost(@ModelAttribute PostDto post){
        log.debug(post.toString());
        updatePost(post);
        return "redirect:detail.html";
    }

    // 게시글을 수정한다.
    public void updatePost(PostDto post){
        PostDto targetPost = null;
        for(PostDto org : getPosts()){
            if(org.getId() == post.getId()){
                targetPost = org;
                break;
            }
        }

        targetPost.setTitle(post.getTitle());
        targetPost.setContent(post.getContent());
        targetPost.setAuthor(post.getAuthor());
    }

    // 게시글 삭제 요청을 처리하는 컨트롤러
    @PostMapping("/01/board/delete")
    public String deletePost(){
        return "삭제 완료 후 보여줄 페이지";
    }

}