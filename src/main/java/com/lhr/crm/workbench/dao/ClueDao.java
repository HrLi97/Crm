package com.lhr.crm.workbench.dao;

import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.Clue;
import com.lhr.crm.workbench.domain.ClueRemark;

import java.util.HashMap;
import java.util.List;

public interface ClueDao {


    int save(Clue clue);

    int getTotalByCondition(HashMap<String, Object> map);

    List<Clue> getClueByCondition(HashMap<String, Object> map);

    Clue detail(String id);

    List<ClueRemark> showRemark();

    List<Activity> getActivityListByName(String aname);

    Clue getById(String clueId);

    int delById(String clueId);
}
