/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.fuluola.springboot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author fuluola
 *
 */
public class QueryDomainTest {
	
	public static void main(String[] args) throws Exception {
		Runtime rt = Runtime.getRuntime();
		String command = "phantomjs.exe D:\\software\\phantomjs-2.1.1-windows\\netsniff1.js ";
		Process process = rt.exec(command+"http://pr.aizhan.com/baidu.com");
		InputStream is = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line=null;
		String result = "";
		while((line=br.readLine())!=null){
			result += line;
		}
		process.waitFor();
		br.close();
		process.destroy();
		System.out.println(result);
	}
}
