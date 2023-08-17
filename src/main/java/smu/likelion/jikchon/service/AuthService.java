package smu.likelion.jikchon.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.jikchon.domain.enumurate.SubCategory;
import smu.likelion.jikchon.domain.member.JwtRefreshToken;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.member.MemberRole;
import smu.likelion.jikchon.domain.member.VerifiedMember;
import smu.likelion.jikchon.dto.member.MemberRequestDto;
import smu.likelion.jikchon.dto.member.MemberResponseDto;
import smu.likelion.jikchon.dto.member.TokenResponseDto;
import smu.likelion.jikchon.exception.*;
import smu.likelion.jikchon.jwt.TokenProvider;
import smu.likelion.jikchon.repository.JwtRefreshTokenRepository;
import smu.likelion.jikchon.repository.MemberRepository;
import smu.likelion.jikchon.repository.VerifiedCacheRepository;
import smu.likelion.jikchon.security.JwtType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final VerifiedCacheRepository verifiedCacheRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final LoginService loginService;


    @Transactional
    public MemberResponseDto.Simple signUpCustomer(MemberRequestDto.SignUp memberRequestDto) {
        checkDuplicatePhoneNumber(memberRequestDto.getPhoneNumber());
        Member member = memberRequestDto.toCustomerEntity();
        member.encodePassword(passwordEncoder);

        return MemberResponseDto.Simple.of(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto.Simple signUpSeller(MemberRequestDto.SignUp memberRequestDto) {
        checkDuplicatePhoneNumber(memberRequestDto.getPhoneNumber());
        isValidate(memberRequestDto);

        Member member = memberRequestDto.toSellerEntity();
        member.encodePassword(passwordEncoder);

        return MemberResponseDto.Simple.of(memberRepository.save(member));

    }

    @Transactional
    public void isValidate(MemberRequestDto.SignUp memberRequestDto) {
        Optional<VerifiedMember> verifiedMemberOptional = verifiedCacheRepository.findByPhoneNumber(memberRequestDto.getPhoneNumber());

        if (verifiedMemberOptional.isPresent()) {
            VerifiedMember verifiedMember = verifiedMemberOptional.get();
            if (!Objects.equals(memberRequestDto.getCompanyNumber(), verifiedMember.getCompanyNumber())) {
                throw new CustomBadRequestException(ErrorCode.NOT_VERIFIED_COMPANY_NUMBER);
            }
            verifiedCacheRepository.delete(verifiedMember);
        } else {
            throw new CustomBadRequestException(ErrorCode.NOT_VERIFIED_COMPANY_NUMBER);
        }
    }

    @Transactional(readOnly = true)
    public void checkDuplicatePhoneNumber(String phoneNumber) {
        memberRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(member -> {
                    throw new CustomBadRequestException(ErrorCode.DUPLICATE_PHONE_NUMBER);
                });
    }


    @Transactional
    public void validationCompanyNumber(MemberRequestDto.VerifyCompanyNumber verifyCompanyNumberRequest) {
        final String VALID_STATUS_CODE = "01";
        final String requestUrl = "https://api.odcloud.kr/api/nts-businessman/v1/status?" +
                "serviceKey=bFcIfbKjGI8rVFG9xZouBt%2B3s0kITpf0u6Loz8ekrvseXj%2Bye16tUmvGrBgLdK5zbVA3cAanmNPa%2F1o%2B2n2feQ%3D%3D";

        checkDuplicatePhoneNumber(verifyCompanyNumberRequest.getPhoneNumber());

        memberRepository.findByCompanyNumber(verifyCompanyNumberRequest.getCompanyNumber()).ifPresent((member) -> {
            throw new CustomForbiddenException(ErrorCode.DUPLICATE_COMPANY_NUMBER);
        });

        verifiedCacheRepository.findByPhoneNumber(verifyCompanyNumberRequest.getPhoneNumber())
                .ifPresent(verifiedCacheRepository::delete);

        JSONObject requestBody = new JSONObject();
        requestBody.put("b_no", new String[]{verifyCompanyNumberRequest.getCompanyNumber()});

        JSONObject responseJson = callApiAndGetResponse(requestUrl, requestBody.toString());

        if (!getBusinessStatus(responseJson).equals(VALID_STATUS_CODE)) {
            throw new CustomBadRequestException(ErrorCode.NONEXISTENT_BUSINESS_REGISTRATION_CODE);
        }

        verifiedCacheRepository.save(VerifiedMember.builder()
                .phoneNumber(verifyCompanyNumberRequest.getPhoneNumber())
                .companyNumber(verifyCompanyNumberRequest.getCompanyNumber())
                .build());
    }

    private JSONObject callApiAndGetResponse(String requestUrl, String requestBody) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setDoOutput(true);

            OutputStream outputStream = urlConnection.getOutputStream();

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bufferedWriter.write(requestBody);
            bufferedWriter.flush();
            bufferedWriter.close();

            StringBuilder responseBuilder = new StringBuilder();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            } else {
                throw new CustomInternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            return new JSONObject(responseBuilder.toString());
        } catch (IOException e) {
            throw new CustomInternalServerErrorException(ErrorCode.OPEN_API_ERROR);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {
                }
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


    private String getBusinessStatus(JSONObject apiResponseJson) {
        return apiResponseJson.getJSONArray("data").getJSONObject(0).getString("b_stt_cd");
    }

    @Transactional
    public TokenResponseDto login(HttpServletResponse response, MemberRequestDto.Login loginRequestDto) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getPhoneNumber(), loginRequestDto.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenResponseDto accessToken = tokenProvider.generateTokenResponse(JwtType.ACCESS_TOKEN, authentication);

        TokenResponseDto refreshToken = tokenProvider.generateTokenResponse(JwtType.REFRESH_TOKEN, authentication);
        createOrUpdateRefreshToken(refreshToken);

        setRefreshTokenCookie(response, refreshToken);

        return accessToken;
    }

    private void createOrUpdateRefreshToken(TokenResponseDto refreshTokenDto) {
        Long memberId = tokenProvider.getMemberIdFromRefreshToken(refreshTokenDto.getToken());

        JwtRefreshToken refreshToken = jwtRefreshTokenRepository.findByMemberId(memberId)
                .orElse(JwtRefreshToken.builder().
                        member(Member.builder().id(memberId).build())
                        .build());

        refreshToken.updateRefreshToken(refreshTokenDto);
        jwtRefreshTokenRepository.save(refreshToken);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, TokenResponseDto refreshTokenDto) {

        String cookieValue = "Bearer " + refreshTokenDto.getToken();
        cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8);

        Cookie cookie = new Cookie(JwtType.REFRESH_TOKEN.getHeader(), cookieValue);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) refreshTokenDto.getExpiresIn());
        response.addCookie(cookie);
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String refreshToken = tokenProvider.getRefreshToken(request);
        JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() ->
                        new CustomUnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_EXIST));

        jwtRefreshTokenRepository.delete(jwtRefreshToken);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshAccessToken(HttpServletRequest request) {
        String refreshToken = tokenProvider.getRefreshToken(request);
        tokenProvider.validateToken(JwtType.REFRESH_TOKEN, refreshToken);

        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN)
        );

        return tokenProvider.generateTokenResponse(JwtType.ACCESS_TOKEN, member);
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Member member = memberRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND)
        );

        return User.builder()
                .username(member.getId().toString())
                .password(member.getPassword())
                .authorities(member.getAuthority())
                .build();
    }

    @Transactional
    public void registerInterestCategory(MemberRequestDto.InterestCategory interestCategoryRequestDto) {
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        Set<SubCategory> interRestCategoryList = interestCategoryRequestDto.getInterestCategory().stream().map(SubCategory::fromDescription).collect(Collectors.toSet());
        member.updateInterestCategoryList(interRestCategoryList);
    }


    public MemberResponseDto.Detail getMemberDetail() {
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        return MemberResponseDto.Detail.of(member);
    }

    @Transactional
    public void updateMember(MemberRequestDto.SignUp memberRequestDto) {
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        member.setUsername(memberRequestDto.getUserName());
        member.setEncodePassword(memberRequestDto.getPassword(), passwordEncoder);
        member.setPhoneNumber(memberRequestDto.getPhoneNumber());
        member.setZipcode(member.getZipcode());
        member.setAddress(memberRequestDto.getAddress());
        member.setCompanyNumber(member.getCompanyNumber());
//
//        if (member.getRole().equals(MemberRole.ROLE_SELLER)) {
//            if (!memberRequestDto.getCompanyNumber().equals(member.getCompanyNumber())) {
//                isValidate(memberRequestDto);
//                member.setCompanyNumber(memberRequestDto.getCompanyNumber());
//            }
//        }
    }
}
