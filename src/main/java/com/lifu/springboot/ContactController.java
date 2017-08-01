
package com.lifu.springboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fuluola
 *
 */
@Controller
@RequestMapping("/home")
public class ContactController {

	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    
	@Autowired
	private DomainRepository domainRepo;
    
//	@RequestMapping(method=RequestMethod.GET)
//	public String home(Map<String,Object> model){
//		List<Contact> contacts = contactRepo.findAll();
//		model.put("contacts", contacts);
//		return "home";
//	}

	@RequestMapping(value="importInit",method=RequestMethod.GET)
	public String domainImport(Map<String,Object> model){
		return "domainImport";
	}
	
	@ResponseBody
	@RequestMapping(value="importDomain",method=RequestMethod.POST)
	public String importDomain(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException{
		int totalRow = 0;
		String resultStr = "",line;
        String fileName=file.getOriginalFilename();
        logger.info("导入文件名:"+fileName);
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        if(!"txt".equals(suffix)){
        	resultStr="请导入文本格式文件";
        }else{
        	BufferedReader in = new BufferedReader(new InputStreamReader( file.getInputStream(),"utf-8"));
          	while((line=in.readLine())!=null){
          		int succRow = domainRepo.insertDomain(line);
          		totalRow+=succRow;
          		if(succRow==1){
          			logger.info(line+" 成功插入数据库!");
          		}
         	}
          	resultStr="成功导入"+totalRow+"个域名!";
        }
		return resultStr;
	}
//	
//	@RequestMapping(value="domainResult",method=RequestMethod.GET)
//	public Object domainResult(Map<String,Object> model){
//		return "pageDomainInfo";
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="domainList",method=RequestMethod.POST)
//	public Object domainList(Map<String,Object> model){
//		List<Map<String,Object>> resultMap = domainRepo.pageQueryDomainInfo(null);
//		return resultMap;
//	}
}
