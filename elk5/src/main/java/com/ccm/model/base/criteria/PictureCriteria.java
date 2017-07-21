package com.ccm.model.base.criteria;

/**
 * 图片查询对象
 */
public class PictureCriteria extends SearchCriteria {

	private static final long serialVersionUID = 4729750990394561462L;

	private String cateId; // 分类ID

	private String picSysName; // 图片系统名称

	private String specName; // 切图规格，11种？针对酒店和房型

	private String picName;// 图片名称

	private String bizId;// 业务对象

	private String bizType;// 业务对象类型：房型，酒店，商家，客户

	private String srcFlag;// 是否原图

	private String cutFlag;// 是否做过切图标志,用在图片处理

	private String defaultFlag; // 是否默认图片，如果是默认图片，在对多图房型和酒店，查询列表中默认显示的图片,详情页的第一张图片

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getPicSysName() {
		return picSysName;
	}

	public void setPicSysName(String picSysName) {
		this.picSysName = picSysName;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	/**
	 * @return the bizId
	 */
	public String getBizId() {
		return bizId;
	}

	/**
	 * @param bizId
	 *            the bizId to set
	 */
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	/**
	 * @return the bizType
	 */
	public String getBizType() {
		return bizType;
	}

	/**
	 * @param bizType
	 *            the bizType to set
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getSrcFlag() {
		return srcFlag;
	}

	public void setSrcFlag(String srcFlag) {
		this.srcFlag = srcFlag;
	}

	public String getCutFlag() {
		return cutFlag;
	}

	public void setCutFlag(String cutFlag) {
		this.cutFlag = cutFlag;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
