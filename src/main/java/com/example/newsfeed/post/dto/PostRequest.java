package com.example.newsfeed.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotBlank
    @Size(min = 1, max = 100, message = "length min = 1, max = 100")
    private String title;
    @NotBlank
    private String content;
}
