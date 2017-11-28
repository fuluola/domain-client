/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.lifu.domain;

import java.util.Map;

import com.lifu.springboot.DomainRepository;

/**
 * @author fuluola
 *
 */
public class DomainConsumer implements Runnable{

	private DomainHandler domainHandler;
	
	private DomainRepository domainRepo;
	
	public DomainConsumer(DomainHandler handler,DomainRepository domainRepo){
		this.domainHandler = handler;
		this.domainRepo = domainRepo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		while(true){
			try {
				Map<String,Object> domain = domainHandler.takeDomain();
				domainRepo.processSingleDomain((String)domain.get("domain"), (String)domain.get("batch"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
