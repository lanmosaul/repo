package com.ccm.model.ads;

import org.springframework.data.annotation.Id;


public class AdsMessageCode{
	private static final long serialVersionUID = -4882057364247451953L;
	@Id
	private String AdsMessageCodeId;
	private String chainCode;
	private String channelCode;
	private String hotelCode;
	private String roomTypeCode;
	private Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getChainCode() {
		return chainCode;
	}

	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getHotelCode() {
		return hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

	public String getAdsMessageCodeId() {
		return AdsMessageCodeId;
	}

	public void setAdsMessageCodeId(String adsMessageCodeId) {
		AdsMessageCodeId = adsMessageCodeId;
	}

	public String getRoomTypeCode() {
		return roomTypeCode;
	}

	public void setRoomTypeCode(String roomTypeCode) {
		this.roomTypeCode = roomTypeCode;
	}

	@Override
	public String toString() {
		return "AdsMessageCode [AdsMessageCodeId=" + AdsMessageCodeId
				+ ", chainCode=" + chainCode + ", channelCode=" + channelCode
				+ ", hotelCode=" + hotelCode + ", roomTypeCode=" + roomTypeCode
				+ ", count=" + count + "]";
	}
}
