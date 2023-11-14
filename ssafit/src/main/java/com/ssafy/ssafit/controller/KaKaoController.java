package com.ssafy.ssafit.controller;

import com.ssafy.ssafit.domain.User;
import com.ssafy.ssafit.service.kakaoService.KaKaoService;
import com.ssafy.ssafit.service.userService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class KaKaoController {

    private final KaKaoService ks;
    private final UserService userService;

    @GetMapping("/kakao/login")
    public ResponseEntity<User> getCI(@RequestParam String code, Model model, HttpServletRequest request) throws IOException {
        String access_token = ks.getToken(code);
        Map<String, Object> userInfo = ks.getUserInfo(access_token);
        model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("userInfo", userInfo);
        //ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능

        // 카카오에서 로그인 완료

        // 카카오service 에서 db 넣어주기
        // id, nickname, email
        User user = new User((Long) userInfo.get("id"), (String) userInfo.get("nickname"), (String) userInfo.get("email"), (String) userInfo.get("password"), (boolean) userInfo.get("iskakao"));
        userService.insertUser(user);
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);
        // kakaoService.createUser(model.id, model.nickname, model.account)
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/kakao/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        System.out.println("로그아웃 성공");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
