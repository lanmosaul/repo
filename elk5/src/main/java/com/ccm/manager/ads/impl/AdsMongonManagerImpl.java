package com.ccm.manager.ads.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ccm.dao.mongo.AdsMessageDao;
import com.ccm.dao.mongo.Pagination;
import com.ccm.dao.mongo.impl.AdsMessageDaoMongo;
import com.ccm.manager.ads.AdsManager;
import com.ccm.manager.base.impl.GenericManagerImpl;
import com.ccm.model.ads.AdsLogMessageCriteria;
import com.ccm.model.ads.AdsMessage;
import com.ccm.model.ads.AdsMessageCode;
import com.ccm.model.ads.AdsMessageResult;
import com.ccm.model.ads.AdsToTBLog;
import com.ccm.model.common.InvokeResponse;
import com.ccm.util.CommonUtil;
import com.ccm.util.DateUtil;


@Repository("adsMongonManager")
public class AdsMongonManagerImpl extends GenericManagerImpl<
AdsMessage, String> implements 
AdsManager {

	@Resource
	protected AdsMessageDaoMongo adsMessageDaoMongo;

	protected Log onlineLog = LogFactory.getLog("online");
	@Resource
	protected AdsMessageDao adsMessageDao;

	@Override
	public AdsMessage createAdsMessage(AdsMessage adsMsg) {
		AdsMessage saveAdsMsg = adsMessageDaoMongo.save(adsMsg);
		return saveAdsMsg;
	}

	@Override
	public void saveOrUpdate(AdsMessage adsMessage) {
		adsMessageDaoMongo.saveOrUpdateById(adsMessage);
	}

	/** 定时删除数据 */
	@Override
	public void delAdsMessageAndAdsToTBLog() {
		adsMessageDaoMongo.delAdsMessageAndAdsToTBLog(DateUtil.addMonths(new Date(), -1));
	}

	@Override
	public AdsMessageResult searchAdsLog(AdsLogMessageCriteria amc) {
		Pagination<AdsMessage> page = adsMessageDaoMongo.getPage(amc.getPageNum(), amc.getPageSize(), buildQuery(amc));
		List<AdsMessage> adsList = page.getDatas();
		AdsMessageResult adsRes = new AdsMessageResult();
		adsRes.setResultList(adsList);
		adsRes.setTotalCount(page.getTotalCount());
		return adsRes;
	}

	private Query buildQuery(AdsLogMessageCriteria amc) {
		Query query = new Query();
		if (CommonUtil.isNotEmpty(amc.getHotelCode())) {
			query.addCriteria(Criteria.where("hotelCode").is(amc.getHotelCode()));
		}
		if (CommonUtil.isNotEmpty(amc.getTbExecErrMsg())) {
			query.addCriteria(Criteria.where("tbExecErrMsg").is(amc.getTbExecErrMsg()));
		}
		if (CommonUtil.isNotEmpty(amc.getStatus())) {
			query.addCriteria(Criteria.where("status").is(amc.getStatus()));
		}
		if (CommonUtil.isNotEmpty(amc.getTargetGDS())) {
			query.addCriteria(Criteria.where("targetGDS").is(amc.getTargetGDS()));
		}
		if (CommonUtil.isNotEmpty(amc.getRoomTypeCode())) {
			query.addCriteria(Criteria.where("roomTypeCode").is(amc.getRoomTypeCode()));
		}
		if (CommonUtil.isNotEmpty(amc.getRatePlanCode())) {
			query.addCriteria(Criteria.where("ratePlanCode").is(amc.getRatePlanCode()));
		}
		if (CommonUtil.isNotEmpty(amc.getAdsType())) {
			if (AdsMessage.ADSTYPE_STAYHISTORY.equalsIgnoreCase(amc.getAdsType())) {
				List<String> typeList = new ArrayList<String>();
				typeList.add(AdsMessage.ADSTYPE_STAYHISTORY);
				typeList.add(AdsMessage.ADSTYPE_KUNLUNCRSPUSH);
				query.addCriteria(Criteria.where("adsType").in(typeList));
			} else {
				query.addCriteria(Criteria.where("adsType").is(amc.getAdsType()));
			}
		}
		if (CommonUtil.isNotEmpty(amc.getTbStatus())) {
			query.addCriteria(Criteria.where("tbStatus").is(amc.getTbStatus()));
		}
		if (CommonUtil.isNotEmpty(amc.getEchoToken())) {
			query.addCriteria(Criteria.where("echoToken").regex("^" + amc.getEchoToken()));
		}
		if (CommonUtil.isNotEmpty(amc.getChainCode())) {
			query.addCriteria(Criteria.where("chainCode").is(amc.getChainCode()));
		}

		if (CommonUtil.isNotEmpty(amc.getStartDate()) && CommonUtil.isNotEmpty(amc.getEndDate())) {
			query.addCriteria(Criteria.where("createdTime").gte(amc.getStartDate()).andOperator(Criteria.where("createdTime").lte(amc.getEndDate())));
		}
		if (CommonUtil.isNotEmpty(amc.getStartDate()) && CommonUtil.isEmpty(amc.getEndDate())) {
			query.addCriteria(Criteria.where("createdTime").gte(amc.getStartDate()));
		}
		if (CommonUtil.isNotEmpty(amc.getEndDate()) && CommonUtil.isEmpty(amc.getStartDate())) {
			query.addCriteria(Criteria.where("createdTime").lte(amc.getEndDate()));
		}
		if (CommonUtil.isNotEmpty(amc.getSortBy())) {
			query.with(new Sort(amc.ASC.equalsIgnoreCase(amc.getDesc()) ? Sort.Direction.ASC : Sort.Direction.DESC, amc.getSortBy()));
		} else {
			query.with(new Sort(Sort.Direction.DESC, "createdTime"));
		}
		return query;
	}

	@Override
	public List<AdsToTBLog> getTbLog(AdsToTBLog tbLog) {
		return adsMessageDaoMongo.getTbLog(tbLog);
	}

	@Override
	public String getAdsMessageFieldValue(String field, String adsId) {
		String fieldValue = "";
		AdsMessage ads = adsMessageDaoMongo.findOne(Query.query(Criteria.where("adsId").is(adsId)));
		if (ads != null && "content".equals(field)) {
			fieldValue = ads.getContent();
		}
		return fieldValue;
	}

	@Override
	public String getAdsToTBLogFieldValue(String field, String adsToTBLogId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdsToTBLog getAdsToTBLog(String adsToTBLogId) {
		Object obj = adsMessageDaoMongo.findById(adsToTBLogId, AdsToTBLog.class);
		return obj != null ? (AdsToTBLog) obj : new AdsToTBLog();
	}

	@Override
	public AdsMessage getMaxReqDateAdsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, LinkedHashSet<String>> getParamCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getHotelByChainCode(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedHashSet<String> getRoomByHotelId(String hotelId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdsMessage> exportAdsLog(AdsLogMessageCriteria amc) {
		List<AdsMessage> adsList = new ArrayList<AdsMessage>();
		Pagination<AdsMessage> page = adsMessageDaoMongo.getPage(1, amc.getPageSize(), buildQuery(amc));
		Integer rowcount = page.getTotalCount();
		Integer pagesize = amc.getPageSize();

		adsList.addAll(page.getDatas());
		int pagecount = ((rowcount % pagesize == 0) ? (rowcount / pagesize) : (rowcount / pagesize + 1));
		for (int i = 2; i <= pagecount; i++) {
			amc.setPageNum(i);
			Pagination<AdsMessage> page2 = adsMessageDaoMongo.getPage(i, amc.getPageSize(), buildQuery(amc));
			adsList.addAll(page2.getDatas());
		}
		return adsList;
	}

	@Override
	public AdsToTBLog saveAdsToTBLog(String echoToken, String adsType, Integer status, String content, Object res) {
		AdsToTBLog log = new AdsToTBLog();
		log.setEchoToken(echoToken);
		log.setAdsType(adsType);
		log.setContent(content);
		log.setStatus(status);
		if (res instanceof InvokeResponse) {
			InvokeResponse invRes = (InvokeResponse) res;
			log.setErrMsg(invRes.getErrMsg());
			log.setRequestDate(invRes.getRequestDate());
			log.setResponseDate(invRes.getResponseDate());
		} else {
			log.setErrMsg(String.valueOf(res));
		}
		log.setCreatedTime(new Date());
		adsMessageDaoMongo.save(log);
		return log;
	}

	@Override
	public void processAdsData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendAdsToTB(List<AdsMessage> adsList) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void batchProcess(List<AdsMessage> adsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AdsMessageCode> refreshAdsMessageCode() {
		return adsMessageDaoMongo.refreshAdsMessageCode();
	}

	@Override
	public LinkedHashSet<String> getRateByHotelId(String hotelId) {
		// TODO Auto-generated method stub
		return null;
	}

}