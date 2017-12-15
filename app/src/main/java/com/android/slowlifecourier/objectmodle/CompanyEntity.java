package com.android.slowlifecourier.objectmodle;

import java.util.List;

/**
 * Created by sgrape on 2017/5/24.
 * e-mail: sgrape1153@gmail.com
 */

public class CompanyEntity {


    /**
     * id : 402881ed5bd86638015bd867019b0000
     * idNumber : 123
     * idConfirm : null
     * idConfirm_value : null
     * isPlatform : no
     * account : null
     * name : 韵达
     * legalname : 2
     * legalphone : 13594728101
     * telePhone : null
     * companyusers : []
     * mailbox : null
     * registerDate : 2017-05-05 19:38:01
     * companyImg : null
     * idImgs : []
     * brief : 123
     * status : Normal
     * status_value : 正常使用公司
     * id2 : null
     */

    private String id;
    private String idNumber;
    private String idConfirm;
    private String idConfirm_value;
    private String isPlatform;
    private String account;
    private String name;
    private String legalname;
    private String legalphone;
    private String telePhone;
    private String mailbox;
    private String registerDate;
    private String companyImg;
    private String brief;
    private String status;
    private String status_value;
    private String id2;
    private List<String> companyusers;
    private List<IdImgs> idImgs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdConfirm() {
        return idConfirm;
    }

    public void setIdConfirm(String idConfirm) {
        this.idConfirm = idConfirm;
    }

    public String getIdConfirm_value() {
        return idConfirm_value;
    }

    public void setIdConfirm_value(String idConfirm_value) {
        this.idConfirm_value = idConfirm_value;
    }

    public String getIsPlatform() {
        return isPlatform;
    }

    public void setIsPlatform(String isPlatform) {
        this.isPlatform = isPlatform;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalname() {
        return legalname;
    }

    public void setLegalname(String legalname) {
        this.legalname = legalname;
    }

    public String getLegalphone() {
        return legalphone;
    }

    public void setLegalphone(String legalphone) {
        this.legalphone = legalphone;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getCompanyImg() {
        return companyImg;
    }

    public void setCompanyImg(String companyImg) {
        this.companyImg = companyImg;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
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

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public List<String> getCompanyusers() {
        return companyusers;
    }

    public void setCompanyusers(List<String> companyusers) {
        this.companyusers = companyusers;
    }

    public List<IdImgs> getIdImgs() {
        return idImgs;
    }

    public void setIdImgs(List<IdImgs> idImgs) {
        this.idImgs = idImgs;
    }

    public static class IdImgs {

        /**
         * id : 402881f05c356594015c3565efe60001
         * imgName : f514b836-39e7-4679-99ed-cfb2f373bf0b.jpg
         * uploadDate : 2017-05-23 21:01:32
         */

        private String id;
        private String imgName;
        private String uploadDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public String getUploadDate() {
            return uploadDate;
        }

        public void setUploadDate(String uploadDate) {
            this.uploadDate = uploadDate;
        }
    }
}
