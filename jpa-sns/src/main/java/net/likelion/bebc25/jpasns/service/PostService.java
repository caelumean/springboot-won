package net.likelion.bebc25.jpasns.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.jpasns.domain.Comment;
import net.likelion.bebc25.jpasns.domain.Member;
import net.likelion.bebc25.jpasns.domain.Post;
import net.likelion.bebc25.jpasns.dto.CommentDto;
import net.likelion.bebc25.jpasns.dto.PostDto;
import net.likelion.bebc25.jpasns.repository.CommentRepository;
import net.likelion.bebc25.jpasns.repository.MemberRepository;
import net.likelion.bebc25.jpasns.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    // 게시글 등록
    @Transactional
    public PostDto.Response createPost(Long memberId, PostDto.CreateRequest request){
        // member가 없을 경우에 대한 코드도 작성해야한다.
        // .orElseThrow
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId));
        Post post = new Post(member,request.content()); // 아직까지 비영속 상태
        Post savedPost = postRepository.save(post);  // 영속 상태 em.persist(); -> insert 구문을 실행하는 것과 똑같다.
        // PostDto에 Response에 from에 savedPost를 넘겨준다.
        return PostDto.Response.from(savedPost);

    }

    // 전체 게시글 목록 조회
    public Page<PostDto.Response> getPosts(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(PostDto.Response::from);

    }

    // 게시글 상세 조회
    @Transactional
    public PostDto.DetailResponse getPost(Long id){
        // postRepository의 메서드 중 id로 조회하는 메서드 사용
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시글 입니다. id = " + id));

        return PostDto.DetailResponse.from(post);
    }

    // 댓글 등록
    @Transactional
    public CommentDto.Response addComment(Long postId, Long memberId, CommentDto.CreateRequest request){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글 입니다. id = " + postId));
        // .orElseThrow
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId));

        Comment comment = new Comment(request.content(), member);

        // 영속성 컨텍스트의 동기화를 위해 필요
        // 추후에 post를 사용할 일이 있다면 필요하다
        post.addComment(comment);
        Comment savedComment = commentRepository.save(comment);

        return CommentDto.Response.from(savedComment);

    }

    // 전체 게시글 목록 조회(검색 조건 추가)
    public Page<PostDto.Response> getPosts(Long memberId, String keyword, Pageable pageable){
        Page<Post> posts;

        // 전체 게시글 목록 조회 및 스트림 API를 이용해 List<Post>를 List<PostDto.Response>로 변환하여 반환
//        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        if(memberId != null){   // memberId가 있을 경우
            // 회원 존재 여부를 먼저 확인한 뒤 해당 회원의 게시글 목록을 조회하여 DTO로 변환하여 반환
            if(!memberRepository.existsById(memberId)){
                throw new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId);
            }
            if(keyword != null){
                // 쿼리 메서드 호출
//                posts = postRepository.findByMember_IdAndContentContainingOrderByCreatedAtDesc(memberId, keyword);

                // Query 어노테이션 기반의 JPQL 사용
                posts = postRepository.findByMemberAndKeyword(memberId, keyword,pageable);
            }else{
                posts = postRepository.findByMember_IdOrderByCreatedAtDesc(memberId,pageable);
            }
        }else{  // memberId가 없을 경우
            if(keyword != null){
                posts = postRepository.findByContentContainingOrderByCreatedAtDesc(keyword,pageable);
            }else{  // 그냥 검색
                posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        return posts.map(PostDto.Response::from);
    }
}
