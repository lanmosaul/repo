package com.ccm.model.constant;

public class OXIConstant {

	public static String Interface = "OXI-OPERA";// 接口标志

	public static String creatorCode = "SUPERVISOR";// 系统
	public static String changeCode = "PMS";// 系统
	public static String restype = "CANCELED";
	public static String user = "OEDS$OWS";// insertUser,updateUser：OEDS$OWS（固定值）

	// dictionary中dictName
	public static String guaranteeType = "guaranteeType";// 担保类型
	public static String paymentMethod = "paymentMethod";// 支付方式
	public static String title = "title";// 称谓
	public static String currency = "currency";// 币种
	public static String addressType = "addressType";// 地址类型
	public static String phoneType = "phoneType";// 电话类型
	public static String gender = "gender";// 性别类型
	public static String profileType = "profileType";// 个人信息类型
	public static String languageCode = "languageCode";// 语言代码
	public static String reservationStatusType = "reservationStatusType";// 预订状态类型
	public static String orderStatus = "orderStatus";// 订单状态
	public static String country = "country";// 国家代码
	public static String custom = "customId";// 客户代码
	public static String channelCode = "channelCode";// 渠道代码
	public static String commentType = "commentType";// 备注类型
	public static String marketCode = "marketCode";// 市场代码
	public static String sourceCode = "sourceCode";// 来源代码，需要在pms里配置
	public static String ratePlanCode = "ratePlanCode";// 房价代码
	public static String roomTypeCode = "roomTypeCode";// 房型代码
	public static String packageCode = "packageCode"; // 包价代码
	public static String rpt = "rpt"; // 房价类别
	public static String pmsColumnName = "pmsColumnName"; // UDF代码
	public static String paymentTransaction = "paymentTransaction";// 支付流程水
	public static String prepaidAmount = "prepaidAmount";// 支付金额
	public static String bookingSource = "bookingSource";// 渠道代码

	public static String mfSourceCode = "COL";// 来源代码，需要在pms里配置
	public static String mfMarketCode = "WHO";// 市场代码
	public static String mfChannelCode = "COL";// 传送给PMS渠道代码
	public static String mfCommentType = "RESERVATION";// 备注默认值
	public static String mfPaymentMethod = "CA";// 支付方式默认值
	public static String mfGuaranteeType = "6PM";// 担保类型默认值
	public static String nameTitle = "MR.";// 称谓默认值
	public static String ratePlanCodeDefault = "DIFF";// PMS修改房价时设置默认
	public static String roomTypeCodeDefault = "DIFF";// PMS修改房价时设置默认
	public static String pmsTypeDefault = "DEFAULT";// 订单下传到PMS的XML版本
	public static String pmsTableName = "RESERVATION_NAME";// 订单下传到PMS的Udf中pmsTableName
	public static String mfPmsColumnName = "UDFC30"; // UDF代码默认值
	public static String mfPmsRATEPLANLEVEL = "UDFC37"; // UDF代码默认值
	public static String paymentTransactionValName = "Payment Transaction";
	public static String prepaidAmountValName = "Prepaid Amount";
	public static String bookingSourceValName = "Booking Source";
	public static String createByValName = "Create By";
	public static String partyValName = "PARTY";
	public static String reservationReferenceType = "CHINAONLINE";
}
