package com.ccm.model.ads;


import java.util.Date;

import com.ccm.model.base.criteria.SearchCriteria;

public class AdsLogMessageCriteria extends SearchCriteria {

    private static final long serialVersionUID = 2241610909495825852L;

    private Date startDate;
    private Date endDate;
    private String status;
    private String adsType;
    private String echoToken;
    private String chainCode;
    private String hotelCode;
    private String targetGDS;
    private String roomTypeCode;
    private String ratePlanCode;
    private String tbStatus;
    public static final String EXEC_INIT_STATUS = "0";// 初始为 0 为执行
    public static final String EXEC_END_STATUS = "1";// 1 为 已执行
    public static final String EXEC_ERROR_STATUS = "9";// 9 执行错误

    private String tbExecErrMsg;  //调用淘宝api执行后的错误信息
    
    private Boolean outOfSize=false;//导出记录是否超限
    
	public Boolean getOutOfSize() {
		return outOfSize;
	}

	public void setOutOfSize(Boolean outOfSize) {
		this.outOfSize = outOfSize;
	}

	public String getTbExecErrMsg() {
		return tbExecErrMsg;
	}

	public void setTbExecErrMsg(String tbExecErrMsg) {
		this.tbExecErrMsg = tbExecErrMsg;
	}

	public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getTargetGDS() {
        return targetGDS;
    }

    public void setTargetGDS(String targetGDS) {
        this.targetGDS = targetGDS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdsType() {
        return adsType;
    }

    public void setAdsType(String adsType) {
        this.adsType = adsType;
    }

    public String getEchoToken() {
        return echoToken;
    }

    public void setEchoToken(String echoToken) {
        this.echoToken = echoToken;
    }

    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    public String getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(String tbStatus) {
        this.tbStatus = tbStatus;
    }

    public String getRatePlanCode() {
        return ratePlanCode;
    }

    public void setRatePlanCode(String ratePlanCode) {
        this.ratePlanCode = ratePlanCode;
    }

}
