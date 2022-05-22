package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepositoryOld memberRepositoryOld;

  // 회원가입
  public Long join(Member member) {

    validateDuplicateMember(member);
    memberRepositoryOld.save(member);
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    // exception
    List<Member> findMembers = memberRepositoryOld.findByName(member.getName());
    if(!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  //회원 전체 조회
  public List<Member> findMembers() {
    return memberRepositoryOld.findAll();
  }

  public Member findOne(Long memberId) {
    return memberRepositoryOld.findOne(memberId);
  }

  @Transactional
  public void update(Long id, String name) {
    Member member = memberRepositoryOld.findOne(id);
    member.setName(name);
  }
}
