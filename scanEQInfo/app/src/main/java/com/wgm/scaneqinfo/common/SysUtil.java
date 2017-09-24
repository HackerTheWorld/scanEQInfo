package com.wgm.scaneqinfo.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SysUtil {
	public static boolean isBlankString(String str){
		if(str == null || "".equals(str.trim()))
			return true;
		return false;
	}
	
	public static DateFormat getSimpleDateFormat(boolean hasTime){
		if(hasTime){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		return new SimpleDateFormat("yyyy-MM-dd");
	}
}
