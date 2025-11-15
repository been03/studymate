package com.studymate.studymate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    private String email;
    private String password;

    // Lombok의 @NoArgsConstructor는 기본 생성자를 자동으로 만들어 줍니다.
}