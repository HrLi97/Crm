package com.lhr.crm.settings.service.imp;

import com.lhr.crm.settings.dao.DicTypeDao;
import com.lhr.crm.settings.dao.DicValueDao;
import com.lhr.crm.settings.domain.DicType;
import com.lhr.crm.settings.domain.DicValue;
import com.lhr.crm.settings.service.DicService;
import com.lhr.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhr
 * @Date:2022/4/8 14:54
 * @Version 1.0
 */
public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao =
            SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao =
            SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {

        List<DicType> dicTypes = dicTypeDao.getTypeList();

        HashMap <String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
        for (DicType type:dicTypes){
            String code = type.getCode();
            List<DicValue> dicValues = dicValueDao.getValueListByCode(code);
            map.put(code,dicValues);
        }

        return map;
    }
}
