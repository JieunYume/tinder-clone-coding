package com.hot6.pnureminder.controller;

import com.hot6.pnureminder.dto.matching.MatchingResponseDto;
import com.hot6.pnureminder.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    @GetMapping("/my")
    public ResponseEntity<MatchingResponseDto> findMatch(Principal principal) {
        return ResponseEntity.ok(matchingService.findMatching(principal.getName()));
    }
}
