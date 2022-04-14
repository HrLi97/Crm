package com.lhr.crm.workbench.service;

import com.lhr.crm.vo.PaginationVO;
import com.lhr.crm.workbench.domain.Activity;
import com.lhr.crm.workbench.domain.Clue;
import com.lhr.crm.workbench.domain.ClueRemark;
import com.lhr.crm.workbench.domain.Tran;

import java.util.HashMap;
import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/8 14:39
 * @Version 1.0
 */
public interface ClueService {

    boolean save(Clue clue);

    PaginationVO<Clue> pageList(HashMap<String, Object> map);

    Clue detail(String id);

    boolean unbound(String relationId);

    List<ClueRemark> showRemark();

    boolean saveRemark(ClueRemark cr);

    List<Activity> getActivityListByName(String aname);

    boolean convert(String clueId, Tran t, String createBy);

}
