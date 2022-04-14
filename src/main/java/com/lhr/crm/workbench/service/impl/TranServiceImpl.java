package com.lhr.crm.workbench.service.impl;

import com.lhr.crm.utils.DateTimeUtil;
import com.lhr.crm.utils.SqlSessionUtil;
import com.lhr.crm.utils.UUIDUtil;
import com.lhr.crm.workbench.dao.CustomerDao;
import com.lhr.crm.workbench.dao.TranDao;
import com.lhr.crm.workbench.dao.TranHistoryDao;
import com.lhr.crm.workbench.domain.Customer;
import com.lhr.crm.workbench.domain.Tran;
import com.lhr.crm.workbench.domain.TranHistory;
import com.lhr.crm.workbench.service.CustomerService;
import com.lhr.crm.workbench.service.TranService;

import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/11 19:20
 * @Version 1.0
 */
public class TranServiceImpl implements TranService {
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public boolean save(Tran t, String company) {
        //判断客户信息 无则创建

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(company);
        System.out.println(company);
        if (cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(company);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());
            //添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }
        }
        //创建新的交易信息

        t.setCustomerId(cus.getId());

        int count2 = tranDao.save(t);
        if(count2!=1){
            flag = false;
        }

        //创建交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        return tranDao.detail(id);
    }

    @Override
    public List<Tran> showHistoryList(String tranId) {
        return tranHistoryDao.showHistoryList(tranId);
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;
        int count1 = tranDao.changeStage(t);
        if (count1!=1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){

            flag = false;

        }
        return flag;
    }
}
