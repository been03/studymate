package com.studymate.studymate.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Spring Bean으로 등록
@RequiredArgsConstructor // 의존성 주입
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 사용자 정보를 DB에서 가져오기 위해 주입

    // Spring Security가 인증을 위해 사용자 정보를 로드하는 핵심 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // DB에서 이메일로 User 객체를 찾아옵니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // User 엔티티의 toUserDetails() 메서드를 사용하여 Spring Security의 UserDetails 객체로 변환하여 반환
        return user.toUserDetails();
    }
}
