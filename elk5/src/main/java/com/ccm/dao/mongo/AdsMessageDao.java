package com.ccm.dao.mongo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccm.model.ads.AdsLogMessageCriteria;
import com.ccm.model.ads.AdsMessageResult;
import com.ccm.model.ads.AdsToTBLog;
import com.ccm.dao.GenericDao;
import com.ccm.model.ads.AdsMessage;


public interface AdsMessageDao extends GenericDao<AdsMessage, String> {

    AdsMessageResult searchAdsLog(AdsLogMessageCriteria amc);

    List<AdsToTBLog> getTbLog(AdsToTBLog tbLog);

    String getAdsMessageFieldValue(HashMap<String, String> map);

    String getAdsToTBLogFieldValue(HashMap<String, String> map);

    AdsMessage getMaxReqDateAdsMessage();

    List<AdsMessage> getParamCode();
    
    /**跟据echoToken，adsType 获取最近一条消息 */
    AdsMessage getAdsMessageByEchoTokenAndAdsType(String echoToken, String adsType);

    List<Map<String, Object>> getInventoryByCode(String hotelCode,
            String roomTypeCode, String ratePlanCode, Date startDate, Date endDate);

    Integer searchAdsLogCount(AdsLogMessageCriteria amc);

    List<AdsMessage> searchAdsLogList(AdsLogMessageCriteria amc);
    /***批量处理ads消息，房量、房价、开关
     * @throws Exception */
	Map<String, String> batchProcess(List<AdsMessage> adsList) throws Exception;

	
	/***
	 * 删除time 之前的数据
	 * @param time
	 */
	void delAdsMessageAndAdsToTBLog(Date time);

}
