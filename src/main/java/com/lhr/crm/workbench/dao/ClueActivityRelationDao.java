package com.lhr.crm.workbench.dao;

import com.lhr.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbound(String relationId);

    List<ClueActivityRelation> getActivityListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}
