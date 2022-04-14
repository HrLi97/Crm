package com.lhr.crm.workbench.dao;

import com.lhr.crm.workbench.domain.Tran;
import com.lhr.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<Tran> showHistoryList(String tranId);
}
