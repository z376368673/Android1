package com.jhobor.ddc.entity;

/**
 * Created by Administrator on 2017/3/3.
 * 分页加载数据参数类
 */

public class Pager {
    private int page;// 第几页
    private int op; // 0.刷新  1.加载
    private int pageLen;// 每页获取的条目数量
    private boolean noMoreData; // 是否没有数据了

    public Pager(int pageLen) {
        this.op = 1;
        this.pageLen = pageLen;
    }

    public Pager(int page, int op, int pageLen, boolean noMoreData) {
        this.page = page;
        this.op = op;
        this.pageLen = pageLen;
        this.noMoreData = noMoreData;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public int getPageLen() {
        return pageLen;
    }

    public void setPageLen(int pageLen) {
        this.pageLen = pageLen;
    }

    public boolean isNoMoreData() {
        return noMoreData;
    }

    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
    }
}
