package com.studymate.studymate.board;

import com.studymate.studymate.dto.BoardRequest;
import com.studymate.studymate.dto.BoardResponse;
import com.studymate.studymate.user.User;
import com.studymate.studymate.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // ⭐️ 기본적으로 읽기 전용으로 설정
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository; // ⭐️ 작성자 정보 조회를 위해 추가

    // ===========================================
    // 1. 게시글 생성 (Create)
    // ===========================================
    @Transactional
    public BoardResponse createBoard(BoardRequest request, String email) {
        // 1. 현재 로그인 사용자(작성자) 찾기
        // Spring Security에서 받은 이메일(principal.name)로 User 엔티티를 찾습니다.
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("작성자 정보가 유효하지 않습니다."));

        // 2. BoardRequest와 User 정보를 이용해 Board 엔티티 생성
        Board newBoard = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author) // ⭐️ User 객체를 author 필드에 연결
                .build();

        // 3. DB에 저장
        Board savedBoard = boardRepository.save(newBoard);

        // 4. 응답 DTO로 변환하여 반환
        return new BoardResponse(savedBoard);
    }

    // ===========================================
    // 2. 게시글 상세 조회 (Read - Single)
    // ===========================================
    public BoardResponse getBoardById(Long boardId) {
        // ID로 게시글을 찾고, 없으면 예외 발생
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException(boardId + "번 게시글을 찾을 수 없습니다."));

        // 응답 DTO로 변환하여 반환
        return new BoardResponse(board);
    }

    // ===========================================
    // 3. 게시글 목록 조회 (Read - List with Paging)
    // ===========================================
    // Pageable 객체를 인자로 받아 페이징된 결과를 반환합니다.
    public Page<BoardResponse> getAllBoards(Pageable pageable) {
        // 1. BoardRepository에서 Page<Board> 형태로 데이터를 조회합니다.
        Page<Board> boards = boardRepository.findAll(pageable);

        // 2. Page<Board>를 Page<BoardResponse>로 변환하여 반환합니다.
        // map() 메서드를 사용하여 각 Board 엔티티를 BoardResponse DTO로 변환합니다.
        return boards.map(BoardResponse::new);
    }

    // ===========================================
    // 4. 게시글 수정 (Update)
    // ===========================================
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest request, String email) {
        // 1. 게시글 찾기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException(boardId + "번 게시글을 찾을 수 없습니다."));

        // 2. ⭐️ 인가 확인: 로그인된 사용자와 게시글 작성자가 일치하는지 검증
        if (!board.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다.");
        }

        // 3. 게시글 내용 업데이트
        board.update(request.getTitle(), request.getContent());

        // update 메서드 내부에서 @Transactional에 의해 자동으로 DB에 반영됩니다.
        // Board 엔티티를 그대로 반환하고 Controller에서 DTO로 변환해도 되지만,
        // 여기서는 명시적으로 DTO를 반환합니다.
        return new BoardResponse(board);
    }

    // ===========================================
    // 5. 게시글 삭제 (Delete)
    // ===========================================
    @Transactional
    public void deleteBoard(Long boardId, String email) {
        // 1. 게시글 찾기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException(boardId + "번 게시글을 찾을 수 없습니다."));

        // 2. ⭐️ 인가 확인: 로그인된 사용자와 게시글 작성자가 일치하는지 검증
        if (!board.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("게시글 삭제 권한이 없습니다.");
        }

        // 3. DB에서 삭제
        boardRepository.delete(board);
    }
}