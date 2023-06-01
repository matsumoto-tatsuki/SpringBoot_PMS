package com.example.springwebtask.service;

import com.example.springwebtask.dao.UserDao;
import com.example.springwebtask.entity.User;
import com.example.springwebtask.from.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User loginCheck(LoginForm loginForm) {
        return userDao.loginCheck(loginForm);
    }
}
