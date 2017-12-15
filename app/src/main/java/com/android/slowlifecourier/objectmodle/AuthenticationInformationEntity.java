package com.android.slowlifecourier.objectmodle;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class AuthenticationInformationEntity {
    private String identityTag="0";//身份证认证（0未认证 1已认证）
    private int healthTag;//健康证认证
    private int driverTag;//驾驶证认证
    private int vehicleTag;//车辆认证
    private String identityName;//身份证名
    private String healthName;//健康证名
    private  String driverName;//驾驶证名
    private String vehicleName;//车辆名

    public String getIdentityTag() {
        return identityTag;
    }

    public void setIdentityTag(String identityTag) {
        this.identityTag = identityTag;
    }

    public int getHealthTag() {
        return healthTag;
    }

    public void setHealthTag(int healthTag) {
        this.healthTag = healthTag;
    }

    public int getDriverTag() {
        return driverTag;
    }

    public void setDriverTag(int driverTag) {
        this.driverTag = driverTag;
    }

    public int getVehicleTag() {
        return vehicleTag;
    }

    public void setVehicleTag(int vehicleTag) {
        this.vehicleTag = vehicleTag;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getHealthName() {
        return healthName;
    }

    public void setHealthName(String healthName) {
        this.healthName = healthName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
