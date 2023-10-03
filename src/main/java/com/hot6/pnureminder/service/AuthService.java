package com.hot6.pnureminder.service;

import com.hot6.pnureminder.dto.Member.LoginDto;
import com.hot6.pnureminder.dto.Member.MemberRequestDto;
import com.hot6.pnureminder.dto.Member.MemberResponseDto;
import com.hot6.pnureminder.dto.Token.TokenReqDto;
import com.hot6.pnureminder.dto.Token.TokenResDto;
import com.hot6.pnureminder.entity.Member;
import com.hot6.pnureminder.entity.RefreshToken;
import com.hot6.pnureminder.entity.Role;
import com.hot6.pnureminder.exception.CustomException;
import com.hot6.pnureminder.jwt.TokenProvider;
import com.hot6.pnureminder.repository.MemberRepository;
import com.hot6.pnureminder.repository.RefreshTokenRepository;
import com.hot6.pnureminder.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

import static com.hot6.pnureminder.exception.ErrorCode.ALREADY_SAVED_EMAIL;
import static com.hot6.pnureminder.exception.ErrorCode.USER_NOT_FOUND;
import static com.hot6.pnureminder.entity.Role.RoleName.ROLE_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

//    private final SmtpEmailService smtpEmailService;
//    private final MemberService memberService;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
            Role userRole = roleRepository.findByName(ROLE_USER).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );

        if (memberRepository.existsByUsername(memberRequestDto.getUsername())) {
            throw new CustomException(ALREADY_SAVED_EMAIL);
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        member.setRoles(Collections.singleton(userRole));

            return MemberResponseDto.toDto(memberRepository.save(member));

    }

    @Transactional
    public TokenResDto login(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResDto tokenResDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
            .key(authentication.getName())
            .value(tokenResDto.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenResDto;
    }

    @Transactional
    public void logout(TokenReqDto tokenReqDto) {
        // 로그아웃하려는 사용자의 정보를 가져옴
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());

        // 저장소에서 해당 사용자의 refresh token 삭제
        refreshTokenRepository.deleteByKey(authentication.getName());
    }


    @Transactional
    public TokenResDto reissue(TokenReqDto tokenReqDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenReqDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
            .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenReqDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResDto tokenResDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenResDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenResDto;
    }
/*
    @Transactional
    public void issueTempPassword(String username, String nickname) {
        Member member = memberService.findMemberByUsername(username);

        if(!member.getNickname().equals(nickname)){
            throw new RuntimeException("입력한 닉네임이 사용자의 닉네임과 일치하지 않습니다.");
        }

        String tempPassword = generateTempPassword();
        member.setPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(member);

        smtpEmailService.sendTempPassword(member.getUsername(), tempPassword);
    }

 */


    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);

    }

}
