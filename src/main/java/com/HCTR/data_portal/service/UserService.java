package com.HCTR.data_portal.service;

import com.HCTR.data_portal.dto.UserDTO;
import com.HCTR.data_portal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;

    public int signup(UserDTO userDTO) {
        userDTO.setRole(1);
        return userRepository.signup(userDTO);
    }
    public boolean checkEmail(String email) {
        return userRepository.checkEmail(email) == null;
    }
    public boolean checkId(String id) {
        return userRepository.checkId(id) == null;
    }
    public Object signin(UserDTO userDTO) {
        if (userRepository.checkId(userDTO.getId()) == null) return -1;

        userDTO = userRepository.checkPW(userDTO);
        if (userDTO == null) return -2;
        else return userDTO;
    }

}
