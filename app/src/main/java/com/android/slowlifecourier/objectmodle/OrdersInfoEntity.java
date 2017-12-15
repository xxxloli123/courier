package com.android.slowlifecourier.objectmodle;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class OrdersInfoEntity {
    private String tag="0";//0完成单 1异常单 2取消单
    private double price;
    private String commodityDetails;
    private String transactionNumber;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCommodityDetails() {
        return commodityDetails;
    }

    public void setCommodityDetails(String commodityDetails) {
        this.commodityDetails = commodityDetails;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
}
