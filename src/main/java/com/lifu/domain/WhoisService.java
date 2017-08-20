package com.lifu.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lifu.model.Constants;
import com.lifu.model.DomainObject;
import com.lifu.model.QueryDomainRespMessage;
import com.lifu.utils.DomainTools;
import com.lifu.utils.ParseResultDomainInfo;

@Service
public class WhoisService {  

    private static final int DEFAULT_PORT = 43;  
   // private static Logger logger = LoggerFactory.getLogger(WhoisService.class);

    private static Logger logger = LoggerFactory.getLogger(WhoisService.class);
      //grs-whois.hichina.com whois.paycenter.com.cn whois.markmonitor.com whois.verisign-grs.com
    public QueryDomainRespMessage query(String domain)  {  
    	
    	if(domain.contains("www.")){
    		domain = domain.replace("www.", "").trim();
    	}else if(domain.contains("http://www.")){
    		domain = domain.replace("http://www.", "").trim();
    	}
    	if(DomainTools.isTwoLevelDomain(domain)){
    		domain = domain.substring(domain.indexOf(".")+1);
    	}
        String server = "";  
        String tld = getTLD(domain);  
        if ("com".equals(tld)) {  
        	server = "whois.verisign-grs.com";
        	return queryComWhoisServer(domain, server);  
        } else if ("net".equals(tld)) {  
            server = "whois.networksolutions.com";  
            return queryNetWhoisServer(domain, server);  
        } else if ("org".equals(tld)) {  
            server = "whois.pir.org";  
            return queryOrgWhoisServer(domain, server);  
        } else if ("cn".equals(tld)) {  
            server = "whois.cnnic.cn";  
            return queryCnWhoisServer(domain, server);  
        }  else if ("tw".equals(tld)) {  
            server = "whois.twnic.net.tw";  
            return queryTWWhoisServer(domain, server);
        }else{
        	QueryDomainRespMessage respMsg = new QueryDomainRespMessage();
        	respMsg.setCode(Constants.FAIL);
        	respMsg.setExceptionMsg("不支持的域名类型"+tld);
        	return respMsg;
        }
      
    }  
    private QueryDomainRespMessage queryOrgWhoisServer(String domain,
			String server) {
    	 Socket socket = new Socket();  
         SocketAddress  remoteAddr=new InetSocketAddress(server, DEFAULT_PORT);
         PrintWriter out = null;
         String lineSeparator = "\r\n",line="";  
         StringBuilder ret = new StringBuilder();  
         DomainObject obj = new DomainObject();
         QueryDomainRespMessage respMsg = new QueryDomainRespMessage();
     	 obj.setDomainName(domain);
         try {
 			socket.connect(remoteAddr, 15*1000);
 			socket.setSoTimeout(20 * 1000);  
         	out = new PrintWriter(socket.getOutputStream());  
         	out.println(domain);  
         	out.flush(); 
         	BufferedReader in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
         	while((line=in2.readLine())!=null){
         		ret.append(line + lineSeparator);  
         		ParseResultDomainInfo.parseOrgDomainInfo(obj, line);
         	}
         	respMsg.setCode(Constants.SUCCESS);
         	respMsg.setDomainObject(obj);
         	respMsg.setSuccResultStr(ret.toString());
 		} catch (IOException e) {
 			respMsg.setCode(Constants.FAIL);
 			respMsg.setExceptionMsg(e.getMessage());
 			e.printStackTrace();
 		}finally{
 		   	out.close();
         	try {
 				socket.close();
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 		}
         return respMsg;  
	}
    /**
     * 查询台湾域名whois信息
     * @date 2017年8月16日下午4:06:33
     * @author fuzhuan
     * @param domain
     * @param server
     * @return
     *
     */
    private QueryDomainRespMessage queryTWWhoisServer(String domain,String server) {
			
    	 Socket socket = new Socket();  
         SocketAddress  remoteAddr=new InetSocketAddress(server, DEFAULT_PORT);
         PrintWriter out = null;
         String line="";  
         DomainObject obj = null;
         QueryDomainRespMessage respMsg = new QueryDomainRespMessage();
     	 
         try {
 			socket.connect(remoteAddr, 15*1000);
 			socket.setSoTimeout(20 * 1000);  
         	out = new PrintWriter(socket.getOutputStream());  
         	out.println(domain);  
         	out.flush(); 
         	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
         	obj = new DomainObject();
         	obj.setDomainName(domain);
         	int point=0;
         	int pointRegistrant = 0;
         	int pointDNS = 0;
         	int pointEmail = 0;
         	Map<String,String> valueMap = new HashMap<String,String>();
         	while((line=in.readLine())!=null){
         		line = line.trim();
         		++point;
         		if("Registrant:".equals(line)){
         			pointRegistrant=point;
         		}
         		if(line.startsWith("Domain servers in listed order:")){
         			pointDNS=point;
         		}
         		if(line.contains("Contact:")){
         			pointEmail=point;
         		}
         		if(pointRegistrant+1==point){
         			valueMap.put("registrant", line);
         		}else if(pointEmail+1==point){
         			valueMap.put("registrantName",line.split("   ")[0]);
         			valueMap.put("email", line.split("   ")[1]);
         		}else if(pointDNS+1==point){
         			valueMap.put("dns", line);
         		}else if(pointDNS+2==point){
         			valueMap.put("ns", line);
         		}
         		//ret.append(line + lineSeparator);  
         		ParseResultDomainInfo.parseTWDomainInfo(obj, line);
         	}
         	obj.setRegistrantName(valueMap.get("registrantName"));
         	obj.setRegistrantEmail(valueMap.get("email"));
         	obj.setRegistrantOrganization(valueMap.get("registrant"));
         	obj.setDnsServer(valueMap.get("dns"));
         	obj.setNsServer(valueMap.get("ns"));
         	respMsg.setCode(Constants.SUCCESS);
         	respMsg.setDomainObject(obj);

 		} catch (IOException e) {
 			respMsg.setCode(Constants.FAIL);
 			respMsg.setExceptionMsg(e.getMessage());
 			e.printStackTrace();
 		}finally{
 		   	out.close();
         	try {
 				socket.close();
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 		}
         return respMsg;  
	}
	/**
     * 查询.com域名whois信息
     * @date 2017年7月2日上午11:27:09
     * @author fuzhuan
     * @param domain
     * @param server
     * @return
     *
     */
    public QueryDomainRespMessage queryComWhoisServer(String domain, String server) {  
        Socket socket = new Socket();  
        SocketAddress  remoteAddr=new InetSocketAddress(server, DEFAULT_PORT);
        PrintWriter out = null;
        String lineSeparator = "\r\n",line="";  
        String whoisServer=null;
        StringBuilder ret = new StringBuilder();  
        DomainObject obj = new DomainObject();
        QueryDomainRespMessage respMsg = new QueryDomainRespMessage();
        try {
			socket.connect(remoteAddr, 15*1000);
			socket.setSoTimeout(15 * 1000);  
		    out = new PrintWriter(socket.getOutputStream());  
		    out.println(domain);  
		    out.flush();  
		    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
	        while ((line = in.readLine()) != null) {
	        	if(line.toLowerCase().contains("Whois Server:".toLowerCase())){
	        		whoisServer=line.substring(line.indexOf(":")+1).trim();
	        		break;
	        	}
	        }  
	        socket.close(); 
		} catch (IOException e) {
			respMsg.setCode(Constants.FAIL);
			respMsg.setExceptionMsg(e.getMessage());
			e.printStackTrace();
		}
        if(whoisServer!=null){
        	
        	obj.setDomainName(domain);
            Socket socket2 = new Socket();  
            SocketAddress  remoteAddr2=new InetSocketAddress(whoisServer, DEFAULT_PORT);
            try {
				socket2.connect(remoteAddr2, 15*1000);
				socket2.setSoTimeout(20 * 1000);  
	        	out = new PrintWriter(socket2.getOutputStream());  
	        	out.println(domain);  
	        	out.flush(); 
	        	BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));  
	        	while((line=in2.readLine())!=null){
	        		ret.append(line + lineSeparator);  
	        		ParseResultDomainInfo.parseComDomainInfo(obj, line);
	        	}
	        	respMsg.setCode(Constants.SUCCESS);
	        	respMsg.setDomainObject(obj);
	        	respMsg.setSuccResultStr(ret.toString());
			} catch (IOException e) {
				respMsg.setCode(Constants.FAIL);
				respMsg.setExceptionMsg(e.getMessage());
				e.printStackTrace();
			}finally{
			   	out.close();
	        	try {
					socket2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }
        return respMsg;  
    }
    
    public QueryDomainRespMessage queryCnWhoisServer(String domain, String server) {  
        Socket socket = new Socket();  
        SocketAddress  remoteAddr=new InetSocketAddress(server, DEFAULT_PORT);
        PrintWriter out = null;
        String lineSeparator = "\r\n",line="";  
        StringBuilder ret = new StringBuilder();  
        DomainObject obj = new DomainObject();
        QueryDomainRespMessage respMsg = new QueryDomainRespMessage();
    	obj.setDomainName(domain);
        try {
			socket.connect(remoteAddr, 15*1000);
			socket.setSoTimeout(20 * 1000);  
        	out = new PrintWriter(socket.getOutputStream());  
        	out.println(domain);  
        	out.flush(); 
        	BufferedReader in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
        	while((line=in2.readLine())!=null){
        		ret.append(line + lineSeparator);  
        		ParseResultDomainInfo.parseCnDomainInfo(obj, line);
        	}
        	respMsg.setCode(Constants.SUCCESS);
        	respMsg.setDomainObject(obj);
        	respMsg.setSuccResultStr(ret.toString());
		} catch (IOException e) {
			respMsg.setCode(Constants.FAIL);
			respMsg.setExceptionMsg(e.getMessage());
			e.printStackTrace();
		}finally{
		   	out.close();
        	try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return respMsg;  
    }  
    public QueryDomainRespMessage queryNetWhoisServer(String domain, String server) {  
        Socket socket = new Socket();  
        SocketAddress  remoteAddr=new InetSocketAddress(server, DEFAULT_PORT);
        PrintWriter out = null;
        String lineSeparator = "\r\n",line="";  
        StringBuilder ret = new StringBuilder();  
        DomainObject obj = new DomainObject();
        QueryDomainRespMessage respMsg = new QueryDomainRespMessage();
    	obj.setDomainName(domain);
        try {
			socket.connect(remoteAddr, 15*1000);
			socket.setSoTimeout(20 * 1000);  
        	out = new PrintWriter(socket.getOutputStream());  
        	out.println(domain);  
        	out.flush(); 
        	BufferedReader in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
        	while((line=in2.readLine())!=null){
        		ret.append(line + lineSeparator);  
        		ParseResultDomainInfo.parseComDomainInfo(obj, line);
        	}
        	respMsg.setCode(Constants.SUCCESS);
        	respMsg.setDomainObject(obj);
        	respMsg.setSuccResultStr(ret.toString());
		} catch (IOException e) {
			respMsg.setCode(Constants.FAIL);
			respMsg.setExceptionMsg(e.getMessage());
			e.printStackTrace();
		}finally{
		   	out.close();
        	try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return respMsg;  
    }  
    
    public String queryWhoisServer(String domain,String server) throws UnknownHostException, IOException {
    	String lineSeparator = "\r\n";  
    	Socket socket = new Socket(server, DEFAULT_PORT);  
    	PrintWriter out = new PrintWriter(socket.getOutputStream());  
    	out = new PrintWriter(socket.getOutputStream());  
    	out.println(domain);  
    	out.flush(); 
    	String line;
    	StringBuffer ret = new StringBuffer();
    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
    	while((line=in.readLine())!=null){
    		ret.append(line.trim() + lineSeparator);  
    	}
    	out.close();
    	socket.close();
    	return ret.toString();
    }
    
    private String getTLD(String domain) {  
        final int index;  
        return (domain == null || (index = domain.lastIndexOf('.') + 1) < 1) ? domain  
                : (index < (domain.length())) ? domain.substring(index) : "";  
    }  
      
    public static void main(String[] args) throws Exception {  
    	WhoisService w = new WhoisService();  
      //  System.out.println(w.query("spring.io")); 
        long start=System.currentTimeMillis();
        //QueryDomainRespMessage  grs-whois.hichina.com
        String msg = w.queryWhoisServer("yaoze168.com", "whois.verisign-grs.com");
       // QueryDomainRespMessage msg = w.query("yaoze168.com");
        System.out.println(msg);
    }  
      
}  