package smu.likelion.jikchon.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.dto.member.MemberRequestDto;
import smu.likelion.jikchon.dto.member.TokenResponseDto;
import smu.likelion.jikchon.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public BaseResponse<TokenResponseDto> signUp(@RequestBody MemberRequestDto.SignUp memberRequestDto) {
        return BaseResponse.ok(authService.signUp(memberRequestDto));
    }

    @PostMapping("/login")
    public BaseResponse<TokenResponseDto> login(@RequestBody MemberRequestDto.Login loginRequestDto) {
        return BaseResponse.ok(authService.login(loginRequestDto));
    }
}
