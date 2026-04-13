package com.example.simulator.web.controller;

import com.example.simulator.domain.member.Member;
import com.example.simulator.domain.member.MemberRepository;
import com.example.simulator.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/member/v1")
public class MemberController {

    private final MemberRepository memberRepository;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody MemberDto memberDto) {
        Member member = Member.createMember(memberDto.getName(), memberDto.getBalance());
        memberRepository.save(member);
        String result = member.getName() + "(" + member.getId() + ") 가입완료!";
        return ResponseEntity.ok(result);
    }


}
