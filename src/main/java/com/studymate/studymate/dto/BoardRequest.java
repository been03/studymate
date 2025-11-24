package com.studymate.studymate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor // ⭐️ 기본 생성자
@AllArgsConstructor // ⭐️ 모든 필드 포함 생성자
public class BoardRequest {

    private String title;   // 게시글 제목
    private String content; // 게시글 내용

    // ⭐️ [참고] 작성자 정보(User)는 클라이언트에서 받지 않고,
    // Spring Security의 인증 정보에서 서버가 직접 추출하여 사용합니다.
}
