package net.likelion.bebc25.sns.mapper;

import net.likelion.bebc25.sns.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    // 이메일 기반 회원 정보 조회
    // Param XML 파일에서 사용할 변수 이름
    Member findByEmail(@Param("email") String email);

    // 회원 id로 정보 조회
    Member findById(@Param("id") Long id);

}
