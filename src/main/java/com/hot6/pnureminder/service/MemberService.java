package com.hot6.pnureminder.service;

import com.hot6.pnureminder.dto.Member.MemberResponseDto;
import com.hot6.pnureminder.entity.Member;
import com.hot6.pnureminder.exception.CustomException;
import com.hot6.pnureminder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hot6.pnureminder.exception.ErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
//조회 쿼리만 사용할 예정
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomUserDetailsService customUserDetailsService;


    public Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public Member findMemberById(Long memeberId) {
        return memberRepository.findById(memeberId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public MemberResponseDto findMemberInfoByUsername(String username) {
        return customUserDetailsService.findMemberInfoByUsername(username);
    }


/*
    public String findUsernameForFindingId(String nickname, Integer findQuesNum, String findAnswer) {
        Optional<User> member = memberRepository.findByNicknameAndFindQuesNumAndFindAnswer(nickname, findQuesNum, findAnswer);
        return member.map(User::getUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with given parameters"));
    }

 */


/*
    public Integer findMemberStateByUsername(String username) {
        MemberResponseDto member = customUserDetailsService.findMemberInfoByUsername(username);

        return member.getState();

    }

 */

    public boolean checkNickname(String username) {
        return memberRepository.findByUsername(username).isEmpty();
    }
}
