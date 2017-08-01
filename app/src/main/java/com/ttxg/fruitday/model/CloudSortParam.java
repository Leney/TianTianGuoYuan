package com.ttxg.fruitday.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 前端云店搜索参数
 * 
 * @author BG
 * @author 2016年3月10日
 */
public class CloudSortParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String keyword;// 搜索关键字
	private Integer st1;// 搜索类型 1：按分类
	private Integer st2;// 搜索类型 2：按排序
	private Integer st3;// 搜索类型 3：按距离
	private Integer sv1;// 搜索值 1：按分类
	private Integer sv2;// 搜索值 2：按排序
	private Integer sv3;// 搜索值 3：按距离
	private BigDecimal lat;// 纬度
	private BigDecimal lng;// 经度
	private BigDecimal beginDistance;// 开始距离
	private BigDecimal endDistance;// 结束距离
	private Integer storeType;//店铺类型 9 云店
	private Integer appId;//公众号id

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getSt1() {
		return st1;
	}

	public void setSt1(Integer st1) {
		this.st1 = st1;
	}

	public Integer getSt2() {
		return st2;
	}

	public void setSt2(Integer st2) {
		this.st2 = st2;
	}

	public Integer getSt3() {
		return st3;
	}

	public void setSt3(Integer st3) {
		this.st3 = st3;
	}

	public Integer getSv1() {
		return sv1;
	}

	public void setSv1(Integer sv1) {
		this.sv1 = sv1;
	}

	public Integer getSv2() {
		return sv2;
	}

	public void setSv2(Integer sv2) {
		this.sv2 = sv2;
	}

	public Integer getSv3() {
		return sv3;
	}

	public void setSv3(Integer sv3) {
		this.sv3 = sv3;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public BigDecimal getBeginDistance() {
		return beginDistance;
	}

	public void setBeginDistance(BigDecimal beginDistance) {
		this.beginDistance = beginDistance;
	}

	public BigDecimal getEndDistance() {
		return endDistance;
	}

	public void setEndDistance(BigDecimal endDistance) {
		this.endDistance = endDistance;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	
}
