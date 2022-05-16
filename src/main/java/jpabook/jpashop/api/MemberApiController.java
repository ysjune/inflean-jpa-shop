package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @Scheduled
  @PostMapping("/api/v1/members")
  public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
    Long memberId = memberService.join(member);
    return new CreateMemberResponse(memberId);
  }

  @PostMapping("/api/v2/members")
  public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
    Member member = new Member();
    member.setName(request.getName());
    Long memberId = memberService.join(member);
    return new CreateMemberResponse(memberId);
  }

  @PutMapping("/api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
    memberService.update(id, request.getName());
    Member member = memberService.findOne(id);
    return new UpdateMemberResponse(member.getId(), member.getName()) ;
  }

  @GetMapping("/api/v1/members")
  public List<Member> membersV1() {
    return memberService.findMembers();
  }

  @GetMapping("/api/v2/members")
  public Result memberV2() {
    List<Member> members = memberService.findMembers();
    List<MemberDto> memberDtos = members.stream().map(v -> new MemberDto(v.getName()))
        .collect(Collectors.toList());

    return new Result(memberDtos.size(), memberDtos);
  }

  @Data
  @AllArgsConstructor
  static class Result<T> {
    private int count;
    private T data;
  }

  @Data
  @AllArgsConstructor
  static class MemberDto {
    private String name;
  }

  @Data
  @AllArgsConstructor
  static class CreateMemberResponse {
    private Long id;
  }

  @Data
  static class CreateMemberRequest {
    private String name;
  }

  @Data
  @AllArgsConstructor
  static class UpdateMemberResponse {
    private Long id;
    private String name;
  }

  @Data
  private class UpdateMemberRequest {
    @NotEmpty
    private String name;
  }
}
