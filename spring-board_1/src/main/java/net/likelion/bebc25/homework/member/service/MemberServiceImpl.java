package net.likelion.bebc25.homework.member.service;

import net.likelion.bebc25.homework.member.dto.MemberDto;
import net.likelion.bebc25.homework.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MemberService 인터페이스의 비즈니스 로직을 처리하는 기본 구현 클래스입니다.
 */
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  /**
   * 생성자를 통해 MemberRepository 의존성을 주입받습니다.
   *
   * @param memberRepository 주입받을 MemberRepository 스프링 빈 객체
   */
  public MemberServiceImpl(@Qualifier("jdbcTemplateMemberRepository") MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  /**
   * 회원 가입
   */
  @Override
  public void register(MemberDto member) {
    // 실습 영역
    memberRepository.save(member);
  }

  /**
   * 로그인
   */
  @Override
  public MemberDto login(String username, String password) {
    MemberDto member= memberRepository.findByUsername(username);
    if(member==null){
      return null;
    }

    if(member.getPassword().equals(password)){
      return member;
    }

    return null;

  }

  /**
   * 회원 정보를 수정
   */
  @Override
  public void modifyInfo(MemberDto member) {
    // 실습 영역
    memberRepository.update(member);
  }

  /**
   * 회원 탈퇴
   */
  @Override
  public void withdraw(int id) {
    // 실습 영역
    memberRepository.deleteById(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MemberDto> getMembers() {
    return memberRepository.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MemberDto getMember(int id) {
    return memberRepository.findById(id);
  }

  /**
   * UserName 중복 검사
   * @param username
   * @return
   */
  @Override
  public boolean existsByUsername(String username) {
    MemberDto member= memberRepository.findByUsername(username);

    return member != null;
  }
}
