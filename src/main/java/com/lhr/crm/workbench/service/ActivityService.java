package com.lhr.crm.workbench.service;

import com.lhr.crm.vo.PaginationVO;
import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.ActivityRemark;

import java.util.HashMap;
import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/6 9:07
 * @Version 1.0
 */

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVO<Activity> pageList(HashMap<String, Object> map);

    boolean delList(String[] ids);

    HashMap<String, Object> getUserListAndActivity(String id);

    boolean updateList(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark remark);


    boolean updateRemark(ActivityRemark ar);

    List<Activity> showActivityListByClueId(String clueId);

    List<Activity> getActivityListByCondition(HashMap<String, String> map);

    boolean bound(String cid, String[] aids);

    List<Activity> getActivityById(String aId);
}
