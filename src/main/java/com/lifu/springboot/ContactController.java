
package com.lifu.springboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lifu.utils.DomainTools;

/**
 * @author fuluola
 *
 */
@Controller
@RequestMapping("/home")
public class ContactController {

	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private static final String domainReg = "[A-Za-z0-9\\-\\.]+";
    
	@Autowired
	private DomainRepository domainRepo;
    

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
        	
        	String batch = DomainTools.generateBatch();
        	BufferedReader in = new BufferedReader(new InputStreamReader( file.getInputStream(),"utf-8"));
          	while((line=in.readLine())!=null){
            	Pattern pattern = Pattern.compile(domainReg, Pattern.CASE_INSENSITIVE);
            	Matcher matcher = pattern.matcher(line);
            	if(matcher.matches()){
            		
            		int succRow = domainRepo.insertDomain(line,batch);
            		totalRow+=succRow;
            		if(succRow==1){
            			logger.info(line+" insert into database SUCCESS!");
            		}
            	}else{
            		logger.info(line+" format is illegal");
            	}
         	}
          	in.close();
          	resultStr="成功导入"+totalRow+"个域名!";
        }
		return resultStr;
	}

}
