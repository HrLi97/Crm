package com.lhr.crm.workbench.service;

import com.lhr.crm.workbench.domain.Contacts;

import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/11 20:41
 * @Version 1.0
 */
public interface CustomerService {
    List<String> getCustomerName(String name);

    List<Contacts> getContactsByName(String cname);

    List<Contacts> getContactById(String cId);
}
