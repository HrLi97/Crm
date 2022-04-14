package com.lhr.crm.settings.service;

import com.lhr.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @author lhr
 * @Date:2022/4/8 14:54
 * @Version 1.0
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();
}
