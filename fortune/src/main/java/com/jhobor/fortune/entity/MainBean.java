package com.jhobor.fortune.entity;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/29.
 * Description:
 */

public class MainBean {
    /**
     * news : {"id":1,"content":"新闻","createDate":"2018-3-27 18：30：00","status":0}
     * msg : 1
     * capital : 10000.0
     * newsRevenue : 0.0
     * dividend : 10.0
     * totalRevenue : 0.0
     * maxRevenue : 0.0
     */

    private NewsBean news;
    private int msg;
    private double capital;
    private double newsRevenue;
    private double dividend;
    private double totalRevenue;
    private double maxRevenue;

    public NewsBean getNews() {
        return news;
    }

    public void setNews(NewsBean news) {
        this.news = news;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getNewsRevenue() {
        return newsRevenue;
    }

    public void setNewsRevenue(double newsRevenue) {
        this.newsRevenue = newsRevenue;
    }

    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getMaxRevenue() {
        return maxRevenue;
    }

    public void setMaxRevenue(double maxRevenue) {
        this.maxRevenue = maxRevenue;
    }

    public static class NewsBean {
        /**
         * id : 1
         * content : 新闻
         * createDate : 2018-3-27 18：30：00
         * status : 0
         */

        private int id;
        private String content;
        private String createDate;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
