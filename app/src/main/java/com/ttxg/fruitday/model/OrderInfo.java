package com.ttxg.fruitday.model;

import java.io.Serializable;

/**
 * 订单信息表
 * @author 龙连忠
 * @since  2015-08-12
 */
public class OrderInfo implements Serializable {
	//静态值 by 龙连忠 2015-09-09 begin
	/** 待支付 = 1 */
	public static final int ORDER_STATUS_NEW = 1;
	/** 已付款 = 2 */
	public static final int ORDER_STATUS_PAID = 2;
	/** 已发货 = 3 */
	public static final int ORDER_STATUS_SENT = 3;
	/** 客户签收 = 4*/
	public static final int ORDER_STATUS_SIGN = 4;
	/** 已取消 = 5 */
	public static final int ORDER_STATUS_CANCEL = 5;
	
	/** 未删除 =false */
	public static final boolean ORDER_REMOVE_NOT = false;
	/** 已删除 = true */
	public static final boolean ORDER_REMOVE_HAS = true;
	
	/** 支付方式-在线支付 = 1 */
	public static final int PAY_ONLINE = 1;
	/** 支付方式-货到付款 = 2 */
	public static final int PAY_OFFLINE = 2;
	
	/** 提货方式- 自提*/
	public static final int TAKETYPR_SINCE=1;
	/** 提货方式- 物流*/
	public static final int TAKETYPR_LOGISTICS=2;
	
	/** 未评价 */
	public static final boolean	ISRATE_NOT=false;
	/** 已评价 */
	public static final boolean	ISRATE_HAS=true;
	
	/** 是否有邮费-不包邮=false */
	public static final boolean ISFREESHIPPING_NOT=false;
	/** 是否有邮费 -包邮=true*/
	public static final boolean ISFREESHIPPING_HAS=true;
	
	/**改价状态 - 未改价=0*/
	public static final int PRICEMODIFYSTATUS_NOT=0;
	/**改价状态 - 已改价=1*/
	public static final int PRICEMODIFYSTATUS_HAS=1;
	/**改价状态 - 确认改价*/
	public static final int PRICEMODIFYSTATUS_CONFIRM=2;
	/**改价状态 - 取消改价*/
	public static final int PRICEMODIFYSTATUS_CANCEL=3;
	//静态值 by 龙连忠 2015-09-09 end
	
    private int id;					//唯一标示符
    private String serialNumber;		//订单流水号
    private String createTime;	//订单创建时间
    private String payTime;				//订单付款时间
    private int logisticsType;		//物流类型(1快递，2EMS,3平邮等)
    private int userId;				//买家id
    private int orderStatus;		//订单状态(1待支付,未支付;2已支付,待发货;3,已发货,待收货,4,已收货,完成交易;5,已取消)
    private int orderType;			//订单类型(1普通,2团购,3预约,4抢购)
    private double totalPrice;			//商品总金额
    private double realPayment;			//实际付款（商品总金额 + 邮费）
    private double reductionPrice;		//减免费用
    private boolean isfreeShipping;		//是否有邮费(false不包邮,true包邮 )
    private double postage;				//邮费
    private int usePoint;			//使用点数
    private int counterPoint;		//返点数
    private String buyersIp;			//买家ip
    private String orderno;				//订单编号
    private boolean israte;				//是否评价(false 没评价,true 已评价)
    private boolean isremove;			//是否删除(false 没删除,true已删除)
    private String receiveName;			//收货人
    private String receiveProvince;		//收货人省份地址
    private String receiveCity;			//收货人市地址
    private String receiveArea;			//收货人区/县/二级城市地址
    private String receiveAddress;		//收货详细地址
    private String receiveMobile;		//收货人手机
    private String receiveTelephone;	//收货人电话
    private String senderZip;			//邮政编码
    private String buyerMessage;		//买家留言
    private String signTime;			//签收时间
    private String colseTime;			//关闭时间
    private String deleteTime;			//删除时间
    private String consignTime;			//发货时间
    private int isRemarks;          //是否商家备注(默认0未备注1已备注2取消备注)is_remarks
    private String remarks;				//商家备注
    private int payStyle;			//支付方式 (1,在线支付,2货到付款)
    private boolean isinv;				//是否需要发票
    private String invPayee;			//发票抬头
    private int fhUserId;			//发货人id
    private int xsStoreId;			//销售店铺id
    private int appId;				//来源公众号
    private int takeType;			//提货方式(1.买家自提;2.物流配送)
    private int priceModifyStatus;	//改价状态(2.确认改价,3取消改价,1.已改价;0.未改价[ 默认 ] )
    private String logisticsCompany;	//物流公司
    private String logisticsCompanyCode;//物流公司代码
   
	private String trackingNumber;		//运单号码
    private String sendName;			//发货人
    private String sendProvince;		//发货人省份地址
    private String sendCity;			//发货人市地址
    private String sendArea;			//发货人区/县/二级城市地址
    private String sendAddress;			//发货详细地址
    private String sendMobile;			//发货人手机
    private String salesOutlets;		//销售店铺
    private String buyers;			    //买家
    private int isVirtue;	//是否虚拟订单   1：是虚拟订单
    private String fhAccount;		//发货方账号： 虚拟订单时临时存放字段
    
    private double discount;	//会员打折
    private double beforeDiscountPrice;	//会员打折前总价
    private double afterDiscountPrice;	//会员打折后总价

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public int getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(int logisticsType) {
		this.logisticsType = logisticsType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getRealPayment() {
		return realPayment;
	}

	public void setRealPayment(double realPayment) {
		this.realPayment = realPayment;
	}

	public double getReductionPrice() {
		return reductionPrice;
	}

	public void setReductionPrice(double reductionPrice) {
		this.reductionPrice = reductionPrice;
	}

	public boolean isfreeShipping() {
		return isfreeShipping;
	}

	public void setIsfreeShipping(boolean isfreeShipping) {
		this.isfreeShipping = isfreeShipping;
	}

	public double getPostage() {
		return postage;
	}

	public void setPostage(double postage) {
		this.postage = postage;
	}

	public int getUsePoint() {
		return usePoint;
	}

	public void setUsePoint(int usePoint) {
		this.usePoint = usePoint;
	}

	public int getCounterPoint() {
		return counterPoint;
	}

	public void setCounterPoint(int counterPoint) {
		this.counterPoint = counterPoint;
	}

	public String getBuyersIp() {
		return buyersIp;
	}

	public void setBuyersIp(String buyersIp) {
		this.buyersIp = buyersIp;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public boolean israte() {
		return israte;
	}

	public void setIsrate(boolean israte) {
		this.israte = israte;
	}

	public boolean isremove() {
		return isremove;
	}

	public void setIsremove(boolean isremove) {
		this.isremove = isremove;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceiveProvince() {
		return receiveProvince;
	}

	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}

	public String getReceiveCity() {
		return receiveCity;
	}

	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}

	public String getReceiveArea() {
		return receiveArea;
	}

	public void setReceiveArea(String receiveArea) {
		this.receiveArea = receiveArea;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getReceiveMobile() {
		return receiveMobile;
	}

	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}

	public String getReceiveTelephone() {
		return receiveTelephone;
	}

	public void setReceiveTelephone(String receiveTelephone) {
		this.receiveTelephone = receiveTelephone;
	}

	public String getSenderZip() {
		return senderZip;
	}

	public void setSenderZip(String senderZip) {
		this.senderZip = senderZip;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getColseTime() {
		return colseTime;
	}

	public void setColseTime(String colseTime) {
		this.colseTime = colseTime;
	}

	public String getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(String deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getConsignTime() {
		return consignTime;
	}

	public void setConsignTime(String consignTime) {
		this.consignTime = consignTime;
	}

	public int getIsRemarks() {
		return isRemarks;
	}

	public void setIsRemarks(int isRemarks) {
		this.isRemarks = isRemarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getPayStyle() {
		return payStyle;
	}

	public void setPayStyle(int payStyle) {
		this.payStyle = payStyle;
	}

	public boolean isinv() {
		return isinv;
	}

	public void setIsinv(boolean isinv) {
		this.isinv = isinv;
	}

	public String getInvPayee() {
		return invPayee;
	}

	public void setInvPayee(String invPayee) {
		this.invPayee = invPayee;
	}

	public int getFhUserId() {
		return fhUserId;
	}

	public void setFhUserId(int fhUserId) {
		this.fhUserId = fhUserId;
	}

	public int getXsStoreId() {
		return xsStoreId;
	}

	public void setXsStoreId(int xsStoreId) {
		this.xsStoreId = xsStoreId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getTakeType() {
		return takeType;
	}

	public void setTakeType(int takeType) {
		this.takeType = takeType;
	}

	public int getPriceModifyStatus() {
		return priceModifyStatus;
	}

	public void setPriceModifyStatus(int priceModifyStatus) {
		this.priceModifyStatus = priceModifyStatus;
	}

	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public String getLogisticsCompanyCode() {
		return logisticsCompanyCode;
	}

	public void setLogisticsCompanyCode(String logisticsCompanyCode) {
		this.logisticsCompanyCode = logisticsCompanyCode;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendProvince() {
		return sendProvince;
	}

	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}

	public String getSendCity() {
		return sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}

	public String getSendArea() {
		return sendArea;
	}

	public void setSendArea(String sendArea) {
		this.sendArea = sendArea;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendMobile() {
		return sendMobile;
	}

	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}

	public String getSalesOutlets() {
		return salesOutlets;
	}

	public void setSalesOutlets(String salesOutlets) {
		this.salesOutlets = salesOutlets;
	}

	public String getBuyers() {
		return buyers;
	}

	public void setBuyers(String buyers) {
		this.buyers = buyers;
	}

	public int getIsVirtue() {
		return isVirtue;
	}

	public void setIsVirtue(int isVirtue) {
		this.isVirtue = isVirtue;
	}

	public String getFhAccount() {
		return fhAccount;
	}

	public void setFhAccount(String fhAccount) {
		this.fhAccount = fhAccount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getBeforeDiscountPrice() {
		return beforeDiscountPrice;
	}

	public void setBeforeDiscountPrice(double beforeDiscountPrice) {
		this.beforeDiscountPrice = beforeDiscountPrice;
	}

	public double getAfterDiscountPrice() {
		return afterDiscountPrice;
	}

	public void setAfterDiscountPrice(double afterDiscountPrice) {
		this.afterDiscountPrice = afterDiscountPrice;
	}

//	@Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getClass().getSimpleName());
//        sb.append(" [");
//        sb.append("Hash = ").append(hashCode());
//        sb.append(", id=").append(id);
//        sb.append(", serialNumber=").append(serialNumber);
//        sb.append(", createTime=").append(createTime);
//        sb.append(", payTime=").append(payTime);
//        sb.append(", logisticsType=").append(logisticsType);
//        sb.append(", userId=").append(userId);
//        sb.append(", orderStatus=").append(orderStatus);
//        sb.append(", orderType=").append(orderType);
//        sb.append(", totalPrice=").append(totalPrice);
//        sb.append(", realPayment=").append(realPayment);
//        sb.append(", reductionPrice=").append(reductionPrice);
//        sb.append(", isfreeShipping=").append(isfreeShipping);
//        sb.append(", postage=").append(postage);
//        sb.append(", usePoint=").append(usePoint);
//        sb.append(", counterPoint=").append(counterPoint);
//        sb.append(", buyersIp=").append(buyersIp);
//        sb.append(", orderno=").append(orderno);
//        sb.append(", israte=").append(israte);
//        sb.append(", isremove=").append(isremove);
//        sb.append(", receiveName=").append(receiveName);
//        sb.append(", receiveProvince=").append(receiveProvince);
//        sb.append(", receiveCity=").append(receiveCity);
//        sb.append(", receiveArea=").append(receiveArea);
//        sb.append(", receiveAddress=").append(receiveAddress);
//        sb.append(", receiveMobile=").append(receiveMobile);
//        sb.append(", receiveTelephone=").append(receiveTelephone);
//        sb.append(", senderZip=").append(senderZip);
//        sb.append(", buyerMessage=").append(buyerMessage);
//        sb.append(", signTime=").append(signTime);
//        sb.append(", colseTime=").append(colseTime);
//        sb.append(", deleteTime=").append(deleteTime);
//        sb.append(", consignTime=").append(consignTime);
//        sb.append(", isRemarks=").append(isRemarks);
//        sb.append(", remarks=").append(remarks);
//        sb.append(", payStyle=").append(payStyle);
//        sb.append(", isinv=").append(isinv);
//        sb.append(", invPayee=").append(invPayee);
//        sb.append(", fhUserId=").append(fhUserId);
//        sb.append(", xsStoreId=").append(xsStoreId);
//        sb.append(", appId=").append(appId);
//        sb.append(", takeType=").append(takeType);
//        sb.append(", priceModifyStatus=").append(priceModifyStatus);
//        sb.append(", logisticsCompany=").append(logisticsCompany);
//        sb.append(", trackingNumber=").append(trackingNumber);
//        sb.append(", sendName=").append(sendName);
//        sb.append(", sendProvince=").append(sendProvince);
//        sb.append(", sendCity=").append(sendCity);
//        sb.append(", sendArea=").append(sendArea);
//        sb.append(", sendAddress=").append(sendAddress);
//        sb.append(", sendMobile=").append(sendMobile);
//        sb.append(", salesOutlets=").append(salesOutlets);
//        sb.append(", buyers=").append(buyers);
//        sb.append(", isVirtue=").append(isVirtue);
//        sb.append(", fhAccount=").append(fhAccount);
//        sb.append(", discount=").append(fhAccount);
//        sb.append(", beforeDiscountPrice=").append(fhAccount);
//        sb.append(", afterDiscountPrice=").append(fhAccount);
//        sb.append("]");
//        return sb.toString();
//    }
}