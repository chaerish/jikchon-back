package smu.likelion.jikchon.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/phone-number")
    public BaseResponse<Void> checkDuplicatePhoneNumber(@RequestBody MemberRequestDto.PhoneNumber memberRequestDto) {
        authService.checkDuplicatePhoneNumber(memberRequestDto.getPhoneNumber());
        return BaseResponse.ok();
    }

    @PostMapping("/auth-company")
    public BaseResponse<Void> verifyCompanyNumber(@RequestBody MemberRequestDto.VerifyCompanyNumber verifyCompanyNumberRequest) {
        authService.validationCompanyNumber(verifyCompanyNumberRequest);
        return BaseResponse.ok();
    }

    @PostMapping("/login")
    public BaseResponse<TokenResponseDto> login(HttpServletResponse response, @RequestBody MemberRequestDto.Login loginRequestDto) {
        return BaseResponse.ok(authService.login(response, loginRequestDto));
    }

    @PostMapping("/interest")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<Void> registerInterestCategory(@RequestBody MemberRequestDto.InterestCategory interestCategoryRequestDto) {
        authService.registerInterestCategory(interestCategoryRequestDto);
        return BaseResponse.ok();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<MemberResponseDto.Detail> getMemberDetail() {
        return BaseResponse.ok(authService.getMemberDetail());
    }


    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<Void> update(@RequestBody MemberRequestDto.SignUp memberRequestDto) {
        authService.updateMember(memberRequestDto);
        return BaseResponse.ok();
    }

    @PostMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<TokenResponseDto> refreshAccessToken(HttpServletRequest request) {
        return BaseResponse.ok(authService.refreshAccessToken(request));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return BaseResponse.ok();
    }
}
