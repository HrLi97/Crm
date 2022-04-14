package com.lhr.crm.workbench.dao;

import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.ClueActivityRelation;

import java.util.HashMap;
import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/6 9:02
 * @Version 1.0
 */
public interface ActivityDao {

    int save(Activity activity);

    List<Activity> getActivityByCondition(HashMap<String, Object> map);

    int getTotalByCondition(HashMap<String, Object> map);

    int delList(String[] ids);

    Activity getUserActivityById(String id);

    int updateList(Activity a);

    Activity detail(String id);

    List<Activity> showActivityListByClueId(String clueId);

    List<Activity> getActivityListByCondition(HashMap<String, String> map);

    int bound(ClueActivityRelation relation);

    List<Activity> getActivityById(String aId);
}
