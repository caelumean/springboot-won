package net.likelion.bebc25.homework.post.service;

import net.likelion.bebc25.homework.post.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getPosts();
    PostDto getPost(int id);
    void writePost(PostDto post);
    void editPost(PostDto post);
    void removePost(int id);
}
