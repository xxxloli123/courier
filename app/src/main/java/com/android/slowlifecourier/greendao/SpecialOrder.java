package com.android.slowlifecourier.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xxxloli on 2017/12/19.
 */
@Entity
public class SpecialOrder {
    @Id(autoincrement = true)
    private Long writId;
    private String id;
    private String goodsName;
    private String orderNumber;
    private String createDate;
    private String createUserId;
    private String orderOfCompanyId;
    private String createUserName;
    private String createUserPhone;
    private String type;
    private String type_value;
    private String status;
    private String status_value;
    private String payType;
    private String payType_value;
    private String payDate;
    private String transactionNumber;
    private String comment;
    private double userPayFee;
    private double cost;
    private double userActualFee;
    private String payStatus;
    private String payStatus_value;
    private String urgent;
    private double urgentFee;
    private String insured;
    private double insuredFee;
    private float weight;
    private String postmanId;
    private String postmanName;
    private String postmanPhone;
    private String postmanCommpanyId;
    private String postmanCommpanyName;
    private String userChoiceCommpanyId;
    private String userChoiceCommpanyName;
    private String logisticsNumber;
    private String startPro;
    private String startCity;
    private String startDistrict;
    private String startStreet;
    private String startHouseNumber;
    private double startLng;
    private double startLat;
    private String receiverId;
    private String receiverName;
    private String receiverPhone;
    private String endPro;
    private String endCity;
    private String endDistrict;
    private String endStreet;
    private String endHouseNumber;
    private double endLng;
    private double endLat;
    private String shopId;
    private String shopName;
    private String arriveDate;
    private String postmanDate;
    private String shopSendDate;
    private String postmanSendDate;
    private String returnDate;
    private String returnReason;
    private String merchantReplay;
    private String billoflading;
    @Generated(hash = 381147254)
    public SpecialOrder(Long writId, String id, String goodsName,
            String orderNumber, String createDate, String createUserId,
            String orderOfCompanyId, String createUserName, String createUserPhone,
            String type, String type_value, String status, String status_value,
            String payType, String payType_value, String payDate,
            String transactionNumber, String comment, double userPayFee,
            double cost, double userActualFee, String payStatus,
            String payStatus_value, String urgent, double urgentFee, String insured,
            double insuredFee, float weight, String postmanId, String postmanName,
            String postmanPhone, String postmanCommpanyId,
            String postmanCommpanyName, String userChoiceCommpanyId,
            String userChoiceCommpanyName, String logisticsNumber, String startPro,
            String startCity, String startDistrict, String startStreet,
            String startHouseNumber, double startLng, double startLat,
            String receiverId, String receiverName, String receiverPhone,
            String endPro, String endCity, String endDistrict, String endStreet,
            String endHouseNumber, double endLng, double endLat, String shopId,
            String shopName, String arriveDate, String postmanDate,
            String shopSendDate, String postmanSendDate, String returnDate,
            String returnReason, String merchantReplay, String billoflading) {
        this.writId = writId;
        this.id = id;
        this.goodsName = goodsName;
        this.orderNumber = orderNumber;
        this.createDate = createDate;
        this.createUserId = createUserId;
        this.orderOfCompanyId = orderOfCompanyId;
        this.createUserName = createUserName;
        this.createUserPhone = createUserPhone;
        this.type = type;
        this.type_value = type_value;
        this.status = status;
        this.status_value = status_value;
        this.payType = payType;
        this.payType_value = payType_value;
        this.payDate = payDate;
        this.transactionNumber = transactionNumber;
        this.comment = comment;
        this.userPayFee = userPayFee;
        this.cost = cost;
        this.userActualFee = userActualFee;
        this.payStatus = payStatus;
        this.payStatus_value = payStatus_value;
        this.urgent = urgent;
        this.urgentFee = urgentFee;
        this.insured = insured;
        this.insuredFee = insuredFee;
        this.weight = weight;
        this.postmanId = postmanId;
        this.postmanName = postmanName;
        this.postmanPhone = postmanPhone;
        this.postmanCommpanyId = postmanCommpanyId;
        this.postmanCommpanyName = postmanCommpanyName;
        this.userChoiceCommpanyId = userChoiceCommpanyId;
        this.userChoiceCommpanyName = userChoiceCommpanyName;
        this.logisticsNumber = logisticsNumber;
        this.startPro = startPro;
        this.startCity = startCity;
        this.startDistrict = startDistrict;
        this.startStreet = startStreet;
        this.startHouseNumber = startHouseNumber;
        this.startLng = startLng;
        this.startLat = startLat;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.endPro = endPro;
        this.endCity = endCity;
        this.endDistrict = endDistrict;
        this.endStreet = endStreet;
        this.endHouseNumber = endHouseNumber;
        this.endLng = endLng;
        this.endLat = endLat;
        this.shopId = shopId;
        this.shopName = shopName;
        this.arriveDate = arriveDate;
        this.postmanDate = postmanDate;
        this.shopSendDate = shopSendDate;
        this.postmanSendDate = postmanSendDate;
        this.returnDate = returnDate;
        this.returnReason = returnReason;
        this.merchantReplay = merchantReplay;
        this.billoflading = billoflading;
    }
    @Generated(hash = 779478380)
    public SpecialOrder() {
    }
    public Long getWritId() {
        return this.writId;
    }
    public void setWritId(Long writId) {
        this.writId = writId;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGoodsName() {
        return this.goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getOrderNumber() {
        return this.orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateUserId() {
        return this.createUserId;
    }
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    public String getOrderOfCompanyId() {
        return this.orderOfCompanyId;
    }
    public void setOrderOfCompanyId(String orderOfCompanyId) {
        this.orderOfCompanyId = orderOfCompanyId;
    }
    public String getCreateUserName() {
        return this.createUserName;
    }
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
    public String getCreateUserPhone() {
        return this.createUserPhone;
    }
    public void setCreateUserPhone(String createUserPhone) {
        this.createUserPhone = createUserPhone;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType_value() {
        return this.type_value;
    }
    public void setType_value(String type_value) {
        this.type_value = type_value;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus_value() {
        return this.status_value;
    }
    public void setStatus_value(String status_value) {
        this.status_value = status_value;
    }
    public String getPayType() {
        return this.payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getPayType_value() {
        return this.payType_value;
    }
    public void setPayType_value(String payType_value) {
        this.payType_value = payType_value;
    }
    public String getPayDate() {
        return this.payDate;
    }
    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
    public String getTransactionNumber() {
        return this.transactionNumber;
    }
    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public double getUserPayFee() {
        return this.userPayFee;
    }
    public void setUserPayFee(double userPayFee) {
        this.userPayFee = userPayFee;
    }
    public double getCost() {
        return this.cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public double getUserActualFee() {
        return this.userActualFee;
    }
    public void setUserActualFee(double userActualFee) {
        this.userActualFee = userActualFee;
    }
    public String getPayStatus() {
        return this.payStatus;
    }
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
    public String getPayStatus_value() {
        return this.payStatus_value;
    }
    public void setPayStatus_value(String payStatus_value) {
        this.payStatus_value = payStatus_value;
    }
    public String getUrgent() {
        return this.urgent;
    }
    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }
    public double getUrgentFee() {
        return this.urgentFee;
    }
    public void setUrgentFee(double urgentFee) {
        this.urgentFee = urgentFee;
    }
    public String getInsured() {
        return this.insured;
    }
    public void setInsured(String insured) {
        this.insured = insured;
    }
    public double getInsuredFee() {
        return this.insuredFee;
    }
    public void setInsuredFee(double insuredFee) {
        this.insuredFee = insuredFee;
    }
    public float getWeight() {
        return this.weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public String getPostmanId() {
        return this.postmanId;
    }
    public void setPostmanId(String postmanId) {
        this.postmanId = postmanId;
    }
    public String getPostmanName() {
        return this.postmanName;
    }
    public void setPostmanName(String postmanName) {
        this.postmanName = postmanName;
    }
    public String getPostmanPhone() {
        return this.postmanPhone;
    }
    public void setPostmanPhone(String postmanPhone) {
        this.postmanPhone = postmanPhone;
    }
    public String getPostmanCommpanyId() {
        return this.postmanCommpanyId;
    }
    public void setPostmanCommpanyId(String postmanCommpanyId) {
        this.postmanCommpanyId = postmanCommpanyId;
    }
    public String getPostmanCommpanyName() {
        return this.postmanCommpanyName;
    }
    public void setPostmanCommpanyName(String postmanCommpanyName) {
        this.postmanCommpanyName = postmanCommpanyName;
    }
    public String getUserChoiceCommpanyId() {
        return this.userChoiceCommpanyId;
    }
    public void setUserChoiceCommpanyId(String userChoiceCommpanyId) {
        this.userChoiceCommpanyId = userChoiceCommpanyId;
    }
    public String getUserChoiceCommpanyName() {
        return this.userChoiceCommpanyName;
    }
    public void setUserChoiceCommpanyName(String userChoiceCommpanyName) {
        this.userChoiceCommpanyName = userChoiceCommpanyName;
    }
    public String getLogisticsNumber() {
        return this.logisticsNumber;
    }
    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }
    public String getStartPro() {
        return this.startPro;
    }
    public void setStartPro(String startPro) {
        this.startPro = startPro;
    }
    public String getStartCity() {
        return this.startCity;
    }
    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }
    public String getStartDistrict() {
        return this.startDistrict;
    }
    public void setStartDistrict(String startDistrict) {
        this.startDistrict = startDistrict;
    }
    public String getStartStreet() {
        return this.startStreet;
    }
    public void setStartStreet(String startStreet) {
        this.startStreet = startStreet;
    }
    public String getStartHouseNumber() {
        return this.startHouseNumber;
    }
    public void setStartHouseNumber(String startHouseNumber) {
        this.startHouseNumber = startHouseNumber;
    }
    public double getStartLng() {
        return this.startLng;
    }
    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }
    public double getStartLat() {
        return this.startLat;
    }
    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }
    public String getReceiverId() {
        return this.receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public String getReceiverName() {
        return this.receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getReceiverPhone() {
        return this.receiverPhone;
    }
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
    public String getEndPro() {
        return this.endPro;
    }
    public void setEndPro(String endPro) {
        this.endPro = endPro;
    }
    public String getEndCity() {
        return this.endCity;
    }
    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }
    public String getEndDistrict() {
        return this.endDistrict;
    }
    public void setEndDistrict(String endDistrict) {
        this.endDistrict = endDistrict;
    }
    public String getEndStreet() {
        return this.endStreet;
    }
    public void setEndStreet(String endStreet) {
        this.endStreet = endStreet;
    }
    public String getEndHouseNumber() {
        return this.endHouseNumber;
    }
    public void setEndHouseNumber(String endHouseNumber) {
        this.endHouseNumber = endHouseNumber;
    }
    public double getEndLng() {
        return this.endLng;
    }
    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }
    public double getEndLat() {
        return this.endLat;
    }
    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }
    public String getShopId() {
        return this.shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public String getShopName() {
        return this.shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getArriveDate() {
        return this.arriveDate;
    }
    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }
    public String getPostmanDate() {
        return this.postmanDate;
    }
    public void setPostmanDate(String postmanDate) {
        this.postmanDate = postmanDate;
    }
    public String getShopSendDate() {
        return this.shopSendDate;
    }
    public void setShopSendDate(String shopSendDate) {
        this.shopSendDate = shopSendDate;
    }
    public String getPostmanSendDate() {
        return this.postmanSendDate;
    }
    public void setPostmanSendDate(String postmanSendDate) {
        this.postmanSendDate = postmanSendDate;
    }
    public String getReturnDate() {
        return this.returnDate;
    }
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
    public String getReturnReason() {
        return this.returnReason;
    }
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
    public String getMerchantReplay() {
        return this.merchantReplay;
    }
    public void setMerchantReplay(String merchantReplay) {
        this.merchantReplay = merchantReplay;
    }
    public String getBilloflading() {
        return this.billoflading;
    }
    public void setBilloflading(String billoflading) {
        this.billoflading = billoflading;
    }
}
