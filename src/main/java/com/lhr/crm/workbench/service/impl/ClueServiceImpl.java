package com.lhr.crm.workbench.service.impl;

import com.lhr.crm.utils.DateTimeUtil;
import com.lhr.crm.utils.SqlSessionUtil;
import com.lhr.crm.utils.UUIDUtil;
import com.lhr.crm.vo.PaginationVO;
import com.lhr.crm.workbench.dao.*;
import com.lhr.crm.workbench.domain.*;
import com.lhr.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/8 14:39
 * @Version 1.0
 */

public class ClueServiceImpl implements ClueService {

        private ClueDao clueDao =
                SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        private ClueActivityRelationDao clueActivityRelationDao
                = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        private ClueRemarkDao clueRemarkDao =
                SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

        //客户相关表
        private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

        //联系人相关表
        private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
        private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
        private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

        //交易相关表
        private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


        @Override
        public boolean save(Clue clue) {
                int flag = clueDao.save(clue);
                if (flag!=1){
                        return false;
                }
                else {return true;}
        }

        @Override
        public PaginationVO<Clue> pageList(HashMap<String, Object> map) {
                int total = clueDao.getTotalByCondition(map);
                List<Clue> dataList = clueDao.getClueByCondition(map);

                PaginationVO<Clue> vo = new PaginationVO<Clue>();
                vo.setTotal(total);
                vo.setDataList(dataList);

                return vo;
        }

        @Override
        public Clue detail(String id) {

                Clue clue = clueDao.detail(id);

                return clue;

        }

        @Override
        public boolean unbound(String relationId) {
                int i = clueActivityRelationDao.unbound(relationId);
                if (i==1) {
                        return true;
                }
                else {return false;}
        }

        @Override
        public List<ClueRemark> showRemark() {
               return clueDao.showRemark();
        }

        @Override
        public boolean saveRemark(ClueRemark cr) {
                return clueRemarkDao.saveRemark(cr);
        }

        @Override
        public List<Activity> getActivityListByName(String aname) {
                return clueDao.getActivityListByName(aname);
        }

        @Override
        public boolean convert(String clueId, Tran t, String createBy) {

                boolean flag = true;

                String createTime = DateTimeUtil.getSysTime();

                //获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
                Clue c = clueDao.getById(clueId);

                 //通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
                String company = c.getCompany();

                Customer cus = customerDao.getCustomerByName(company);

                if(cus==null){
                       cus = new Customer();
                       cus.setId(UUIDUtil.getUUID());
                       cus.setAddress(c.getAddress());
                       cus.setWebsite(c.getWebsite());
                       cus.setPhone(c.getPhone());
                       cus.setOwner(c.getOwner());
                       cus.setNextContactTime(c.getNextContactTime());
                       cus.setName(company);
                       cus.setDescription(c.getDescription());
                       cus.setCreateTime(createTime);
                       cus.setCreateBy(createBy);
                       cus.setContactSummary(c.getContactSummary());
                       //添加客户
                       int count1 = customerDao.save(cus);
                       if(count1!=1){
                               flag = false;
                       }
                 }

                //通过线索对象提取联系人信息，保存联系人
                Contacts con = new Contacts();
                con.setId(UUIDUtil.getUUID());
                con.setSource(c.getSource());
                con.setOwner(c.getOwner());
                con.setNextContactTime(c.getNextContactTime());
                con.setMphone(c.getMphone());
                con.setJob(c.getJob());
                con.setFullname(c.getFullname());
                con.setEmail(c.getEmail());
                con.setDescription(c.getDescription());
                con.setCustomerId(cus.getId());
                con.setCreateTime(createTime);
                con.setCreateBy(createBy);
                con.setContactSummary(c.getContactSummary());
                con.setAppellation(c.getAppellation());
                con.setAddress(c.getAddress());

                int count = contactsDao.save(con);
                if(count!=1){
                        flag = false;
                }

                //线索备注转换到客户备注以及联系人备注
                List<ClueRemark> cList = clueRemarkDao.getRemarkList(clueId);

                for (ClueRemark clueRemark:cList){
                        //取出备注信息（主要转换到客户备注和联系人备注的就是这个备注信息）
                        String noteContent = clueRemark.getNoteContent();

                        //创建客户备注对象，添加客户备注
                        CustomerRemark customerRemark = new CustomerRemark();
                        customerRemark.setId(UUIDUtil.getUUID());
                        customerRemark.setCreateBy(createBy);
                        customerRemark.setCreateTime(createTime);
                        customerRemark.setCustomerId(cus.getId());
                        customerRemark.setEditFlag("0");
                        customerRemark.setNoteContent(noteContent);
                        int count3 = customerRemarkDao.save(customerRemark);
                        if(count3!=1){
                                flag = false;
                        }

                        //创建联系人备注对象，添加联系人
                        ContactsRemark contactsRemark = new ContactsRemark();
                        contactsRemark.setId(UUIDUtil.getUUID());
                        contactsRemark.setCreateBy(createBy);
                        contactsRemark.setCreateTime(createTime);
                        contactsRemark.setContactsId(con.getId());
                        contactsRemark.setEditFlag("0");
                        contactsRemark.setNoteContent(noteContent);
                        int count4 = contactsRemarkDao.save(contactsRemark);
                        if(count4!=1){
                                flag = false;
                        }
                }

                //“线索和市场活动”的关系转换到“联系人和市场活动”的关系

                List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.getActivityListByClueId(clueId);

                for (ClueActivityRelation a: clueActivityRelations){
                        String id = UUIDUtil.getUUID();
                        ContactsActivityRelation relation = new ContactsActivityRelation();
                        relation.setId(id);
                        relation.setActivityId(a.getActivityId());
                        relation.setContactsId(con.getId());

                        int count1 = contactsActivityRelationDao.save(relation);
                        if (count1!=1){
                                flag = false;
                        }
                }

                //如果有创建交易需求，创建一条交易
                if (t!=null){

                        t.setSource(c.getSource());
                        t.setOwner(c.getOwner());
                        t.setNextContactTime(c.getNextContactTime());
                        t.setDescription(c.getDescription());
                        t.setCustomerId(cus.getId());
                        t.setContactSummary(c.getContactSummary());
                        t.setContactsId(con.getId());
                        //添加交易
                        int count6 = tranDao.save(t);
                        if(count6!=1){
                                flag = false;
                        }
                        //船舰交易历史
                        TranHistory th = new TranHistory();
                        th.setId(UUIDUtil.getUUID());
                        th.setCreateBy(createBy);
                        th.setCreateTime(createTime);
                        th.setExpectedDate(t.getExpectedDate());
                        th.setMoney(t.getMoney());
                        th.setStage(t.getStage());
                        th.setTranId(t.getId());

                        int count7 = tranHistoryDao.save(th);
                        if(count7!=1){
                                flag = false;
                        }

                }

                //(8)删除线索备注
                for(ClueRemark clueRemark : cList){
                        int count8 = clueRemarkDao.delete(clueRemark);
                        if(count8!=1){
                                flag = false;
                        }
                }

                //(9) 删除线索和市场活动的关系
                for(ClueActivityRelation clueActivityRelation : clueActivityRelations){

                        int count9 = clueActivityRelationDao.delete(clueActivityRelation);
                        if(count9!=1){
                                flag = false;
                        }
                }

                int count10 = clueDao.delById(clueId);
                if (count10!=1){
                        flag=false;
                }
                return flag;
        }

}
