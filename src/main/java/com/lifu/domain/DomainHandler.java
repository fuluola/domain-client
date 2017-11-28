/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.lifu.domain;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.stereotype.Component;

/**
 * @author fuluola
 *
 */
@Component
public class DomainHandler {

	private static BlockingQueue<Map<String,Object>> domainsQueue = new ArrayBlockingQueue<Map<String,Object>>(1000,false);

	private static StringBuffer queueInfo = new StringBuffer();
	/**
	 * 批量往队列里插入域名信息
	 * @date 2017年11月17日下午5:51:45
	 * @author fuzhuan
	 *
	 */
	public void addDomain2Queue(List<Map<String,Object>> list){
		
		if(list.size()>=1){
			domainsQueue.addAll(list);
		}
	}
	
	/**
	 * 取一个需要爬取的域名
	 * @date 2017年11月17日下午5:52:27
	 * @author fuzhuan
	 * @return
	 * @throws InterruptedException
	 *
	 */
	public Map<String,Object> takeDomain() throws InterruptedException{
		
		return domainsQueue.take();
	}
	
	public String queueInfotoString(){
		queueInfo.delete(0, queueInfo.length());
		for(Map<String,Object> map: domainsQueue){
			queueInfo.append(map.get("domain")).append(",");
		}
		return queueInfo.toString();
	}
}
