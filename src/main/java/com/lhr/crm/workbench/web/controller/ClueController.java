package com.lhr.crm.workbench.web.controller;

import com.lhr.crm.settings.domain.User;
import com.lhr.crm.settings.service.UserService;
import com.lhr.crm.settings.service.imp.UserServiceImpl;
import com.lhr.crm.utils.*;
import com.lhr.crm.vo.PaginationVO;
import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.Clue;
import com.lhr.crm.workbench.domain.ClueRemark;
import com.lhr.crm.workbench.domain.Tran;
import com.lhr.crm.workbench.service.ActivityService;
import com.lhr.crm.workbench.service.ClueService;
import com.lhr.crm.workbench.service.impl.ActivityServiceImpl;
import com.lhr.crm.workbench.service.impl.ClueServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * @author lhr
 * @Date:2022/4/8 14:43
 * @Version 1.0
 */
@WebServlet({"/workbench/clue/save.do","/workbench/clue/getUserList.do","/workbench/clue/pageList.do",
            "/workbench/clue/detail.do","/workbench/clue/showActivityListByClueId.do","/workbench/clue/unbound.do",
            "/workbench/clue/getActivityListByCondition.do","/workbench/clue/bound.do","/workbench/clue/showRemark.do",
            "/workbench/clue/saveRemark.do","/workbench/clue/getActivityListByName.do","/workbench/clue/convert.do"})

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if(("/workbench/clue/save.do").equals(path)){
            save(request,response);
        }else if (("/workbench/clue/getUserList.do").equals(path)){
            getUserList(request,response);
        }else if (("/workbench/clue/pageList.do").equals(path)){
            pageList(request,response);
        }else if (("/workbench/clue/detail.do").equals(path)){
            detail(request,response);
        }else if (("/workbench/clue/showActivityListByClueId.do").equals(path)){
            showActivityListByClueId(request,response);
        }else if (("/workbench/clue/unbound.do").equals(path)){
            unbound(request,response);
        }else if (("/workbench/clue/getActivityListByCondition.do").equals(path)){
            getActivityListByCondition(request,response);
        }else if (("/workbench/clue/bound.do").equals(path)){
            bound(request,response);
        }else if (("/workbench/clue/showRemark.do").equals(path)){
            showRemark(request,response);
        }else if (("/workbench/clue/saveRemark.do").equals(path)){
            saveRemark(request,response);
        }else if (("/workbench/clue/getActivityListByName.do").equals(path)){
            getActivityListByName(request,response);
        }else if (("/workbench/clue/convert.do").equals(path)){
            convert(request,response);
        }

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) {

        boolean flag = true;
        String a = request.getParameter("flag");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String clueId = request.getParameter("clueId");
        Tran t = null;
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        if ("a".equals(a)){
            t = new Tran();

            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }

        boolean flag1 = cs.convert(clueId,t,createBy);

        if (flag1){

            request.getRequestDispatcher(request.getContextPath()+"/workbench/clue/index.jsp");

        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String aname = request.getParameter("aname");
        List<Activity> aList = cs.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        String noteContent = request.getParameter("noteContent");
        String createBy = request.getParameter("createBy");
        String clueId = request.getParameter("clueId");
        String id = UUIDUtil.getUUID();
        String creatTime = DateTimeUtil.getSysTime();
        String editFlag = "0";

        ClueRemark cr = new ClueRemark();
        cr.setClueId(clueId);cr.setCreateBy(createBy);cr.setCreateTime(creatTime);
        cr.setId(id);cr.setEditFlag(editFlag);cr.setNoteContent(noteContent);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.saveRemark(cr);
        PrintJson.printJsonFlag(response,flag);


    }

    private void showRemark(HttpServletRequest request, HttpServletResponse response) {
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> cList= cs.showRemark();

        PrintJson.printJsonObj(response,cList);
    }

    private void bound(HttpServletRequest request, HttpServletResponse response) {
        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.bound(cid,aids);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityListByCondition(HttpServletRequest request, HttpServletResponse response) {
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("aname",aname);
        System.out.println(aname);
        map.put("clueId",clueId);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByCondition(map);
        PrintJson.printJsonObj(response,aList);
    }

    private void unbound(HttpServletRequest request, HttpServletResponse response) {

        String relationId = request.getParameter("relationId");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unbound(relationId);
        System.out.println(relationId);
        PrintJson.printJsonFlag(response,flag);

    }

    private void showActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String clueId = request.getParameter("clueId");
        List<Activity> aList = as.showActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);
        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        String mphone = request.getParameter("mphone");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String state = request.getParameter("state");
        String pageNo = request.getParameter("pageNo");
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        HashMap<String, Object> map = new HashMap<String, Object>();
        int skipCount = (Integer.parseInt(pageNo)-1)*pageSize;
        map.put("mphone",mphone);
        map.put("owner",owner);
        map.put("name",name);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("state",state);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);


        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Clue> cluePaginationVO = cs.pageList(map);
        System.out.println(cluePaginationVO.getTotal());
        PrintJson.printJsonObj(response,cluePaginationVO);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        String createTime = DateTimeUtil.getSysTime();
        String createBy =( (User)request.getSession().getAttribute("user")).getName();

        Clue clue = new Clue();
        clue.setAddress(address);clue.setAppellation(appellation);clue.setCompany(company);
        clue.setId(id);clue.setFullname(fullname);clue.setOwner(owner);clue.setJob(job);
        clue.setEmail(email);clue.setPhone(phone);clue.setWebsite(website);clue.setMphone(mphone);
        clue.setSource(source);clue.setState(state);clue.setDescription(description);
        clue.setCreateBy(createBy);clue.setCreateTime(createTime);clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setState("0");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }
}
