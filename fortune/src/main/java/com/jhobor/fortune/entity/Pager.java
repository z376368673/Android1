package com.jhobor.fortune.entity;

/**
 * Created by Administrator on 2017/3/3.
 * 分页加载数据参数类
 */

public class Pager {
    public static final int MODE_REFRESH = 0;
    public static final int MODE_LOAD_MORE = 1;
    private int page;// 第几页
    private int mode; // 0.刷新  1.加载
    private int pageCount;// 每页获取的条目数量
    private boolean noMoreData; // 是否没有数据了
    private boolean isLoading;

    public Pager(int pageCount) {
        this.mode = MODE_LOAD_MORE;
        this.pageCount = pageCount;
    }

    public Pager(int page, int mode, int pageLen, boolean noMoreData) {
        this.page = page;
        this.mode = mode;
        this.pageCount = pageLen;
        this.noMoreData = noMoreData;
    }

    public void next() {
        ++this.page;
    }

    public void prev() {
        if (this.page > 0) {
            --this.page;
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public boolean isNoMoreData() {
        return noMoreData;
    }

    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
