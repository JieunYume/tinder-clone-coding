package com.hot6.pnureminder.dto.Like;

import com.hot6.pnureminder.entity.Like;
import com.hot6.pnureminder.entity.Member;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDto {
    private String username;
    private char gender;

    static public LikeDto toDto(Like like){
        Member fromMember = like.getFromMember();
        return LikeDto.builder()
                .username(fromMember.getUsername())
                .gender(fromMember.getGender())
                .build();
    }
}
