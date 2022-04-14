package com.lhr.crm.workbench.web.controller;

import com.lhr.crm.settings.domain.User;
import com.lhr.crm.settings.service.UserService;
import com.lhr.crm.settings.service.imp.UserServiceImpl;
import com.lhr.crm.utils.DateTimeUtil;
import com.lhr.crm.utils.PrintJson;
import com.lhr.crm.utils.ServiceFactory;
import com.lhr.crm.utils.UUIDUtil;
import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.Contacts;
import com.lhr.crm.workbench.domain.Tran;
import com.lhr.crm.workbench.service.ActivityService;
import com.lhr.crm.workbench.service.ClueService;
import com.lhr.crm.workbench.service.CustomerService;
import com.lhr.crm.workbench.service.TranService;
import com.lhr.crm.workbench.service.impl.ActivityServiceImpl;
import com.lhr.crm.workbench.service.impl.ClueServiceImpl;
import com.lhr.crm.workbench.service.impl.CustomerServiceImpl;
import com.lhr.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lhr
 * @Date:2022/4/11 19:22
 * @Version 1.0
 */
@WebServlet({"/workbench/transaction/getUserList.do","/workbench/transaction/getCustomerName.do","/workbench/transaction/searchActivity.do",
            "/workbench/transaction/addActivityToUser.do","/workbench/transaction/searchContacts.do","/workbench/transaction/addContactToTran.do",
            "/workbench/transaction/saveTran.do","/workbench/transaction/detail.do","/workbench/transaction/showHistoryList.do",
            "/workbench/transaction/changeStage.do"})
public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/transaction/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if ("/workbench/transaction/searchActivity.do".equals(path)){
            searchActivity(request,response);
        }else if ("/workbench/transaction/addActivityToUser.do".equals(path)){
            addActivityToUser(request,response);
        }else if ("/workbench/transaction/searchContacts.do".equals(path)){
            searchContacts(request,response);
        }else if ("/workbench/transaction/addContactToTran.do".equals(path)){
            addContactToTran(request,response);
        }else if ("/workbench/transaction/saveTran.do".equals(path)){
            saveTran(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/transaction/showHistoryList.do".equals(path)){
            showHistoryList(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");

        Tran t = new Tran();
        Map<String, String> pMao = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        t.setPossibility(pMao.get(stage));
        t.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        t.setEditTime(DateTimeUtil.getSysTime());
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setMoney(money);
        t.setId(id);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("t",t);

        PrintJson.printJsonObj(response,map);

    }

    private void showHistoryList(HttpServletRequest request, HttpServletResponse response) {

        String tranId = request.getParameter("tranId");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Tran> tList = ts.showHistoryList(tranId);

        Map<String, String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");

        for (Tran t:tList){
            String possibility = pMap.get(t.getStage());
            t.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response,tList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran = ts.detail(id);
        request.setAttribute("t",tran);

        Map<String, String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String company = request.getParameter("customerName"); //此处我们暂时只有客户名称，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(t,company);

        if (flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");

        }
    }

    private void addContactToTran(HttpServletRequest request, HttpServletResponse response) {

        String cId = request.getParameter("CId");
        CustomerService cus = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<Contacts> cList =  cus.getContactById(cId);

        PrintJson.printJsonObj(response,cList);

    }

    private void searchContacts(HttpServletRequest request, HttpServletResponse response) {

        String cname = request.getParameter("cname");

        CustomerService cus = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<Contacts> contactsList = cus.getContactsByName(cname);
        PrintJson.printJsonObj(response,contactsList);

    }

    private void addActivityToUser(HttpServletRequest request, HttpServletResponse response) {

        String AId = request.getParameter("AId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList =  as.getActivityById(AId);

        PrintJson.printJsonObj(response,aList);

    }

    private void searchActivity(HttpServletRequest request, HttpServletResponse response) {

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String aname = request.getParameter("aname");
        List<Activity> aList = cs.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = us.getUserList();

        request.setAttribute("userList",userList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,sList);

    }
}
