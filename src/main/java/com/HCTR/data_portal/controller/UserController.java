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
@RequestMapping("/api")
public class UserController {
    private final HttpSession httpSession;
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO){
        System.out.println("SignUp User");

        int res = userService.signup(userDTO);
        if (res > 0) return ResponseEntity.status(HttpStatus.CREATED).body("Success");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Signup Failure");
    }

    @PostMapping("/signUp/email")
    public ResponseEntity<?> checkEmail(@RequestParam("Email") String email){
        // 이메일 중복 체크
        System.out.println("Check Email");

        boolean res = userService.checkEmail(email);
        if (res) return ResponseEntity.status(HttpStatus.OK).body("Success: This Email can be use");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: This Email can NOT be use.");
    }

    @PostMapping("signUp/id")
    public ResponseEntity<?> checkId(@RequestParam("Id") String id) {
        // 아이디 중복 체크
        System.out.println("Check Id");

        boolean res = userService.checkId(id);
        if (res) return ResponseEntity.status(HttpStatus.OK).body("Success: This Id can be use");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: This Id can NOT be use.");
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

