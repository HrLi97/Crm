package com.lhr.crm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lhr
 * @Date:2022/4/5 10:53
 * @Version 1.0
 */
public class DateTimeUtil {
	
	public static String getSysTime(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date date = new Date();
		String dateStr = sdf.format(date);
		
		return dateStr;
		
	}
	
}
