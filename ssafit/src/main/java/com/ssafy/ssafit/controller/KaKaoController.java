package com.ssafy.ssafit.controller;

import com.ssafy.ssafit.service.kakaoService.KaKaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class KaKaoController {

    private final KaKaoService ks;

    @GetMapping("/do")
    public String loginPage()
    {

        return "kakaoCI/login";
    }

    @GetMapping("/member/kakao")
    public String getCI(@RequestParam String code, Model model) throws IOException {
        System.out.println("code = " + code);
        String access_token = ks.getToken(code);
        Map<String, Object> userInfo = ks.getUserInfo(access_token);
        model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("userInfo", userInfo);
        System.out.println("userInfo = " + userInfo);
        //ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능
        return "index";
    }
    @GetMapping("/member/do")
    public String logout(){
        return "index";
    }


}
