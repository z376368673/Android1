package com.jhobor.ddc.entity;

import android.animation.TypeEvaluator;

/**
 * Created by Administrator on 2017/1/7.
 * 贝塞尔曲线公式，应用于 加入购物车 动画效果
 */

public class BezierEvaluator implements TypeEvaluator<Point> {
    private Point controllPoint;

    public BezierEvaluator(Point controllPoint) {
        this.controllPoint = controllPoint;
    }

    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        int x = (int) ((1 - fraction) * (1 - fraction) * startValue.x + 2 * fraction * (1 - fraction) * controllPoint.x + fraction * fraction * endValue.x);
        int y = (int) ((1 - fraction) * (1 - fraction) * startValue.y + 2 * fraction * (1 - fraction) * controllPoint.y + fraction * fraction * endValue.y);
        return new Point(x, y);
    }
}
