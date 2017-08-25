package com.lifu.domain;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lifu.model.DomainObject;
import com.lifu.model.HtmlHead;
import com.lifu.model.QueryDomainRespMessage;
import com.lifu.utils.WebUtil;
import com.mysql.jdbc.StringUtils;

/** 
 * @description 
 * @author  fuzhuan fu.luola@qq.com
 * @date 2017年7月4日 
 */
@Service
public class DomainInfoService {

	private static Logger logger = LoggerFactory.getLogger(DomainInfoService.class);
	
	private String PR_URL = "http://pr.chinaz.com/";
	@Value("${phantom.path}")
	private String phantomRoot;
	@Value("${getPR}")
	private Boolean getPR;
	@Autowired
	private NsLookup nslookUp;
	
	@Autowired
	private WhoisService whois;
	
	public QueryDomainRespMessage domainInfoQuery(String domain) {
		logger.info("start collecting whois info,domain:"+domain);
		QueryDomainRespMessage respMessage = whois.query(domain);
		if(respMessage==null){
			logger.info(domain+":can not find whois info!!");
			return null;
			
		}
		if(respMessage.getCode()==null || respMessage.getDomainObject()==null){
			logger.info(domain+":can not find whois info!!");
			return null;
		}
		DomainObject domainInfo = respMessage.getDomainObject();
		domainInfo.setDomainName(domain);
		logger.info("start collecting ip address info,domain:"+domain);
		String ip = nslookUp.lookUpIP(domain);
		domainInfo.setIp(ip);
		if(ip!=null){
			String address = nslookUp.getAddressCityByIp("ip="+ip);
			domainInfo.setIpAddress(address);
		}
		logger.info("start collecting website info,domain:"+domain);
		HtmlHead hh = WebUtil.getHtmlHead(domain);
		if(hh!=null){
			domainInfo.setTitle(hh.getTitle());
			domainInfo.setKeywords(hh.getKeywords());
			domainInfo.setDescription(hh.getDescription());
		}
		
		if(getPR){
			logger.info("start collecting googlePR,domain:"+domain);
			domainInfo.setGooglePR(getGooglePR(domain));
		}
		return respMessage;
		
	}
	
	public String getGooglePR(String domain)  {
		Runtime rt = Runtime.getRuntime();
		Process process;
		try {
			String command = "phantomjs.exe "+phantomRoot+"phantomjs-2.1.1-windows\\netsniff2.js ";
			process = rt.exec(command+PR_URL+domain);
		    /*为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞*/  
            Thread t=new Thread(new InputStreamRunnable(process.getErrorStream(),domain));  
            t.start();  

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
			if(!StringUtils.isNullOrEmpty(result) && result.contains("http")){
				
				String pr=result.substring(result.lastIndexOf(".")-1, result.lastIndexOf("."));
				logger.info(domain +" google pr is " + pr);
				return pr;
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**读取InputStream的线程*/  
	class InputStreamRunnable implements Runnable  { 
	 
	    BufferedReader bReader=null;  
	    String type=null;  
	    public InputStreamRunnable(InputStream is, String domain)  
	    {  
	        try  
	        {  
	            bReader=new BufferedReader(new InputStreamReader(new BufferedInputStream(is),"UTF-8"));  
	            type=domain;  
	        }  
	        catch(Exception ex)  
	        {  
	        }  
	    }  
	    public void run()  
	    {  
	        String line;  
	        try  
	        {  
	            while((line=bReader.readLine())!=null)  
	            {  
	                logger.info(type+":get pr error:"+line);
	            }  
	            bReader.close();  
	        }  
	        catch(Exception ex)  
	        {  
	        }  
	    }  
	}  
}
