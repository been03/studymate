package com.studymate.studymate.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// Board 엔티티와 ID 타입(Long)을 상속받아 기본 CRUD 기능을 자동 제공받습니다.
public interface BoardRepository extends JpaRepository<Board, Long> {

    // Pageable을 사용하여 페이징 처리도 함께 진행
    // 1. 제목 + 내용 검색 (기존)
    Page<Board> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);

    // 2. 제목 단독 검색 (신규 추가)
    Page<Board> findByTitleContaining(String titleKeyword, Pageable pageable);

    // 3. 내용 단독 검색 (신규 추가)
    Page<Board> findByContentContaining(String contentKeyword, Pageable pageable);
}
