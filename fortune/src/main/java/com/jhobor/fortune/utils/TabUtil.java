package com.jhobor.fortune.utils;

import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/31.
 */

public class TabUtil {
    private int normalColor, activedColor;
    private TextView[][] textViews;
    private int pos;

    public TabUtil(int normalColor, int activedColor, TextView[][] textViews) {
        this.normalColor = normalColor;
        this.activedColor = activedColor;
        this.textViews = textViews;
    }

    public void change(int newPos) {
        if (newPos < textViews.length && newPos != pos) {
            for (TextView tv : textViews[pos]) {
                tv.setTextColor(normalColor);
            }
            for (TextView tv : textViews[newPos]) {
                tv.setTextColor(activedColor);
            }
            pos = newPos;
        }
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getActivedColor() {
        return activedColor;
    }

    public void setActivedColor(int activedColor) {
        this.activedColor = activedColor;
    }

    public TextView[][] getTextViews() {
        return textViews;
    }

    public void setTextViews(TextView[][] textViews) {
        this.textViews = textViews;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
