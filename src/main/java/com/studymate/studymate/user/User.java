package com.studymate.studymate.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;

@Entity
@Table(name = "user") // 테이블 이름은 DB에 따라
@Getter
@Builder // DTO를 엔티티로 변환할 때 사용
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // ⭐️ email은 필수(Not Null)이며, 중복 불가(Unique)
    private String email;

    @Column(nullable = false) // ⭐️ password는 필수(Not Null)
    private String password;

    private String nickname;

    // ⭐️ 참고: 프로젝트 초기에는 Getter, Setter만 있어도 작동하지만,
    // JPA의 안정적인 사용을 위해 NoArgsConstructor, AllArgsConstructor, Builder를 사용하는 것이 좋습니다.

    //User 객체를 Spring Security의 UserDetails 객체로 변환하는 핵심 메서드
    public UserDetails toUserDetails() {
        return org.springframework.security.core.userdetails.User.builder()
                .username(this.email) // UserDetails의 username은 우리의 email을 사용합니다.
                .password(this.password)
                // ⭐️최소한의 권한 ROLE_USER를 부여합니다.
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }


}

