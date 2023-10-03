package com.hot6.pnureminder.controller;

import com.hot6.pnureminder.dto.Member.*;
import com.hot6.pnureminder.dto.Token.TokenReqDto;
import com.hot6.pnureminder.dto.Token.TokenResDto;
import com.hot6.pnureminder.service.AuthService;
import com.hot6.pnureminder.service.MemberService;
import com.hot6.pnureminder.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final VerificationTokenService verificationTokenService;


    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.signup(memberRequestDto));
    }

    //보안 향상을 위한 중복이메일 미허가
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return memberService.checkNickname(email);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenReqDto requestDto) {
        authService.logout(requestDto);
        return ResponseEntity.ok().build();
    }

// 무슨 용도일까?
    @PostMapping("/reissue")
    public ResponseEntity<TokenResDto> reissue(@RequestBody TokenReqDto tokenReqDto) {
        return ResponseEntity.ok(authService.reissue(tokenReqDto));
    }
/*
    @PostMapping("/findingId")
    public String getMemberIdForFindingId(@RequestBody MemberFindIdReqDto requestDto) {
        return  memberService.findUsernameForFindingId(requestDto.getNickname(), requestDto.getFindQuesNum(), requestDto.getFindAnswer());
    }
    // 보안문제 리팩터링 요청
    @PostMapping("/send-email")
    public ResponseEntity<?> getNewPasswordToUser(@RequestBody MemberFindPwReqDto reqDto){
            authService.issueTempPassword(reqDto.getUsername(), reqDto.getNickname());
            return new ResponseEntity<>("Verification token has been sent to your email.", HttpStatus.OK);
        }



 */
    // 토큰발급방식 deprecated 됨
}
