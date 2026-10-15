package net.likelion.bebc25.jpasns.repository;

import net.likelion.bebc25.jpasns.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// jpa가 제공하는 메서드
public interface MemberRepository extends JpaRepository<Member,Long> {
    // 기본으로 제공안하는 메서드는 만들면 된다.
    boolean existsByEmail(String email);

    // 닉네임 조회
    boolean existsByNickname(String nickname);

    // 이메일 단건 조회(로그인용) - 단건 조회이기 때문에 Optional
    Optional<Member> findByEmail(String email);



}
