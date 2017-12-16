package com.android.slowlifecourier.objectmodle;

import android.os.Parcel;

import com.util.ParcelableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgrape on 2017/5/25.
 * e-mail: sgrape1153@gmail.com
 */

public class OrderEntity extends ParcelableUtil<OrderEntity> implements Cloneable{

    public OrderEntity() {
    }


    /**
     * id : 402881f05c3f0b34015c3f12a8b50006
     * orderNumber : 15333333333-2879-20170525180646452
     * createDate : 2017-05-25 18:06:46
     * createUserId : 402881ed5bcdaa4f015bcdabe3c10001
     "orderOfCompanyId": "f0cad24a5c9f9af4015ca054760c0002",
     * createUserName :
     * createUserPhone : 15333333333
     * type : CityWide
     * type_value : 同城配送
     * status : UnReceivedOrder
     * status_value : 快递员未接单
     * payType : null
     * payType_value : null
     * payDate : null
     * transactionNumber : null
     * comment : null
     * userPayFee : 0
     * cost : 0
     * userActualFee : 0
     * payStatus : UnPayed
     * payStatus_value : 未付款
     * urgent : no
     * urgentFee : 0
     * insured : no
     * insuredFee : 0
     * weight : 0
     * postmanId : null
     * postmanName : null
     * postmanPhone : null
     * postmanCommpanyId : null
     * postmanCommpanyName : null
     * userChoiceCommpanyId : 402881f05c00a4fa015c00aa5a7e0001
     * userChoiceCommpanyName : 平台物流
     * logisticsNumber : null
     * startPro : 重庆市
     * startCity : 重庆市
     * startDistrict : 江北区
     * startStreet : 石马河街道
     * startHouseNumber : 321
     * startLng : 106.484413
     * startLat : 29.587856
     * receiverId : null
     * receiverName :
     * receiverPhone : 13333535383
     * endPro : 重庆市
     * endCity : 重庆市
     * endDistrict : 江北区
     * endStreet : 大石坝
     * endHouseNumber : 兔兔
     * endLng : 0
     * endLat : 0
     * shopId : null
     * shopName : null
     * arriveDate : null
     * postmanDate : null
     * shopSendDate : null
     * postmanSendDate : null
     * returnDate : null
     * returnReason : null
     * merchantReplay : null
     * evidenceImg : []
     * tradeImg : []
     * goods : []
     * orderRecord : [{"id":"402881f05c3f0b34015c3f12a8e00007","userOrderNumber":"15333333333-2879-20170525180646452","createDate":"2017-05-25 18:06:46","userIdOfModifyOrder":"402881ed5bcdaa4f015bcdabe3c10001","userNameOfModifyOrder":null,"userPhoneOfModifyOrder":"15333333333","operationDetails":"用户：15333333333于2017-05-25 18:06:46创建订单成功,等待快递员接单。"}]
     * billoflading : null
     * id2 : null
     */

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
    private String id2;
    private List<String> evidenceImg;
    private String tradeImg;
    private String userChoiceCommpanyCode;
    private ArrayList<BillCommodity> goods;
    private List<OrderRecordBean> orderRecord;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (OrderEntity)super.clone();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getOrderOfCompanyId() {
        return orderOfCompanyId;
    }

    public void setOrderOfCompanyId(String orderOfCompanyId) {
        this.orderOfCompanyId = orderOfCompanyId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserPhone() {
        return createUserPhone;
    }

    public void setCreateUserPhone(String createUserPhone) {
        this.createUserPhone = createUserPhone;
    }

    public String getUserChoiceCommpanyCode() {
        return userChoiceCommpanyCode;
    }

    public void setUserChoiceCommpanyCode(String userChoiceCommpanyCode) {
        this.userChoiceCommpanyCode = userChoiceCommpanyCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_value() {
        return type_value;
    }

    public void setType_value(String type_value) {
        this.type_value = type_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_value() {
        return status_value;
    }

    public void setStatus_value(String status_value) {
        this.status_value = status_value;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayType_value() {
        return payType_value;
    }

    public void setPayType_value(String payType_value) {
        this.payType_value = payType_value;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getUserPayFee() {
        return userPayFee;
    }

    public void setUserPayFee(double userPayFee) {
        this.userPayFee = userPayFee;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getUserActualFee() {
        return userActualFee;
    }

    public void setUserActualFee(double userActualFee) {
        this.userActualFee = userActualFee;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatus_value() {
        return payStatus_value;
    }

    public void setPayStatus_value(String payStatus_value) {
        this.payStatus_value = payStatus_value;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public double getUrgentFee() {
        return urgentFee;
    }

    public void setUrgentFee(double urgentFee) {
        this.urgentFee = urgentFee;
    }

    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
    }

    public double getInsuredFee() {
        return insuredFee;
    }

    public void setInsuredFee(double insuredFee) {
        this.insuredFee = insuredFee;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(String postmanId) {
        this.postmanId = postmanId;
    }

    public String getPostmanName() {
        return postmanName;
    }

    public void setPostmanName(String postmanName) {
        this.postmanName = postmanName;
    }

    public String getPostmanPhone() {
        return postmanPhone;
    }

    public void setPostmanPhone(String postmanPhone) {
        this.postmanPhone = postmanPhone;
    }

    public String getPostmanCommpanyId() {
        return postmanCommpanyId;
    }

    public void setPostmanCommpanyId(String postmanCommpanyId) {
        this.postmanCommpanyId = postmanCommpanyId;
    }

    public String getPostmanCommpanyName() {
        return postmanCommpanyName;
    }

    public void setPostmanCommpanyName(String postmanCommpanyName) {
        this.postmanCommpanyName = postmanCommpanyName;
    }

    public String getUserChoiceCommpanyId() {
        return userChoiceCommpanyId;
    }

    public void setUserChoiceCommpanyId(String userChoiceCommpanyId) {
        this.userChoiceCommpanyId = userChoiceCommpanyId;
    }

    public String getUserChoiceCommpanyName() {
        return userChoiceCommpanyName;
    }

    public void setUserChoiceCommpanyName(String userChoiceCommpanyName) {
        this.userChoiceCommpanyName = userChoiceCommpanyName;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getStartPro() {
        return startPro;
    }

    public void setStartPro(String startPro) {
        this.startPro = startPro;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartDistrict() {
        return startDistrict;
    }

    public void setStartDistrict(String startDistrict) {
        this.startDistrict = startDistrict;
    }

    public String getStartStreet() {
        return startStreet;
    }

    public void setStartStreet(String startStreet) {
        this.startStreet = startStreet;
    }

    public String getStartHouseNumber() {
        return startHouseNumber;
    }

    public void setStartHouseNumber(String startHouseNumber) {
        this.startHouseNumber = startHouseNumber;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getEndPro() {
        return endPro;
    }

    public void setEndPro(String endPro) {
        this.endPro = endPro;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndDistrict() {
        return endDistrict;
    }

    public void setEndDistrict(String endDistrict) {
        this.endDistrict = endDistrict;
    }

    public String getEndStreet() {
        return endStreet;
    }

    public void setEndStreet(String endStreet) {
        this.endStreet = endStreet;
    }

    public String getEndHouseNumber() {
        return endHouseNumber;
    }

    public void setEndHouseNumber(String endHouseNumber) {
        this.endHouseNumber = endHouseNumber;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getPostmanDate() {
        return postmanDate;
    }

    public void setPostmanDate(String postmanDate) {
        this.postmanDate = postmanDate;
    }

    public String getShopSendDate() {
        return shopSendDate;
    }

    public void setShopSendDate(String shopSendDate) {
        this.shopSendDate = shopSendDate;
    }

    public String getPostmanSendDate() {
        return postmanSendDate;
    }

    public void setPostmanSendDate(String postmanSendDate) {
        this.postmanSendDate = postmanSendDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getMerchantReplay() {
        return merchantReplay;
    }

    public void setMerchantReplay(String merchantReplay) {
        this.merchantReplay = merchantReplay;
    }

    public String getBilloflading() {
        return billoflading;
    }

    public void setBilloflading(String billoflading) {
        this.billoflading = billoflading;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public List<String> getEvidenceImg() {
        return evidenceImg;
    }

    public void setEvidenceImg(List<String> evidenceImg) {
        this.evidenceImg = evidenceImg;
    }

    public String getTradeImg() {
        return tradeImg;
    }

    public void setTradeImg(String tradeImg) {
        this.tradeImg = tradeImg;
    }

    public ArrayList<BillCommodity> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<BillCommodity> goods) {
        this.goods = goods;
    }

    public List<OrderRecordBean> getOrderRecord() {
        return orderRecord;
    }

    public void setOrderRecord(List<OrderRecordBean> orderRecord) {
        this.orderRecord = orderRecord;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public static class OrderRecordBean extends ParcelableUtil<OrderEntity> {
        /**
         * id : 402881f05c3f0b34015c3f12a8e00007
         * userOrderNumber : 15333333333-2879-20170525180646452
         * createDate : 2017-05-25 18:06:46
         * userIdOfModifyOrder : 402881ed5bcdaa4f015bcdabe3c10001
         * userNameOfModifyOrder : null
         * userPhoneOfModifyOrder : 15333333333
         * operationDetails : 用户：15333333333于2017-05-25 18:06:46创建订单成功,等待快递员接单。
         */

        private String id;
        private String userOrderNumber;
        private String createDate;
        private String userIdOfModifyOrder;
        private String userNameOfModifyOrder;
        private String userPhoneOfModifyOrder;
        private String operationDetails;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserOrderNumber() {
            return userOrderNumber;
        }

        public void setUserOrderNumber(String userOrderNumber) {
            this.userOrderNumber = userOrderNumber;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUserIdOfModifyOrder() {
            return userIdOfModifyOrder;
        }

        public void setUserIdOfModifyOrder(String userIdOfModifyOrder) {
            this.userIdOfModifyOrder = userIdOfModifyOrder;
        }

        public String getUserNameOfModifyOrder() {
            return userNameOfModifyOrder;
        }

        public void setUserNameOfModifyOrder(String userNameOfModifyOrder) {
            this.userNameOfModifyOrder = userNameOfModifyOrder;
        }

        public String getUserPhoneOfModifyOrder() {
            return userPhoneOfModifyOrder;
        }

        public void setUserPhoneOfModifyOrder(String userPhoneOfModifyOrder) {
            this.userPhoneOfModifyOrder = userPhoneOfModifyOrder;
        }

        public String getOperationDetails() {
            return operationDetails;
        }

        public void setOperationDetails(String operationDetails) {
            this.operationDetails = operationDetails;
        }
    }
}
