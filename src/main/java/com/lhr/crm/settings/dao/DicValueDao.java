package com.lhr.crm.settings.dao;

import com.lhr.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author lhr
 * @Date:2022/4/8 14:49
 * @Version 1.0
 */
public interface DicValueDao {
    List<DicValue> getValueListByCode(String code);
}
