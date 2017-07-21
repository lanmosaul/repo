package com.ccm.model.common;

import java.util.Date;

public class InvokeResponse {
	private boolean isSuccess;
	private String errMsg;
	private String errCode;
	/***
	 * 返回对象
	 */
	private Object resObj;
	/***
	 * 请求数据
	 */
	private String reqData;
	/***
	 * 返回数据
	 */
	private String resDate;

	/** 该酒店已经存在,不能重复发布该酒店 */
	public final static String TB_ERRCODE_HOTEL_EXIST = "isv.invalid-parameter:HOTEL_EXIST";

	private Date requestDate;
	private Date responseDate;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Object getResObj() {
		return resObj;
	}

	public void setResObj(Object resObj) {
		this.resObj = resObj;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getReqData() {
		return reqData;
	}

	public void setReqData(String reqData) {
		this.reqData = reqData;
	}

	public String getResDate() {
		return resDate;
	}

	public void setResDate(String resDate) {
		this.resDate = resDate;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

}
