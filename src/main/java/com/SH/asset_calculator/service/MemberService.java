package com.SH.asset_calculator.service;

import com.SH.asset_calculator.domain.Member;
import com.SH.asset_calculator.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getMembersScopePublic(){
        memberRepository.getMembersScopePublic();

        return null;
    }
}
