package com.lhr.crm.workbench.dao;

import com.lhr.crm.workbench.domain.ActivityRemark;

import java.util.HashMap;
import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/6 14:17
 * @Version 1.0
 */

public interface ActivityRemarkDao {

    int getCountByIds(String[] ids);

    int delByIds(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark remark);


    int updateRemark(ActivityRemark ar);
}
