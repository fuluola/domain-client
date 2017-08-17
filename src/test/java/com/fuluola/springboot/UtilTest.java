/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.fuluola.springboot;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fuluola
 *
 */
public class UtilTest {

    private static String getTLD(String domain) {  
        final int index;  
        return (domain == null || (index = domain.lastIndexOf('.') + 1) < 1) ? domain  
                : (index < (domain.length())) ? domain.substring(index) : "";  
    }  
    
    public static void main(String[] args) throws IOException {

//    	String domain = "baidu.com".replace("www.", "");
//    	HtmlHead hh = WebUtil.getHtmlHead("http://www.mi.com");
//		System.out.println(hh.getTitle());
    	String str="qq--ww900.mld.cn";
    	String regEx = "(\\S+\\.){2}(com|net|org|cn|tw)";
    	String regEx2 = "(\\S+)(.com|.net|.org|.cn|.tw){2}"; 
    	Pattern pattern = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);

    	Matcher matcher = pattern.matcher(str);
    	    // 字符串是否与正则表达式相匹配
	    boolean rs = matcher.matches();
	    System.out.println(rs);
	}
}
