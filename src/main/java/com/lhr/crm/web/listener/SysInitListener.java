package com.lhr.crm.web.listener;

import com.lhr.crm.settings.domain.DicValue;
import com.lhr.crm.settings.service.DicService;
import com.lhr.crm.settings.service.imp.DicServiceImpl;
import com.lhr.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @author lhr
 * @Date:2022/4/8 16:38
 * @Version 1.0
 */

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {

        ServletContext application = event.getServletContext();
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }


        //将阶段和可能性的配置文件存放在application域中
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = bundle.getKeys();
        HashMap<String, String> pMap = new HashMap<String, String>();

        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = bundle.getString(key);

            pMap.put(key,value);
        }

        application.setAttribute("pMap",pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
