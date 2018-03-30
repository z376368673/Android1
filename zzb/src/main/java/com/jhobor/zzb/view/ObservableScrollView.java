package com.jhobor.zzb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ObservableScrollView extends ScrollView {

    private OnScrollChangedListener mOnScrollChangedListener;
    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(t);
        }
    }

    public interface OnScrollChangedListener {
        public void onScrollChanged(int top);
    }

    public void setOnScrollChangeListener(OnScrollChangedListener l) {
        mOnScrollChangedListener = l;
    }
}
