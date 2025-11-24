package com.studymate.studymate.board;

import org.springframework.data.jpa.repository.JpaRepository;

// Board 엔티티와 ID 타입(Long)을 상속받아 기본 CRUD 기능을 자동 제공받습니다.
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 추가적인 사용자 정의 쿼리는 여기에 선언할 수 있습니다. (예: 제목으로 검색)
}
