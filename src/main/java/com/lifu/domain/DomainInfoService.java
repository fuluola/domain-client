package com.lifu.domain;

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
		domainInfo.setIp(ip);
		logger.info("start collecting googlePR,domain:"+domain);
		domainInfo.setGooglePR(getGooglePR(domain));
		return respMessage;
		
	}
	
	public String getGooglePR(String domain)  {
		Runtime rt = Runtime.getRuntime();
		Process p;
		try {
			String command = "phantomjs.exe "+phantomRoot+"phantomjs-2.1.1-windows\\netsniff2.js ";
			p = rt.exec(command+PR_URL+domain);
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line=null;
			String result = "";
			while((line=br.readLine())!=null){
				result += line;
			}
			if(!StringUtils.isNullOrEmpty(result) && result.contains("http")){
				
				String pr=result.substring(result.lastIndexOf(".")-1, result.lastIndexOf("."));
				logger.info(domain +" google pr is " + pr);
				return pr;
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
