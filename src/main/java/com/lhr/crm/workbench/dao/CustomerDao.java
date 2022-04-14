package com.lhr.crm.workbench.dao;

import com.lhr.crm.workbench.domain.Contacts;
import com.lhr.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);

    List<Contacts> getContactsByName(String cname);

    List<Contacts> getContactById(String cId);
}
