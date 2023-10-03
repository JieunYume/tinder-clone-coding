package com.hot6.pnureminder.service;

import com.hot6.pnureminder.dto.matching.MatchDto;
import com.hot6.pnureminder.dto.matching.MatchingResponseDto;
import com.hot6.pnureminder.entity.Like;
import com.hot6.pnureminder.entity.Matching;
import com.hot6.pnureminder.entity.Member;
import com.hot6.pnureminder.exception.CustomException;
import com.hot6.pnureminder.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingService {
    private final MemberService memberService;
    private final MatchingRepository matchingRepository;

    private static final Logger log = LoggerFactory.getLogger(MatchingService.class);

    public boolean matching(Member member1, Member member2) {
        List<Like> toMemberLikeList = member2.getMyLikeList();

        boolean isMatch = false;
        for (Like like : toMemberLikeList) {
            if(like.getToMember().getId() == member1.getId()){ // 나도 상대방에게 좋아요를 받았다면 -> 매칭 성공
                isMatch = true;
                Matching matching = matchingRepository.save(Matching.builder()
                        .member1(member1)
                        .member2(member2)
                        .build());
                return true;
            }
        }
        return false;
    }

    public MatchingResponseDto findMatching(String username) throws CustomException {
        Member member = memberService.findMemberByUsername(username);

        List<Matching> myMatchingList = member.getMyMatchingList();
        log.info("myMatchingList: {}", myMatchingList.size());

        List<MatchDto> myMatchingListDto = new ArrayList<>();
        if(myMatchingList != null) {
            for (Matching matching : myMatchingList) {
                if(matching.getMember1().equals(member)){
                    myMatchingListDto.add(MatchDto.toDto(matching.getMember2()));
                } else{
                    myMatchingListDto.add(MatchDto.toDto(matching.getMember1()));
                }
            }
        }

        return MatchingResponseDto.builder()
                .myMatchingList(myMatchingListDto)
                .build();
    }
}
