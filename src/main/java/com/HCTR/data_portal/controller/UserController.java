package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.UserDTO;
import com.HCTR.data_portal.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dataportal")
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute UserDTO userDTO){
        // user table 저장
        return "signIn";
    }

    @PostMapping("/signUp/email")
    public String checkEmail(String email){
        // 이메일 중복 체크
        return "redirect:/signUp";
    }

    @PostMapping("signUp/id")
    public String checkId(String id) {
        // 아이디 중복 체크
        return "redirect:/signUp";
    }

    @PostMapping("/signIn")
    public String signIn(@ModelAttribute UserDTO userDTO){
        // 유저 권한 부여
        // 만약 세션 기반 인증으로 진행하게 되면 HttpSession 부여해야함.
        return "main";
    }

    // 로그아웃
    @PostMapping("/signOut")
    public String signOut(){
        // 권한 제거
        return "main";
    }


}

