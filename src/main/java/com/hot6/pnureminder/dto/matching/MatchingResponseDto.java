package com.hot6.pnureminder.dto.matching;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingResponseDto {
    private List<MatchDto> myMatchingList;
}
