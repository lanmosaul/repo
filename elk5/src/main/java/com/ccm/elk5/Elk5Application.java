package com.ccm.elk5;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.ccm.dao.elk.ElkQueryDao;
import com.ccm.dao.mongo.AdsMessageDao;
import com.ccm.manager.ads.AdsManager;
import com.ccm.manager.ads.AdsMongoManager;
import com.ccm.manager.ads.impl.AdsMongonManagerImpl;
import com.ccm.model.ads.AdsLogMessageCriteria;
import com.ccm.model.ads.AdsMessage;
import com.ccm.model.ads.AdsMessageResult;
import com.ccm.util.DateUtil;
import com.ccm.util.ElkParam;
@SpringBootApplication
//
@ComponentScan(basePackages = {"com.ccm.manager","com.ccm.dao.mongo","com.ccm.dao.elk","com.ccm.util"})
public class Elk5Application implements CommandLineRunner{
	
	public static void main(String[] args) throws UnknownHostException {
		SpringApplication.run(Elk5Application.class, args).close();
	}
	
	@Resource 
	private ElkQueryDao elkQueryDao;
	@Resource
	private AdsManager adsMongonManager;
	
	private void removeAdsMsgs(String ... idList){
		for(String id:idList)
		elkQueryDao.removeDataById("adsmessage","2017-04-14",id);
	}
	
	
	private void getAdsLog()throws ParseException{
		Map<String,Object> conditionMap=new HashMap<String,Object>();
		conditionMap.put("status","1");
		QueryBuilder qb=elkQueryDao.initSimpleQueryBuilder("status","1");
		SearchResponse sr=elkQueryDao.initSearchRequestBuilder("adsmessage", "2017-04-14",qb,null,SortOrder.DESC,0,100);
		elkQueryDao.fetchDocument(sr);
//		elkQueryDao.getDataById("dept", "employee", "AV0wmEh5X8uqqMmcr1nw");
	}
	
	private void insertAdsLog() throws ParseException{
		int recyclePages=147267;
		int pageSize = 100;// this.getCurrentPageSize("adsMsg");
		for(int pageNo=1;pageNo<=recyclePages;pageNo++){
		AdsLogMessageCriteria amc = new AdsLogMessageCriteria();
//		amc.setStartDate(DateUtil.convertStringToDate("2017-07-01"));
//		amc.setAdsType(AdsMessage.ADSTYPE_RatePlanNotif);
//		amc.setChainCode("SWD");
//		amc.setHotelCode("SHDLH");
//		amc.setRoomTypeCode();
//		amc.setRatePlanCode();
//		amc.setTbExecErrMsg();
		amc.setPageNum(pageNo);
		amc.setPageSize(pageSize);
		AdsMessageResult result=adsMongonManager.searchAdsLog(amc);
		System.out.println("total data size=>"+result.getTotalCount()+",current pageNo"+pageNo);
			elkQueryDao.postAdsMessage("adsMessage",result.getResultList());
		}
	}
	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		//"dept", "employee", "AV0wmEh5X8uqqMmcr1nw"
		System.out.println("begin");
//		getAdsLog();
//		removeAdsMsgs("AV1zofg0FMcRxcKExZPX","AV1zogg9FMcRxcKExZPY");
		insertAdsLog();
		System.out.println("end");

	}
	private void test(){
		/*
		while(true){
			Map<String,Object> conditionMap=new HashMap<String,Object>();
			conditionMap.put("empname","emp2");
			QueryBuilder qb=elkQueryDao.composeEqualCondtionWithMulitQuery(null, conditionMap, ElkParam.ELC_AND);
			SearchResponse sr=elkQueryDao.initSearchRequestBuilder("dept", "employee",qb,"age",SortOrder.DESC,0,100);
			elkQueryDao.fetchDocument(sr);
//			elkQueryDao.getDataById("dept", "employee", "AV0wmEh5X8uqqMmcr1nw");
			
		}
		*/
		/*
		Settings settings = Settings.builder()
		        .put("cluster.name",cluster_name).build();
		TransportClient client = 
				new PreBuiltTransportClient
				(settings)
		        .addTransportAddress(
		        		new InetSocketTransportAddress(InetAddress.getByName(nodes_config), 9300));
		
		QueryBuilder qb=initFuzzyQueryBuilder("empname","e");
		SearchResponse sr=initSearchRequestBuilder(client,"dept","employee",qb,null,null,0,100);
		fetchDocument(sr);
		client.close();
		*/
	}
}
