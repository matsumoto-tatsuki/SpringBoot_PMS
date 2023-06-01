package com.example.springwebtask.dao;

import com.example.springwebtask.entity.User;
import com.example.springwebtask.from.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public User loginCheck(LoginForm loginForm) {
        System.out.println("DaoCheck");
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", loginForm.getLoginId());
        param.addValue("pass", loginForm.getPassword());
        var list = jdbcTemplate.query("SELECT login_id, password, name, role FROM users WHERE login_id = :id AND password = :pass", param, new DataClassRowMapper<>(User.class));
        return list.isEmpty() ? null : list.get(0);
    }
}
