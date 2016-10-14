package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 金融超市---信用卡列表数据对象
 * <p>
 * Created by tengtao on 2016/4/14.
 */
public class FinanceMarketCreditCardBean {

    /**
     * list : [{"bank":"中信银行","bid":"1","description":"IMAX五折观影","id":"1","image":"up_files/bank/ecitic.png","name":"中信i白金信用卡","url":"https://creditcard.ecitic.com/citiccard/cardishop/jsp/index.jsp?sid=WHSQK&pid=CS0083"}]
     * ret_msg :
     * status : 0
     */

    private String ret_msg;
    private int status;
    /**
     * bank : 中信银行
     * bid : 1
     * description : IMAX五折观影
     * id : 1
     * image : up_files/bank/ecitic.png
     * name : 中信i白金信用卡
     * url : https://creditcard.ecitic.com/citiccard/cardishop/jsp/index.jsp?sid=WHSQK&pid=CS0083
     */

    private List<ListBean> list;

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String bank;
        private String bid;
        private String description;
        private String id;
        private String image;
        private String name;
        private String url;

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
