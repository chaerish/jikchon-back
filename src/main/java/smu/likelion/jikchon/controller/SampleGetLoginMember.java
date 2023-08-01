package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.service.LoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sample")
public class SampleGetLoginMember {

    private final LoginService loginService;
    @GetMapping
    public BaseResponse<Long> getLoginMemberId() {
        return BaseResponse.ok(loginService.getLoginMemberId());
    }
}
