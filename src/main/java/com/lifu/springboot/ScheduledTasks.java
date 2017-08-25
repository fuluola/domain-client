package com.lifu.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author fuluola
 *
 */
@Component
@Configurable
@EnableScheduling
public class ScheduledTasks{
	
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	@Autowired
	private DomainRepository domainRepo;
	//10分钟执行一次
    @Scheduled(fixedRate = 1000 * 60*15)
    public void reportCurrentTime(){
    	logger.info("batch domain collecting begin...");
    	domainRepo.processBatchDomain();
    	logger.info("batch domain collecting over...");
    }

    //每1分钟执行一次
//    @Scheduled(cron = "0 */1 *  * * * ")
//    public void reportCurrentByCron(){
//        System.out.println ("Scheduling Tasks Examples By Cron: The time is now " + dateFormat ().format (new Date ()));
//        
//    }
//

    
}