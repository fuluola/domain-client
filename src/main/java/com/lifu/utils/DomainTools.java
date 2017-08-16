/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.lifu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fuluola
 *
 */
public class DomainTools {

	/**
	 * 判断一个域名是否是二级域名 ，如 mail.qq.com是,pchome.com.cn不是
	 * @date 2017年8月16日下午10:54:15
	 * @author fuzhuan
	 * @param domain
	 * @return
	 *
	 */
	public static boolean isTwoLevelDomain(String domain) {
	  	String secondDomain = "(\\S+\\.){2}(com|net|org|cn|tw)";
    	String firstDomain = "(\\S+)(.com|.net|.org|.cn|.tw){2}"; 
    	Pattern secondptn = Pattern.compile(secondDomain, Pattern.CASE_INSENSITIVE);
    	Pattern firstptn = Pattern.compile(firstDomain, Pattern.CASE_INSENSITIVE);
    	Matcher secondmatcher = secondptn.matcher(domain);
    	Matcher firstmatcher = firstptn.matcher(domain);
    	if(secondmatcher.matches()&&!firstmatcher.matches()){
    		return true;
    	}
    	return false;
	}
	
	public static void main(String[] args) {
		
		System.out.println(isTwoLevelDomain("mail.com.tw"));
		String str = "qq.r333344vv.cn";
		System.out.println(str.substring(str.indexOf(".")+1));
	}
}
