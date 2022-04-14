package com.lhr.crm.workbench.service.impl;

import com.lhr.crm.utils.SqlSessionUtil;
import com.lhr.crm.workbench.dao.CustomerDao;
import com.lhr.crm.workbench.dao.CustomerRemarkDao;
import com.lhr.crm.workbench.domain.Contacts;
import com.lhr.crm.workbench.service.CustomerService;

import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/11 20:41
 * @Version 1.0
 */
public class CustomerServiceImpl implements CustomerService {
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);


    @Override
    public List<String> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }

    @Override
    public List<Contacts> getContactsByName(String cname) {
        return customerDao.getContactsByName(cname);
    }

    @Override
    public List<Contacts> getContactById(String cId) {
        return customerDao.getContactById(cId);
    }
}
