package com.lhr.settomhs.test;

import com.lhr.crm.utils.DateTimeUtil;
import com.lhr.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lhr
 * @Date:2022/4/5 10:53
 * @Version 1.0
 */
public class test1 {

    public static void main(String[] args) {
        String expireTime ="2022-04-03 17:53:23";

        String currentTime = DateTimeUtil.getSysTime();
        System.out.println(currentTime);
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);
       if(count>0){
           System.out.println(true);
       }else {
           System.out.println(false);
       }

        String psd = "hrhr19970315";
        String s = MD5Util.getMD5(psd);
        System.out.println(s);
    }

}
