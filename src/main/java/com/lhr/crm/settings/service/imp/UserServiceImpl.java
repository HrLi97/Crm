package com.lhr.crm.settings.service.imp;

import com.lhr.crm.exception.LoginException;
import com.lhr.crm.settings.dao.UserDao;
import com.lhr.crm.settings.domain.User;
import com.lhr.crm.settings.service.UserService;
import com.lhr.crm.utils.DateTimeUtil;
import com.lhr.crm.utils.SqlSessionUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhr
 * @Date:2022/4/5 19:28
 * @Version 1.0
 */

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        map.put("ip",ip);

        User user = userDao.loginDao(map);

        if (user==null){
            throw new LoginException("账号密码错误");
        }

        //验证其他信息
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }

        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已经锁定");
        }

        String ips = user.getAllowIps();
        if (!ips.contains(ip)){
            throw new LoginException("ip受限");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        return userDao.getUserList();
    }
}
