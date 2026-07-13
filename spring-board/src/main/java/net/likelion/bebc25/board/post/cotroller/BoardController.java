package net.likelion.bebc25.board.post.cotroller;

import net.likelion.bebc25.board.post.dto.PostDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
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
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String getIndex() {
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
                      <p>현재 시간은 %s 입니다.</p>
                      <p><a href="/01/board/list.html">01 - Controller에서 HTML 하드코딩</a></p>
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

    @GetMapping("/01/board/list.html")
    @ResponseBody
    public String getBoardList() {
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = getPosts();
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
                            <a href="detail.html">%s</a>
                          </td>
                          <td>%s</td>
                          <td>%s</td>
                          <td>
                            <a href="list.html" class="btn btn-danger">삭제</a>
                          </td>
                        </tr>
                        """.formatted(
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
}
