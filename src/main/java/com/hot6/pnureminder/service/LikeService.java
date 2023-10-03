package com.hot6.pnureminder.service;

import com.hot6.pnureminder.dto.Like.LikeAddResponseDto;
import com.hot6.pnureminder.dto.Like.LikeDto;
import com.hot6.pnureminder.dto.Like.LikeResponseDto;
import com.hot6.pnureminder.entity.Like;
import com.hot6.pnureminder.entity.Member;
import com.hot6.pnureminder.exception.CustomException;
import com.hot6.pnureminder.repository.LikeRepository;
import com.hot6.pnureminder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.hot6.pnureminder.exception.ErrorCode.LIKE_NOT_FOUND;
import static com.hot6.pnureminder.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final LikeRepository likeRepository;
    private final MatchingService matchingService;


    @Transactional
    public LikeAddResponseDto add(String username, Long toMemberId) throws CustomException {
        Member fromMember = memberService.findMemberByUsername(username);
        Member toMember = memberService.findMemberById(toMemberId);

        // 좋아요를 추가할 때마다, 매칭 성공 유무 판단 (서로 좋아요를 누른 상태인지 확인하는 작업)
        boolean matchingStatus = matchingService.matching(fromMember, toMember);

        likeRepository.save(Like.builder()
                .toMember(toMember)
                .fromMember(fromMember)
                .build());

        return LikeAddResponseDto.builder()
                .matchingStatus(matchingStatus)
                .build();
    }


    public LikeResponseDto find(String username) throws CustomException{
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<Like> likeList = likeRepository.findByToMemberId(member.getId())
                .orElseThrow(() -> new CustomException(LIKE_NOT_FOUND));

        List<LikeDto> likeDtoList = new ArrayList<>();
        for (Like like : likeList) {
            likeDtoList.add(LikeDto.toDto(like));
        }
        return LikeResponseDto.builder()
                .likeCount(likeList.size())
                .likeDtoList(likeDtoList)
                .build();
    }
/*
    @Transactional
    public void delete(LikeRequestDTO likeRequestDTO) {

        Member member = memberRepository.findById(likeRequestDTO.getMemberId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(likeRequestDTO.getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        Like like = likeRepository.findByMemberAndDiary(member, diary)
                .orElseThrow(() -> new CustomException(LIKE_NOT_FOUND));

        likeRepository.delete(like);

        diary.deleteLike();
    }

 */


}
