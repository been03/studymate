package com.studymate.studymate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.studymate.studymate.User;
import com.studymate.studymate.UserRepository;
import com.studymate.studymate.dto.UserJoinRequest; // DTO 임포트 추가
import com.studymate.studymate.dto.UserLoginRequest;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입 추가

    @Transactional // ⭐️ [2] DB 변경 작업에는 @Transactional 어노테이션을 추가하는 것이 좋음
    // 개별 String 대신 DTO 객체 하나를 받도록 변경
    public Long join(UserJoinRequest request) {
        // 1. User 객체 생성 (데이터베이스에 저장할 모델)
        User user = new User();
        user.setEmail(request.getEmail()); // DTO에서 값 추출

        // DTO에서 받은 비밀번호를 암호화하여 저장합니다.
        String rawPassword = request.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword); // 암호화된 비밀번호 저장

        user.setNickname(request.getNickname());

        // 2. Repository를 통해 DB에 저장
        userRepository.save(user);

        // 3. 저장된 User의 ID 반환
        return user.getId();
    }

    // 로그인 요청 처리
// @Transactional을 붙이지 않습니다. (DB에 데이터를 변경하지 않고 조회만 하므로)
    public Long login(UserLoginRequest request) {

        // 1. 이메일을 기반으로 User 객체 찾기
        // findByEmail 메서드는 곧 UserRepository에 추가할 예정입니다.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 비밀번호 일치 여부 확인 (BCrypt 사용)
        // 평문 비밀번호(request.getPassword())와 암호화된 비밀번호(user.getPassword())를 비교합니다.
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 시, 사용자 ID 반환 (실제 서비스에서는 JWT 토큰이나 세션 정보 등을 반환합니다.)
        return user.getId();
    }

}
