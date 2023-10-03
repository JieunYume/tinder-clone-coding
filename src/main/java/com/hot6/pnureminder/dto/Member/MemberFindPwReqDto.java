package com.hot6.pnureminder.dto.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberFindPwReqDto {
    private String username;
    private String nickname;

    @Builder
    public MemberFindPwReqDto(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
