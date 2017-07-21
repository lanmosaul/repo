package com.ccm.manager.ads;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import com.ccm.manager.base.GenericManager;
import com.ccm.model.ads.AdsLogMessageCriteria;
import com.ccm.model.ads.AdsMessage;
import com.ccm.model.ads.AdsMessageCode;
import com.ccm.model.ads.AdsMessageResult;
import com.ccm.model.ads.AdsToTBLog;


/**
 * 
 * Ads 相关业务方法
 *
 */
public interface AdsManager extends GenericManager<AdsMessage, String> {
    
	/**	
     * 保存ads
     */
	public AdsMessage createAdsMessage(AdsMessage adsMsg);
	

    public AdsMessageResult searchAdsLog(AdsLogMessageCriteria amc);


    public List<AdsToTBLog> getTbLog(AdsToTBLog tbLog);


    public String getAdsMessageFieldValue(String field, String adsId);


    public String getAdsToTBLogFieldValue(String field, String adsToTBLogId);


    public AdsToTBLog getAdsToTBLog(String adsToTBLogId);


    public AdsMessage getMaxReqDateAdsMessage();


    public HashMap<String, LinkedHashSet<String>> getParamCode();


    public HashMap<String, String> getHotelByChainCode(String string);


    public LinkedHashSet<String> getRoomByHotelId(String hotelId);

    LinkedHashSet<String> getRateByHotelId(String hotelId);

    public List<AdsMessage> exportAdsLog(AdsLogMessageCriteria amc);


	AdsToTBLog saveAdsToTBLog(String echoToken, String adsType, Integer status,
			String content, Object res);


	public void saveOrUpdate(AdsMessage adsMessage);

	/**定时任务处理*/
	void processAdsData();
	/**发送到tb*/
	void sendAdsToTB(List<AdsMessage> adsList) throws Exception;
	/**处理房量房价房态数据到 mysql
	 * @throws Exception */
	public void batchProcess(List<AdsMessage> adsList) throws Exception;


	List<AdsMessageCode> refreshAdsMessageCode();


	void delAdsMessageAndAdsToTBLog();
}