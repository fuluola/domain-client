/**
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: bubugao yunhou</p>
 */
package com.lifu.utils;

import com.lifu.model.DomainObject;
import com.mysql.jdbc.StringUtils;

/**
 * @author fuluola
 *
 */

public class ParseResultDomainInfo {

public static DomainObject parseSimpleComDomainInfo(DomainObject obj,String line){
		
		if(line.startsWith("Creation Date:")){
			if(!StringUtils.isNullOrEmpty(line.split(":")[1]))
				obj.setCreationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Registry Expiry Date:")){
			if(!StringUtils.isNullOrEmpty(line.split(":")[1]))
				obj.setExpirationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Registrar:")){
			obj.setRegistrar(line.split(":")[1].trim());
		}else if(line.startsWith("Registrar Abuse Contact Phone:")){
			if(line.split(":").length>1)
				obj.setRegistrantPhone(line.split(":")[1]);
		}else if(line.startsWith("Registrar Abuse Contact Email:")){
			if(line.split(":").length>1)
				obj.setRegistrantEmail(line.split(":")[1].trim());
		}else if(line.startsWith("Name Server") && StringUtils.isNullOrEmpty(obj.getNsServer())){
			if(line.split(":").length>1)
				obj.setNsServer(line.split(":")[1]);
		}else if(line.startsWith("Name Server") && !StringUtils.isNullOrEmpty(obj.getNsServer()) && StringUtils.isNullOrEmpty(obj.getDnsServer())){
			if(line.split(":").length>1)
				obj.setDnsServer(line.split(":")[1]);
		}
		return obj;
	}

	public static DomainObject parseComDomainInfo(DomainObject obj,String line){
		
		if(line.startsWith("Creation Date:")){
			if(!StringUtils.isNullOrEmpty(line.split(":")[1]))
				obj.setCreationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Registrar Registration Expiration Date:")){
			if(!StringUtils.isNullOrEmpty(line.split(":")[1]))
				obj.setExpirationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Registrar:")){
			obj.setRegistrar(line.split(":")[1].trim());
		}else if(line.startsWith("Registrant Name:")){
			obj.setRegistrantName(line.split(":")[1].trim());
		}else if(line.startsWith("Registrant Organization:")){
			String ro = line.split(":")[1];
			obj.setRegistrantOrganization((StringUtils.isEmptyOrWhitespaceOnly(ro)?"":ro).trim());
		}else if(line.startsWith("Registrant Phone:")){
			String rp = line.split(":")[1];
			obj.setRegistrantPhone((StringUtils.isEmptyOrWhitespaceOnly(rp)?"":rp).trim());
		}else if(line.startsWith("Registrant Email:")){
			obj.setRegistrantEmail(line.split(":")[1].trim());
		}else if(line.startsWith("Name Server") && StringUtils.isNullOrEmpty(obj.getNsServer())){
			obj.setNsServer(line.split(":")[1]);
		}else if(line.startsWith("Name Server") && !StringUtils.isNullOrEmpty(obj.getNsServer()) && StringUtils.isNullOrEmpty(obj.getDnsServer())){
			obj.setDnsServer(line.split(":")[1]);
		}
		return obj;
	}
public static DomainObject parseOrgDomainInfo(DomainObject obj,String line){
		
		if(line.startsWith("Creation Date:")){
			if(line.split(":").length>1)
				obj.setCreationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Registry Expiry Date:")){
			if(line.split(":").length>1)
				obj.setExpirationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Registrant Name:")){
			if(line.split(":").length>1)
				obj.setRegistrantName(line.split(":")[1].trim());
		}else if(line.startsWith("Registrant Organization:")){
			
			if(line.split(":").length>1){
				String ro = line.split(":")[1];
				obj.setRegistrantOrganization(ro.trim());
			}
		}else if(line.startsWith("Registrant Phone:")){
			String rp = line.split(":")[1];
			obj.setRegistrantPhone((StringUtils.isEmptyOrWhitespaceOnly(rp)?"":rp).trim());
		}else if(line.startsWith("Registrant Email:")){
			obj.setRegistrantEmail(line.split(":")[1].trim());
		}else if(line.startsWith("Name Server") && StringUtils.isNullOrEmpty(obj.getNsServer())){
			if(line.split(":").length>1)
				obj.setNsServer(line.split(":")[1]);
		}else if(line.startsWith("Name Server") && !StringUtils.isNullOrEmpty(obj.getNsServer()) && StringUtils.isNullOrEmpty(obj.getDnsServer())){
			if(line.split(":").length>1)
				obj.setDnsServer(line.split(":")[1]);
		}
		return obj;
	}
	public static DomainObject parseCnDomainInfo(DomainObject obj,String line){
		
		if(line.startsWith("Registration Time:")){
			if(line.split(":").length>1)
				obj.setCreationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Expiration Time:")){
			if(line.split(":").length>1)
				obj.setExpirationDate(line.split(":")[1].trim().substring(0, 10));
		}else if(line.startsWith("Registrant:")){
			if(line.split(":").length>1)
				obj.setRegistrantName(line.split(":")[1].trim());
		}else if(line.startsWith("Sponsoring Registrar:")){
			if(line.split(":").length>1)
				obj.setRegistrar(line.split(":")[1].trim());
		}else if(line.startsWith("Registrant:")){
			if(line.split(":").length>1){
				String ro = line.split(":")[1];
				obj.setRegistrantOrganization((StringUtils.isEmptyOrWhitespaceOnly(ro)?"":ro).trim());
			}
		}else if(line.startsWith("Registrant Contact Email:")){
			if(line.split(":").length>1)
				obj.setRegistrantEmail(line.split(":")[1].trim());
		}else if(line.startsWith("Name Server") && StringUtils.isNullOrEmpty(obj.getNsServer())){
			if(line.split(":").length>1)
				obj.setNsServer(line.split(":")[1]);
		}else if(line.startsWith("Name Server") && !StringUtils.isNullOrEmpty(obj.getNsServer()) && StringUtils.isNullOrEmpty(obj.getDnsServer())){
			if(line.split(":").length>1)
				obj.setDnsServer(line.split(":")[1]);
		}
		return obj;
	}
	/**
	 * @date 2017年8月16日下午4:11:48
	 * @author fuzhuan
	 * @param obj
	 * @param line
	 * 
	 */
	public static DomainObject parseTWDomainInfo(DomainObject obj, String line) {
		if(line.startsWith("Record created on")){
			obj.setCreationDate(line.replace("(YYYY-MM-DD)", "").replace("Record created on", "").trim());
		}else if(line.startsWith("Record expires on")){
			obj.setExpirationDate(line.replace("(YYYY-MM-DD)", "").replace("Record expires on", "").trim());
		}else if(line.startsWith("Registration Service Provider:")){
			obj.setRegistrar(line.split(":")[1].trim());
		}else if(line.startsWith("TEL:")){
			obj.setRegistrantPhone(line.split(":")[1].trim());
		}
		return obj;
		
	}
}
