package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.RequestDTO;
import com.HCTR.data_portal.dto.UserDTO;
import com.HCTR.data_portal.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dataportal")
public class UserController {
    private final HttpSession httpSession;
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO){
        System.out.println("SignUp User");
        Map<String, Object> msg = new HashMap<>();

        int res = userService.signup(userDTO);
        if (res > 0) {
            msg.put("Success", res);
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } else {
            msg.put("Error", "Signup Failure.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
    }

    // String으로 넘겨주면 JSON 형식으로 못 받아옴
    // HttpServletRequest 으로 받아와야하나? 아님 UserDTO ?
    @PostMapping("/signUp/email")
    public ResponseEntity<?> checkEmail(@RequestBody String email){
        // 이메일 중복 체크
        System.out.println("Check Email");
        Map<String, Object> msg = new HashMap<>();

        boolean res = userService.checkEmail(email);
        if (res) {
            msg.put("Success", "This Email can be use");
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } else {
            msg.put("Error", "This Email can NOT be use.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
    }

    @PostMapping("signUp/id")
    public ResponseEntity<?> checkId(@RequestBody String id) {
        // 아이디 중복 체크
        System.out.println("Check Id");
        Map<String, Object> msg = new HashMap<>();

        boolean res = userService.checkId(id);
        if (res) {
            msg.put("Success", "This Id can be use");
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } else {
            msg.put("Error", "This Id can NOT be use.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserDTO userDTO){
        // 아이디 비밀번호 일치하는지 확인
        System.out.println("Check Email");
        Map<String, Object> msg = new HashMap<>();

        // 이게 효율적인 방법인지는 모르겟네
        Object res = userService.signin(userDTO);
        if (res instanceof UserDTO) {
            // 유저 권한 부여
            // 만약 세션 기반 인증으로 진행하게 되면 HttpSession 부여해야함.
            if (((UserDTO) res).getRole() == 0)
                httpSession.setAttribute("MANAGER", res);
            else httpSession.setAttribute("USER", res);

            msg.put("UserId", ((UserDTO) res).getId());
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } else if (res.equals(-1)){
            msg.put("Error", "The ID does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        } else if (res.equals(-2)) {
            msg.put("Error", "The ID and Password do not match.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        } else {
            msg.put("Error", "Login Failure.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
    }

    // 로그아웃
//    @PostMapping("/signOut")
//    public ResponseEntity<?> signOut(){
//        // 권한 제거
//
//    }


}

