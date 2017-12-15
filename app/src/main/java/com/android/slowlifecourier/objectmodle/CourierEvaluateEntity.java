package com.android.slowlifecourier.objectmodle;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class CourierEvaluateEntity {
    /**
     * evaluateInfo : {"iTotalRecords":8,"pageCount":8,"currPage":1,"aaData":[{"id":"402880e95e171ec1015e172d9c5b0004","orderId":"2c9ad8435dd0534e015e0ec146c80190","userId":"f0cad24a5d1b9fe1015d1bb46f1b0030","name":null,"phone":"18102340246","headerImg":"1bd0cc62-435d-46b4-8e39-dfe10920a244.png","anonymity":"yes","userNickName":"李忠祥","grade":2,"createDate":"2017-08-25 10:16:59","comment":"送货快，五星好评！！！","evaluateType":"express","evaluateType_value":"快递员","typeId":"2c9ad8435d40a6e5015d43d951d200b7","typeName":"黄杰","replycomment":null,"replydate":null,"replyStatus":"no","tag1":"0","tag2":"0","tag3":"0","tag4":"0","tag5":"0","tag6":"0","tag7":null,"tag8":null,"tag9":null,"tag10":null}],"iTotalDisplayRecords":1,"sEcho":"1"}
     * message : 查询成功(028)
     * statusCode : 200
     */

    private EvaluateInfoBean evaluateInfo;
    private String message;
    private int statusCode;

    public EvaluateInfoBean getEvaluateInfo() {
        return evaluateInfo;
    }

    public void setEvaluateInfo(EvaluateInfoBean evaluateInfo) {
        this.evaluateInfo = evaluateInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public static class EvaluateInfoBean {
        /**
         * iTotalRecords : 8
         * pageCount : 8
         * currPage : 1
         * aaData : [{"id":"402880e95e171ec1015e172d9c5b0004","orderId":"2c9ad8435dd0534e015e0ec146c80190","userId":"f0cad24a5d1b9fe1015d1bb46f1b0030","name":null,"phone":"18102340246","headerImg":"1bd0cc62-435d-46b4-8e39-dfe10920a244.png","anonymity":"yes","userNickName":"李忠祥","grade":2,"createDate":"2017-08-25 10:16:59","comment":"送货快，五星好评！！！","evaluateType":"express","evaluateType_value":"快递员","typeId":"2c9ad8435d40a6e5015d43d951d200b7","typeName":"黄杰","replycomment":null,"replydate":null,"replyStatus":"no","tag1":"0","tag2":"0","tag3":"0","tag4":"0","tag5":"0","tag6":"0","tag7":null,"tag8":null,"tag9":null,"tag10":null}]
         * iTotalDisplayRecords : 1
         * sEcho : 1
         */

        private int iTotalRecords;
        private int pageCount;
        private int currPage;
        private int iTotalDisplayRecords;
        private String sEcho;
        private List<AaDataBean> aaData;

        public int getITotalRecords() {
            return iTotalRecords;
        }

        public void setITotalRecords(int iTotalRecords) {
            this.iTotalRecords = iTotalRecords;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public int getITotalDisplayRecords() {
            return iTotalDisplayRecords;
        }

        public void setITotalDisplayRecords(int iTotalDisplayRecords) {
            this.iTotalDisplayRecords = iTotalDisplayRecords;
        }

        public String getSEcho() {
            return sEcho;
        }

        public void setSEcho(String sEcho) {
            this.sEcho = sEcho;
        }

        public List<AaDataBean> getAaData() {
            return aaData;
        }

        public void setAaData(List<AaDataBean> aaData) {
            this.aaData = aaData;
        }

        public static class AaDataBean {
            /**
             * id : 402880e95e171ec1015e172d9c5b0004
             * orderId : 2c9ad8435dd0534e015e0ec146c80190
             * userId : f0cad24a5d1b9fe1015d1bb46f1b0030
             * name : null
             * phone : 18102340246
             * headerImg : 1bd0cc62-435d-46b4-8e39-dfe10920a244.png
             * anonymity : yes
             * userNickName : 李忠祥
             * grade : 2
             * createDate : 2017-08-25 10:16:59
             * comment : 送货快，五星好评！！！
             * evaluateType : express
             * evaluateType_value : 快递员
             * typeId : 2c9ad8435d40a6e5015d43d951d200b7
             * typeName : 黄杰
             * replycomment : null
             * replydate : null
             * replyStatus : no
             * tag1 : 0
             * tag2 : 0
             * tag3 : 0
             * tag4 : 0
             * tag5 : 0
             * tag6 : 0
             * tag7 : null
             * tag8 : null
             * tag9 : null
             * tag10 : null
             */

            private String id;
            private String orderId;
            private String userId;
            private Object name;
            private String phone;
            private String headerImg;
            private String anonymity;
            private String userNickName;
            private int grade;
            private String createDate;
            private String comment;
            private String evaluateType;
            private String evaluateType_value;
            private String typeId;
            private String typeName;
            private Object replycomment;
            private Object replydate;
            private String replyStatus;
            private String tag1;
            private String tag2;
            private String tag3;
            private String tag4;
            private String tag5;
            private String tag6;
            private Object tag7;
            private Object tag8;
            private Object tag9;
            private Object tag10;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getHeaderImg() {
                return headerImg;
            }

            public void setHeaderImg(String headerImg) {
                this.headerImg = headerImg;
            }

            public String getAnonymity() {
                return anonymity;
            }

            public void setAnonymity(String anonymity) {
                this.anonymity = anonymity;
            }

            public String getUserNickName() {
                return userNickName;
            }

            public void setUserNickName(String userNickName) {
                this.userNickName = userNickName;
            }

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getEvaluateType() {
                return evaluateType;
            }

            public void setEvaluateType(String evaluateType) {
                this.evaluateType = evaluateType;
            }

            public String getEvaluateType_value() {
                return evaluateType_value;
            }

            public void setEvaluateType_value(String evaluateType_value) {
                this.evaluateType_value = evaluateType_value;
            }

            public String getTypeId() {
                return typeId;
            }

            public void setTypeId(String typeId) {
                this.typeId = typeId;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public Object getReplycomment() {
                return replycomment;
            }

            public void setReplycomment(Object replycomment) {
                this.replycomment = replycomment;
            }

            public Object getReplydate() {
                return replydate;
            }

            public void setReplydate(Object replydate) {
                this.replydate = replydate;
            }

            public String getReplyStatus() {
                return replyStatus;
            }

            public void setReplyStatus(String replyStatus) {
                this.replyStatus = replyStatus;
            }

            public String getTag1() {
                return tag1;
            }

            public void setTag1(String tag1) {
                this.tag1 = tag1;
            }

            public String getTag2() {
                return tag2;
            }

            public void setTag2(String tag2) {
                this.tag2 = tag2;
            }

            public String getTag3() {
                return tag3;
            }

            public void setTag3(String tag3) {
                this.tag3 = tag3;
            }

            public String getTag4() {
                return tag4;
            }

            public void setTag4(String tag4) {
                this.tag4 = tag4;
            }

            public String getTag5() {
                return tag5;
            }

            public void setTag5(String tag5) {
                this.tag5 = tag5;
            }

            public String getTag6() {
                return tag6;
            }

            public void setTag6(String tag6) {
                this.tag6 = tag6;
            }

            public Object getTag7() {
                return tag7;
            }

            public void setTag7(Object tag7) {
                this.tag7 = tag7;
            }

            public Object getTag8() {
                return tag8;
            }

            public void setTag8(Object tag8) {
                this.tag8 = tag8;
            }

            public Object getTag9() {
                return tag9;
            }

            public void setTag9(Object tag9) {
                this.tag9 = tag9;
            }

            public Object getTag10() {
                return tag10;
            }

            public void setTag10(Object tag10) {
                this.tag10 = tag10;
            }
        }
    }
}
