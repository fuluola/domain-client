package com.lifu.springboot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lifu.domain.DomainConsumer;
import com.lifu.domain.DomainHandler;
import com.lifu.domain.DomainProducer;

/**
 * @author fuluola
 *
 */
@WebListener
public class DomainInfoFetchListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(DomainInfoFetchListener.class);
	//获取spring注入的bean对象  
	private WebApplicationContext springContext;  
	
	private DomainHandler handler ;
	
	private DomainRepository domainRepo;
	
	private static Integer MAX_THREAD = 8;
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		logger.info("-----initialize start-----------");
		springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());  
		handler = (DomainHandler) springContext.getBean("domainHandler");
		domainRepo = (DomainRepository) springContext.getBean("domainRepository");
		logger.info("----initialize finished----------");
		DomainProducer producer = new DomainProducer(handler,domainRepo);
		
		new Thread(producer,"domain-producer").start();
//		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
//		fixedThreadPool.execute(new DomainConsumer(handler,domainRepo));
		
		for(int i=0;i<MAX_THREAD;i++){
			new Thread(new DomainConsumer(handler,domainRepo),"domainConsumer"+i).start();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		
		System.out.println("ServletContext销毁");
	}



}
