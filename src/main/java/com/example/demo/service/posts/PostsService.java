package com.example.demo.service.posts;

import com.example.demo.domain.posts.Posts;
import com.example.demo.domain.posts.PostsRepository;
import com.example.demo.web.dto.PostsListResponseDto;
import com.example.demo.web.dto.PostsResponseDto;
import com.example.demo.web.dto.PostsSaveRequestDto;
import com.example.demo.web.dto.PostsUpdateRequestDto;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id ="+id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id ="+id));
        return new PostsResponseDto(entity);
    }

    // readOnly = true: 트랜잭션 범위는 유지하되 조회 기능만 남겨두어 조회 속도가 개선됨
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDec() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no post. id = "+id));

        postsRepository.delete(posts);
    }
}
