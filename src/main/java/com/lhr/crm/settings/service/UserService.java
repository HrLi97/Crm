package com.lhr.crm.settings.service;

import com.lhr.crm.exception.LoginException;
import com.lhr.crm.settings.domain.User;

import java.util.List;

public interface UserService{

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
