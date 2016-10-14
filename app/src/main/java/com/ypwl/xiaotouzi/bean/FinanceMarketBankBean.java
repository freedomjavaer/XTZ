package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 金融超市--银行列表数据请求对象
 * <p>
 * Created by tengtao on 2016/4/14.
 */
public class FinanceMarketBankBean {
    private int status;
    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String id;
        private String name;
        private String image;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
