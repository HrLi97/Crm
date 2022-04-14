package com.lhr.crm.workbench.service.impl;

import com.lhr.crm.settings.dao.UserDao;
import com.lhr.crm.settings.domain.User;
import com.lhr.crm.settings.service.UserService;
import com.lhr.crm.utils.SqlSessionUtil;
import com.lhr.crm.utils.UUIDUtil;
import com.lhr.crm.vo.PaginationVO;
import com.lhr.crm.workbench.dao.ActivityDao;
import com.lhr.crm.workbench.dao.ActivityRemarkDao;
import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.ActivityRemark;
import com.lhr.crm.workbench.domain.ClueActivityRelation;
import com.lhr.crm.workbench.service.ActivityService;
import com.sun.org.apache.xml.internal.utils.Hashtree2Node;
import jdk.nashorn.internal.ir.Flags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/6 9:08
 * @Version 1.0
 */

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao =
            SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao =
            SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao =
            SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {
       boolean flag = true;

       int count = activityDao.save(activity);
       if (count != 1) {
           flag=false;
       }
       return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(HashMap<String, Object> map) {

        int total = activityDao.getTotalByCondition(map);

        List<Activity> dataList = activityDao.getActivityByCondition(map);

        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

    @Override
    public boolean delList(String[] ids) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByIds(ids);
//        删除备注，返回收到影响的条数（即实际删除的数量）
        int count2 = activityRemarkDao.delByIds(ids);


        if (count1!=count2){
            flag=false;
        }
        // 删除时长活动
        int count3 = activityDao.delList(ids);
        if (count3!=ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public HashMap<String, Object> getUserListAndActivity(String id) {
        List<User> uList = userDao.getUserList();
        Activity a = activityDao.getUserActivityById(id);

        HashMap map = new HashMap();
        map.put("uList",uList);
        map.put("a",a);

        return map;
    }

    @Override
    public boolean updateList(Activity a) {

        int i = activityDao.updateList(a);
        if (i!=1){
            return false;
        }
        return true;
    }

    @Override
    public Activity detail(String id) {
        Activity a = activityDao.detail(id);
        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> remarkList = activityRemarkDao.getRemarkListByAid(activityId);

        return remarkList;
    }

    @Override
    public boolean deleteRemark(String id) {
        int i = activityRemarkDao.deleteRemark(id);
        if (i==1){
            return true;
        }
        else {return false;}
    }

    @Override
    public boolean saveRemark(ActivityRemark remark) {
        boolean flag = true;

        int i = activityRemarkDao.saveRemark(remark);
        if(i!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> showActivityListByClueId(String clueId) {

        List<Activity> aList = activityDao.showActivityListByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByCondition(HashMap<String, String> map) {

        return activityDao.getActivityListByCondition(map);

    }

    @Override
    public boolean bound(String cid, String[] aids) {
        boolean flag = true;
        ClueActivityRelation relation = new ClueActivityRelation();

        for (String aid : aids){
            relation.setId(UUIDUtil.getUUID());
            relation.setClueId(cid);
            relation.setActivityId(aid);
            int i = activityDao.bound(relation);
            if (i!=1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityById(String aId) {
        return activityDao.getActivityById(aId);
    }


}
