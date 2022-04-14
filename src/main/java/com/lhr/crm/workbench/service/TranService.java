package com.lhr.crm.workbench.service;

import com.lhr.crm.workbench.domain.Tran;

import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/11 19:20
 * @Version 1.0
 */
public interface TranService {

    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<Tran> showHistoryList(String tranId);

    boolean changeStage(Tran t);
}
