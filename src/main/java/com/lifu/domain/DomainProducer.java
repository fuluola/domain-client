/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.lifu.domain;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifu.springboot.DomainRepository;

/**
 * @author fuluola
 *
 */
public class DomainProducer implements Runnable{

	private static Logger logger = LoggerFactory.getLogger(DomainProducer.class);
	
	private DomainHandler domainHandler;
	
	private DomainRepository domainRepo;
	
	public DomainProducer(DomainHandler handler,DomainRepository domainRepo){
		this.domainHandler = handler;
		this.domainRepo = domainRepo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true){
			logger.info("starting to put domain to queue...");
			List<Map<String,Object>> list = domainRepo.queryProcessDomain();
			if(list!=null){
				domainHandler.addDomain2Queue(list);
			}else{
				logger.info("there is no domain to process...");
			}
			logger.info(" left domain to process：[ "+domainHandler.queueInfotoString()+" ]");
			try {
				//休眠  second s
				int second = 1*60;
				TimeUnit.SECONDS.sleep(second);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
