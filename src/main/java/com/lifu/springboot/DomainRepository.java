package com.lifu.springboot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lifu.domain.DomainInfoService;
import com.lifu.model.Constants;
import com.lifu.model.DomainObject;
import com.lifu.model.PreDomainInfo;
import com.lifu.model.QueryDomainRespMessage;

/** 
 * @description 
 * @author  fuzhuan fu.luola@qq.com
 * @date 2017年7月12日 
 */
@Repository
public class DomainRepository {
	
	private JdbcTemplate jdbc;
	
	public DomainRepository(JdbcTemplate jdbc){
		this.jdbc = jdbc;
	}
	private static Logger logger = LoggerFactory.getLogger(DomainRepository.class);
	
	private static String insertSQL = "insert into domain (domain,status,errorCount,batch,createTime) values(?,0,0,?,now())"; 
	
	private static String findSQL = "select domain,status,createTime,errorCount,batch from domain where domain=?";
	
	private static String processSQL = "select domain,status,errorCount,createTime,batch from domain where status=0 and errorCount<3 order by errorCount asc limit 50";
	
	private static String updateSUCCESS_SQL = "update domain set status=1,errorMsg=null,updateTime=now() where domain=?";
	
	private static String updateERROR_SQL = "update domain set errorMsg=?,errorCount=errorCount+1,updateTime=now() where domain=?";
	
	private static String insertDomainInfoSQL= "INSERT INTO domaininfo_collect (domainName,registrantOrganization,registrar,registrantName,"
			+ "registrantPhone,registrantEmail,nsServer,dnsServer,creationDate,expirationDate,ip,ipAddress,"
			+ "title,keywords,description,googlePR,createTime,batch) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
	

	@Autowired
	private DomainInfoService infoService;
	
	public PreDomainInfo findByDomain(String data){
		
		return jdbc.query(findSQL,new Object[]{data}, new DomainResultSetExtractor());
		
	}
	
	public int insertDomain(String data,String batch) {
		
		if(findByDomain(data)==null){
			return jdbc.update(insertSQL,data,batch);
		}else{
			return 0;
		}
	}
	
	public List<Map<String,Object>> queryProcessDomain(){

		return jdbc.queryForList(processSQL);
		  
	}
	
	@Transactional
	public void processSingleDomain(String data,String batch){
		QueryDomainRespMessage respMsg=infoService.domainInfoQuery(data);
		if(respMsg==null) {
			jdbc.update(updateERROR_SQL, new Object[]{"该域名没有找到注册信息",data});
		}else if(Constants.SUCCESS.equals(respMsg.getCode())){
			DomainObject domainInfo = respMsg.getDomainObject();
			jdbc.update(updateSUCCESS_SQL, new Object[]{data});
			jdbc.update(insertDomainInfoSQL, domainInfo.getDomainName(),domainInfo.getRegistrantOrganization(),domainInfo.getRegistrar(),
								domainInfo.getRegistrantName(),
								domainInfo.getRegistrantPhone(),domainInfo.getRegistrantEmail(),domainInfo.getNsServer(),
								domainInfo.getDnsServer(),domainInfo.getCreationDate(),domainInfo.getExpirationDate(),
								domainInfo.getIp(),domainInfo.getIpAddress(),domainInfo.getTitle(),domainInfo.getKeywords(),
								domainInfo.getDescription(),domainInfo.getGooglePR(),batch);
		}else{
			jdbc.update(updateERROR_SQL, new Object[]{respMsg.getExceptionMsg(),data});
		}
	}
	
	public String processBatchDomain(){
		List<Map<String,Object>> list= queryProcessDomain();
		logger.info("total process "+list.size()+" records!");
		if(list.size()>=1){
			for(Map<String,Object> map:list){
				String domain = (String) map.get("domain");
				String batch = (String) map.get("batch");
				logger.info("start collecting,domain:"+domain);
				processSingleDomain(domain,batch);
			}
		}
		return "";
	}
	
}

class DomainResultSetExtractor implements ResultSetExtractor<PreDomainInfo> {


	public PreDomainInfo extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		
		PreDomainInfo entity = null;
		while(rs.next()){
			entity = new PreDomainInfo();
			entity.setCreateTime(rs.getDate("createTime"));
			entity.setDomain(rs.getString("domain"));
			entity.setStatus(rs.getInt("status"));
			entity.setErrorCount(rs.getInt("errorCount"));
		}
		return entity;
	}
	
}
class PreDomainRowMapper implements RowMapper<PreDomainInfo> {


	public PreDomainInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		PreDomainInfo entity = new PreDomainInfo();
		entity.setCreateTime(rs.getDate("createTime"));
		entity.setDomain(rs.getString("domain"));
		entity.setStatus(rs.getInt("status"));
		entity.setErrorCount(rs.getInt("errorCount"));
		return entity;
	}
	
}