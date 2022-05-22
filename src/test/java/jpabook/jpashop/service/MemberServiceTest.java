package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  private MemberService memberService;
  @Autowired
  private MemberRepositoryOld memberRepositoryOld;
  @Autowired
  private EntityManager em;

  @Test
  void 회원가입() throws Exception{
    // given
    Member member = new Member();
    member.setName("kim");

    // when
    Long savedId = memberService.join(member);

    // then
    assertEquals(member, memberRepositoryOld.findOne(savedId));
  }

  @Test
  void 중복_회원_예약() throws Exception{
    // given
    Member member1 = new Member();
    member1.setName("kim1");

    Member member2 = new Member();
    member2.setName("kim1");

    // when
    memberService.join(member1);
//    assertThrows(IllegalStateException.class, () -> memberService.join(member2));
//    try {
//      memberService.join(member2);
//    } catch (IllegalStateException e) {
//      return;
//    }

    // then
//    Assertions.fail("이미 존재하는 회원입니다.");
  }
}