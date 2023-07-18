package com.HCTR.data_portal.repository;

import com.HCTR.data_portal.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final SqlSessionTemplate sql;

    public int signup(UserDTO userDTO) {
        return sql.insert("user.signup", userDTO);
    }
    public String checkEmail(String email) {
        return sql.selectOne("user.checkEmail", email);
    }
    public String checkId(String id) {
        return sql.selectOne("user.checkId", id);
    }
    public int checkRole(String userId) {
        return sql.selectOne("user.checkRole", userId);
    }
    public UserDTO checkPW(UserDTO userDTO) {
        return sql.selectOne("user.checkPW", userDTO);
    }

}
