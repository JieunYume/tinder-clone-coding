package com.hot6.pnureminder.controller;

import com.hot6.pnureminder.dto.Like.LikeAddResponseDto;
import com.hot6.pnureminder.dto.Like.LikeResponseDto;
import com.hot6.pnureminder.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    private static final Logger log = LoggerFactory.getLogger(LikeController.class);

    @PostMapping("/{toUserId}")
    public ResponseEntity<LikeAddResponseDto> addLike(@PathVariable Long toUserId, Principal principal) {
        return ResponseEntity.ok(likeService.add(principal.getName(), toUserId));
    }

    @GetMapping("/my")
    public ResponseEntity<LikeResponseDto> findLike(Principal principal) {
        log.info("principal.getName(): {}", principal.getName());
        return ResponseEntity.ok(likeService.find(principal.getName()));
    }


    //        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        System.out.println("SecurityUtil.getCurrentMemberId() "+SecurityUtil.getCurrentMemberId());

/*
    @DeleteMapping("/")
    public ResponseEntity<?> delete(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        likeService.delete(likeRequestDTO);
        return ResponseEntity.ok("좋아요 삭제 성공");
    }

 */
}
