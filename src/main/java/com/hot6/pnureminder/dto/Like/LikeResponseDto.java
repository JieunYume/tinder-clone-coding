package com.hot6.pnureminder.dto.Like;

import com.hot6.pnureminder.entity.Like;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponseDto {
    private int likeCount;
    private List<LikeDto> likeDtoList;
}
