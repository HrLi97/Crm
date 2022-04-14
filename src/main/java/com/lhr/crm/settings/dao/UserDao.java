package com.lhr.crm.settings.dao;

import com.lhr.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author lhr
 * @Date:2022/4/5 19:28
 * @Version 1.0
 */
public interface UserDao {
    User loginDao(Map<String, String> map);

    List<User> getUserList();
}

