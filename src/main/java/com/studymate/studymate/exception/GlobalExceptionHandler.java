package com.studymate.studymate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model) {
        log.warn("게시글 조회 예외 감지: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/error"; // templates/error/error.html
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        log.warn("잘못된 인자 예외 감지: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        log.error("서버 내부 에러 감지: ", e);
        model.addAttribute("errorMessage", "요청을 처리하는 중 오류가 발생했습니다.");
        return "error/error";
    }
}