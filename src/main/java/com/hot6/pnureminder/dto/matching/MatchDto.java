package com.hot6.pnureminder.dto.matching;

import com.hot6.pnureminder.entity.Matching;
import com.hot6.pnureminder.entity.Member;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchDto {
    private String username;
    private char gender;

    static public MatchDto toDto(Member matchingMember){
        return MatchDto.builder()
                .username(matchingMember.getUsername())
                .gender(matchingMember.getGender())
                .build();
    }
}
