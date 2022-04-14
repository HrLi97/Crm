package com.lhr.crm.workbench.dao;

import com.lhr.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    boolean saveRemark(ClueRemark cr);

    List<ClueRemark> getRemarkList(String clueId);

    int delete(ClueRemark clueRemark);

}
