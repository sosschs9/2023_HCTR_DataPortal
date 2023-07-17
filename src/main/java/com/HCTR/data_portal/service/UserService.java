package com.HCTR.data_portal.service;

import com.HCTR.data_portal.dto.UserDTO;
import com.HCTR.data_portal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
