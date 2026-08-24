package net.likelion.bebc25.board03.post.service;

import net.likelion.bebc25.board03.post.dto.PostDto;
import net.likelion.bebc25.board03.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

// 서비스 역할을 하는 애다 =>Component와 똑같은 기능을 가지고 있다.
@Service
public class PostServiceImpl implements PostService{
    // 레포지토리는 디비 전담 그 외의 모든 작업은 서비스
    // 알림톡이라던가 이메일
    // 레포지토리를 관리하고 관장하는 것
    // 트랜잭션 시작하고 레포지토리
    private final PostRepository postRepository;

    public PostServiceImpl(@Qualifier("jdbcTemplateRepository") PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDto> getPosts() {
        return postRepository.findAll();
    }

    @Override
    public PostDto getPost(int id) {
        return postRepository.findById(id);
    }

    @Override
    public void writePost(PostDto post) {
        postRepository.save(post);
    }

    @Override
    public void editPost(PostDto post) {
        postRepository.update(post);
    }

    @Override
    public void removePost(int id) {
        postRepository.deleteById(id);
    }
}
