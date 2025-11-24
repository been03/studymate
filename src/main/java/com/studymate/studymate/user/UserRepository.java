package com.studymate.studymate.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일을 통해 User 객체를 찾는 메서드 (Spring Data JPA가 자동으로 구현해 줌)
    Optional<User> findByEmail(String email);

}
