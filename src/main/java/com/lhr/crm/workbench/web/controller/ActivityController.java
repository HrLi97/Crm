package com.lhr.crm.workbench.web.controller;

import com.lhr.crm.settings.domain.User;
import com.lhr.crm.settings.service.UserService;
import com.lhr.crm.settings.service.imp.UserServiceImpl;
import com.lhr.crm.utils.*;
import com.lhr.crm.vo.PaginationVO;
import com.lhr.crm.workbench.dao.ActivityDao;
import com.lhr.crm.workbench.dao.ActivityRemarkDao;
import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.ActivityRemark;
import com.lhr.crm.workbench.service.ActivityService;
import com.lhr.crm.workbench.service.impl.ActivityServiceImpl;

import javax.lang.model.element.NestingKind;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhr
 * @Date:2022/4/6 9:11
 * @Version 1.0
 */

@WebServlet({"/workbench/activity/getUserList.do","/workbench/activity/save.do",
        "/workbench/activity/pageList.do","/workbench/activity/delList.do",
        "/workbench/activity/getUserListAndActivity.do","/workbench/activity/updateList.do",
        "/workbench/activity/detail.do","/workbench/activity/getRemarkListByAid.do",
        "/workbench/activity/deleteRemark.do","/workbench/activity/saveRemark.do",
        "/workbench/activity/updateRemark.do"})
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String path = request.getServletPath();
            if ("/workbench/activity/getUserList.do".equals(path)){
                getUserList(request, response);
            }else if ("/workbench/activity/save.do".equals(path)){
                save(request, response);
            }else if ("/workbench/activity/pageList.do".equals(path)){
                pageList(request, response);
            }else if ("/workbench/activity/delList.do".equals(path)){
                delList(request, response);
            }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
                getUserListAndActivity(request, response);
            }else if ("/workbench/activity/updateList.do".equals(path)){
                updateList(request, response);
            }else if ("/workbench/activity/detail.do".equals(path)){
                detail(request, response);
            }else if ("/workbench/activity/getRemarkListByAid.do".equals(path)){
                getRemarkListByAid(request, response);
            }else if ("/workbench/activity/deleteRemark.do".equals(path)){
                deleteRemark(request, response);
            }else if ("/workbench/activity/saveRemark.do".equals(path)){
                saveRemark(request, response);
            }else if ("/workbench/activity/updateRemark.do".equals(path)){
                updateRemark(request, response);
            }
    }

    /**
     保存修改的备注信息
     */
    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();

        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(ar);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response, map);
    }

    /**
     保存新添加的备注信息
     */
    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String editFlag = "0";
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        ActivityRemark remark = new ActivityRemark();
        remark.setActivityId(activityId);remark.setCreateTime(createTime);
        remark.setId(id);remark.setEditFlag(editFlag);remark.setNoteContent(noteContent);
        remark.setCreateBy(createBy);

        HashMap<String, Object> map = new HashMap<String, Object>();

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.saveRemark(remark);
        map.put("ar",remark);
        map.put("success",flag);
        PrintJson.printJsonFlag(response,flag);

    }

    /**
     删除详情页面中的备注信息
     */
    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = request.getParameter("id");
        boolean flag = as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    /**
     详情页面中备注的展示
     */
    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark>  remarkList = as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response,remarkList);
    }

    /**
     详情页面的展示
     */
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = as.detail(id);

        request.setAttribute("a",a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);

    }

    /**
     进行用户主动的修改保存信息请求
     */
    private void updateList(HttpServletRequest request, HttpServletResponse response) {
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String id = request.getParameter("id");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setName(name);
        a.setOwner(owner);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setId(id);
        a.setEditBy(editBy);
        a.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.updateList(a);
        PrintJson.printJsonFlag(response,flag);
    }

    /**
     进行用户主动的修改请求
     */
    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        HashMap<String,Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }
    /**
     进行用户主动的删除请求
     */

    private void delList(HttpServletRequest request, HttpServletResponse response) throws IOException {



        String ids[] = request.getParameterValues("id");
        System.out.println(ids[0]);
        System.out.println(ids.length);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delList(ids);
        PrintJson.printJsonFlag(response,flag);



    }
    /**
     进行活动列表详细信息的展示
     */
    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.parseInt(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.parseInt(pageSizeStr);
        int skipCount = (pageNo-1)*pageSize;
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVO<Activity> paginationVO = as.pageList(map);
        PrintJson.printJsonObj(response,paginationVO);

    }

    /**
     添加用户数据保存到sql
*/
    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = null;
        String editBy = null;

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setEditBy(editBy);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        activity.setCost(cost);
        activity.setEditTime(editTime);


        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.save(activity);

        PrintJson.printJsonFlag(response,flag);
    }
/**
    获取用户列表，固定所有者的名单
 */
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

       UserService as = (UserService) ServiceFactory.getService(new UserServiceImpl());

       List<User> userList = as.getUserList();

       PrintJson.printJsonObj(response,userList);

    }

}
