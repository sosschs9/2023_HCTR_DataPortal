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

    public int checkRole(String userId) {
        return sql.selectOne("user.checkRole", userId);
    }

}
