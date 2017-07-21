package com.ccm.dao.mongo.impl;

/**
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ccm.dao.mongo.AdsMessageDao;
import com.ccm.dao.mongo.MongodbBaseDao;
import com.ccm.model.ads.AdsMessage;
import com.ccm.model.ads.AdsMessageCode;
import com.ccm.model.ads.AdsLogMessageCriteria;
import com.ccm.model.ads.AdsMessageResult;
import com.ccm.model.ads.AdsToTBLog;
import com.ccm.util.CommonUtil;
import com.ccm.util.DateUtil;

@Repository("adsMessageDaoMongo")
public class AdsMessageDaoMongo extends MongodbBaseDao<AdsMessage> implements AdsMessageDao{
	protected final Log log = LogFactory.getLog(getClass());
	protected Class<AdsMessage> getEntityClass(){
		return AdsMessage.class;
	}
	
	@Override
	public AdsMessageResult searchAdsLog(AdsLogMessageCriteria amc) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdsToTBLog> getTbLog(AdsToTBLog tbLog) {
		Query query = new Query();
		if(CommonUtil.isNotEmpty(tbLog.getEchoToken())){
			query.addCriteria(Criteria.where("echoToken").is(tbLog.getEchoToken()));
		}
		query.with(new Sort(Sort.Direction.DESC, "createdTime"));
		return (List<AdsToTBLog>) this.find(query, AdsToTBLog.class);
	}
	public List<AdsMessageCode> getAdsMessageCode() {
		Query query = new Query();
		//做分机处理
//		query.addCriteria(Criteria.where("chainCode").ne("YZH"));
		return getMongoTemplate().find(query,AdsMessageCode.class);
	}
	
	public List<AdsMessageCode> refreshAdsMessageCode() {
		List<AdsMessageCode> adsMessageCodeList = new ArrayList<AdsMessageCode>();
		MongoTemplate mt = this.getMongoTemplate();
		GroupBy gb = new GroupBy(new String[]{"chainCode","hotelCode","roomTypeCode"});//,"hotelCode"
		gb.initialDocument("{count:0}");
		gb.reduceFunction("function(doc, prev){prev.count+=1}");
		GroupByResults<AdsMessageCode> gbResult = mt.group(Criteria.where("tbStatus").is("0"), "adsMessage", gb, AdsMessageCode.class);

		Iterator<AdsMessageCode> it = gbResult.iterator();
		while (it.hasNext()) {
			AdsMessageCode amc = it.next();
			adsMessageCodeList.add(amc);
		}
		Collections.sort(adsMessageCodeList,new Comparator<AdsMessageCode>() {
			public int compare(AdsMessageCode o1, AdsMessageCode o2) {
				return o2.getCount().compareTo(o1.getCount());
			}
		});
		dropEntity(AdsMessageCode.class);
		saveALL(adsMessageCodeList);
		return adsMessageCodeList;
	}
	@Override
	public void delAdsMessageAndAdsToTBLog(Date time){
		log.info("del time:"+DateUtil.convertDateToString(time));
		Query query = new Query();
		query.addCriteria(Criteria.where("createdTime").lt(time)
				.andOperator(Criteria.where("adsType").nin(new String[]{"StayHistoryRequest","KunlunCRSPush"})));
		log.info("del AdsMessage...");
		this.remove(query);//删除adsMessage
		log.info("del AdsToTBLog...");
		this.remove(query,AdsToTBLog.class);//删除AdsToTBLog
		log.info("del end");
	}
	
	@Override
	public String getAdsMessageFieldValue(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdsToTBLogFieldValue(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdsMessage getMaxReqDateAdsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdsMessage> getParamCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdsMessage getAdsMessageByEchoTokenAndAdsType(String echoToken,
			String adsType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getInventoryByCode(String hotelCode,
			String roomTypeCode, String ratePlanCode, Date startDate,
			Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer searchAdsLogCount(AdsLogMessageCriteria amc) {
		return null;
	}

	@Override
	public List<AdsMessage> searchAdsLogList(AdsLogMessageCriteria amc) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> batchProcess(
			List<AdsMessage> adsList) {
		return null;
	}
}
