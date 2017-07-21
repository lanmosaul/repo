package com.ccm.model.ads;


import java.util.Date;
import java.util.HashMap;

import org.springframework.data.annotation.Id;
import com.ccm.common.Column;
import com.ccm.model.base.BaseObject;

/**
 * 接收ads推送消息类
 */
public class AdsMessage extends BaseObject {

    private static final long serialVersionUID = 2241610909495825852L;
    @Id
    private String adsId;
    
    @Column(title="接收状态")
    private String acceptStatus;
    @Column(title="渠道代码")
    private String targetGDS;
    @Column(title="集团代码")
    private String chainCode;
    @Column(title="酒店代码")
    private String hotelCode;
    @Column(title="房型代码")
    private String roomTypeCode;
    @Column(title="协议类型")
    private String adsType;
    @Column(title="EchoToken")
    private String echoToken;
    @Column(title="消息记录时间")
    private Date createdTime;
    private String createdDate;
    @Column(title = "异常原因")
    private String errMsg;
	@Column(title = "信息详细")
	private String detail;
    
    private String content;
    private String status;
    private String dates;		//请求日期，yyyy-MM-dd，yyyy-MM-dd
    private String price;		//请求价格，OnePerson:888;TwoPerson:999
    private String roomAvailable;//房量
    private String onOff;		//开关	Open/Close
    private String ratePlanCode;
    private String tbStatus; //SEND_ERROR_TBSTATUS
    private String tbExecErrMsg;  //调用淘宝api执行后的错误信息
    
    private Boolean isRate;//是否推送房价
    private String  currencyCode;
    
    public static final String EXEC_INIT_STATUS = "0";// 初始为 0 为执行
    public static final String EXEC_END_STATUS = "1";// 1 为 已执行
    public static final String EXEC_ERROR_STATUS = "9";// 9 执行错误
    
    public static final String SEND_INIT_TBSTATUS = "0";// 未发送
    public static final String SEND_SUCCESS_TBSTATUS = "1";// 1 发送成功
    public static final String SEND_IGNORE_TBSTATUS = "2";// 2 不予发送，未匹配或对象已关闭
    public static final String SEND_ERROR_TBSTATUS = "9";// 发送失败
    
    public static final String ADSTYPE_AVAILABILITY = "FIDELIO_AvailabilityStatusRQ";	//房量
    public static final String ADSTYPE_AVAILUPDATE = "FIDELIO_AvailUpdateNotifRQ";		//开关房
    public static final String ADSTYPE_RATEUPDATE = "FIDELIO_RateUpdateNotifRQ";		//房价
	public static final String ADSTYPE_RatePlanNotif = "OTA_HotelRatePlanNotifRQ"; // 复杂房价
    
    public static final String ADSTYPE_STAYHISTORY = "StayHistoryRequest";  //stayHistory
    public static final String ADSTYPE_KUNLUNCRSPUSH = "KunlunCRSPush";     //kunlunCRS
    
    private static HashMap<String,HashMap<String,String>> chainMap = new HashMap<String, HashMap<String,String>>();
    
    
    
    public String getAcceptStatus() {
        if(status!=null){
            if(status.equals("0") ){
                acceptStatus="未执行";
            }else if(status.equals("1") ){
                acceptStatus="接收成功";
            }
            else if(status.equals("9") ){
                acceptStatus="接收异常";
            }
        }
        return acceptStatus;
    }

    public static HashMap<String,String> getHotelByChainCode(String chainCode){
        return chainMap.get(chainCode);
    }
    
    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
    }


    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
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

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRoomAvailable() {
		return roomAvailable;
	}

	public void setRoomAvailable(String roomAvailable) {
		this.roomAvailable = roomAvailable;
	}

	public String getOnOff() {
		return onOff;
	}

	public void setOnOff(String onOff) {
		this.onOff = onOff;
	}

	public String getTbExecErrMsg() {
		return tbExecErrMsg;
	}

	public void setTbExecErrMsg(String tbExecErrMsg) {
		this.tbExecErrMsg = tbExecErrMsg;
	}

	public Boolean getIsRate() {
		return isRate;
	}

	public void setIsRate(Boolean isRate) {
		this.isRate = isRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	
}
