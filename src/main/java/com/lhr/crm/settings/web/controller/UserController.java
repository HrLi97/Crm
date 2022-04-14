package com.lhr.crm.settings.web.controller;

import com.lhr.crm.exception.LoginException;
import com.lhr.crm.settings.domain.User;
import com.lhr.crm.settings.service.UserService;
import com.lhr.crm.settings.service.imp.UserServiceImpl;
import com.lhr.crm.utils.MD5Util;
import com.lhr.crm.utils.PrintJson;
import com.lhr.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

//@SuppressWarnings("all")
/**
 * @author lhr
 * @Date:2022/4/5 19:28
 * @Version 1.0
 */
@WebServlet({"/settings/user/login.do"})
public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){
                login(request,response);
        }
    }

    public void login(HttpServletRequest request,HttpServletResponse response) throws IOException{


        String loginAct = request.getParameter("loginAct");
        String Pwd = request.getParameter("loginPwd");
        String loginPwd = MD5Util.getMD5(Pwd);
        String ip = request.getLocalAddr();

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {

            User user = us.login(loginAct, loginPwd, ip);
            request.getSession().setAttribute("user",user);

            /*
            向前端返回{"success":true}
           String json = "{\"success\":true}";
        */

            PrintJson.printJsonFlag(response,true);

        }catch (Exception e){
            e.printStackTrace();
            //向前端返回{"success":true,"msg":where}
            String msg = e.getMessage();

            /*
            作为controller，需要为ajax请求体哦概念股多项信息
            有两种方法来处理信息：
                1、将多项信息打包成map，并且将map解析成为json串
                2、创建一个vo
                    private boolean success;
                    private String msg;
             */

            HashMap<String, Object> map = new HashMap<String,Object>();
            map.put("sucess",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
