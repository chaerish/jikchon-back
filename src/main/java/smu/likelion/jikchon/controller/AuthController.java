package smu.likelion.jikchon.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.dto.member.MemberRequestDto;
import smu.likelion.jikchon.dto.member.MemberResponseDto;
import smu.likelion.jikchon.dto.member.TokenResponseDto;
import smu.likelion.jikchon.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup/customer")
    public BaseResponse<MemberResponseDto.Simple> signUpCustomer(@RequestBody MemberRequestDto.SignUp memberRequestDto) {
        return BaseResponse.ok(authService.signUpCustomer(memberRequestDto));
    }

    @PostMapping("/signup/seller")
    public BaseResponse<MemberResponseDto.Simple> signUpSeller(@RequestBody MemberRequestDto.SignUp memberRequestDto) {
        return BaseResponse.ok(authService.signUpSeller(memberRequestDto));
    }

    @PostMapping("/login")
    public BaseResponse<TokenResponseDto.AccessToken> login(HttpServletResponse response, @RequestBody MemberRequestDto.Login loginRequestDto) {
        return BaseResponse.ok(authService.login(response, loginRequestDto));
    }

    @PostMapping("/refresh")
    public BaseResponse<TokenResponseDto.AccessToken> refreshAccessToken(HttpServletRequest request) {
        return BaseResponse.ok(authService.refreshAccessToken(request));
    }
}
