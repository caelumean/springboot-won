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
   * {@inheritDoc}
   */
  @Override
  public void register(MemberDto member) {
    // 실습 영역
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MemberDto login(String username, String password) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void modifyInfo(MemberDto member) {
    // 실습 영역
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void withdraw(int id) {
    // 실습 영역
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
}
