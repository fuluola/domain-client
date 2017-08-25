/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.lifu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fuluola
 *
 */
public class DomainTools {
	//二级域名，\S代表任意非空字符
	private static final String secondDomain = "(\\S+\\.){2}(com|net|org|cn|tw)";
	//一级域名
	private static final String firstDomain = "(\\S+)(.com|.net|.org|.cn|.tw){2}"; 
	/**
	 * 判断一个域名是否是二级域名 ，如 mail.qq.com是,pchome.com.cn不是
	 * @date 2017年8月16日下午10:54:15
	 * @author fuzhuan
	 * @param domain
	 * @return
	 *
	 */
	public static boolean isTwoLevelDomain(String domain) {
	  	
    	
    	Pattern secondptn = Pattern.compile(secondDomain, Pattern.CASE_INSENSITIVE);
    	Pattern firstptn = Pattern.compile(firstDomain, Pattern.CASE_INSENSITIVE);
    	Matcher secondmatcher = secondptn.matcher(domain);
    	Matcher firstmatcher = firstptn.matcher(domain);
    	if(secondmatcher.matches()&&!firstmatcher.matches()){
    		return true;
    	}
    	return false;
	}
	
	public static String generateBatch(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH0000");
		
		return format.format(new Date());
	}
	public static void main(String[] args) {
		
		System.out.println(isTwoLevelDomain("mail.com.tw"));
		System.out.println(generateBatch());
	}
}
