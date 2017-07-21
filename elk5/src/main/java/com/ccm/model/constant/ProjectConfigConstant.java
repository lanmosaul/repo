package com.ccm.model.constant;

import com.ccm.util.PropertiesUtil;

/**
 * 
 * 项目配置常量key
 * 
 */
public class ProjectConfigConstant {

	/**** 亿美短信 ****/

	// 软件序列号
	public static String sms_softwareSerialNo = "sms.softwareSerialNo";

	// 自定义关键字(key值),一定要保存好
	public static String sms_key = "sms.key";

	// 下订单与退款通知相关运营人员
	public static String oper_mobile = "oper.mobile";

	/**** 图片 ****/

	public static String pictureUploadPath = "pictureUploadPath";
	public static String pictureUrlContext = "pictureUrlContext";
	public static String picCutConfig = "picCutConfig";

	/****
	 * 图片原有的存储地址(如房型宝贝图片,酒店图片,系统需要从 这个目录下面读取相应的图片来匹配数据，并且将匹配好的图片copy到系统读取文件的路径)
	 ****/
	public static String pictureFormerPath = "pictureFormerPath";

	// 水印图片
	public static String watermarkFilePath = "watermarkFilePath";
	/***
	 * 畅联switch
	 */
	// 预订ows url
	public static final String ows_reservation_wsdl = PropertiesUtil.getProperty("ows.reservation.wsdl");

	public static final String generalAvailability_wsdl = PropertiesUtil.getProperty("generalAvailability.wsdl");
	// 渠道编码
	public static final String ows_channnelCode = PropertiesUtil.getProperty("ows.channnelCode");
	// 渠道确认ID
	public static final String ows_transactionID = PropertiesUtil.getProperty("ows.transactionID");
	// 预订ows超时时间
	public static String ows_timeout = PropertiesUtil.getProperty("ows.timeout");
	// 从畅连取产品代码
	public static final String onlineProductCodeUrl = PropertiesUtil.getProperty("onlineProductCodeUrl");

	public static final String mailDefaultTo = PropertiesUtil.getProperty("mailDefaultTo");

	public static final String wbeUrl = PropertiesUtil.getProperty("wbeUrl");
	public static final String mcUrl = PropertiesUtil.getProperty("mcUrl");

	public static final String projectDomain = PropertiesUtil.getProperty("projectDomain");

	public static final String incrementService = PropertiesUtil.getProperty("incrementService");
	
	//ws应用标识 
	public static final String ccmWsAppName = PropertiesUtil.getProperty("ccmWs.appName");
	
	// shiji care
	public static final String shijicareUrl = PropertiesUtil.getProperty("shijicare.url");
	public static final String shijicareUsername = PropertiesUtil.getProperty("shijicare.username");
	public static final String shijicarePassword = PropertiesUtil.getProperty("shijicare.password");
	public static final String jobService = PropertiesUtil.getProperty("jobService");
	
	/**
	 * 0:非正式环境
	 * 1：正式环境
	 */
	public static final String isProduction = PropertiesUtil.getProperty("isProduction");
	public static final String wechatMsgUrl = PropertiesUtil.getProperty("wechatMsgUrl");
	public static final String wechatMsgAgentid = PropertiesUtil.getProperty("wechatMsgAgentid");
}
