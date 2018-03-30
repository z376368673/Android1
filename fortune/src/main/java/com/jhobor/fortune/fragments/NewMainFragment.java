package com.jhobor.fortune.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author     Qijing
 * Created by YQJ on 2018/3/24.
 * Description:
 */

public class NewMainFragment extends Fragment {

    private View mView;
    private TextView mGuquan;
    private TextView mFenhong;
    private TextView mTotal_fee;
    private TextView mFen_new;
    private TextView mMost_high;
    private TextView mMain_content;
    private TextView mContent_date;
    private double mCapital;
    private double mNewsRevenue;
    private double mDividend;
    private double mTotalRevenue;
    private double mMaxRevenue;
    private String mContent;
    private String mCreateDate;
    private int mId;
    private int mStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_main, container, false);
        mGuquan = (TextView) mView.findViewById(R.id.guquan);
        mFenhong = (TextView) mView.findViewById(R.id.fenhong);
        mTotal_fee = (TextView) mView.findViewById(R.id.total_fee);
        mFen_new = (TextView) mView.findViewById(R.id.fen_new);
        mMost_high = (TextView) mView.findViewById(R.id.most_high);
        mMain_content = (TextView) mView.findViewById(R.id.main_content);
        mContent_date = (TextView) mView.findViewById(R.id.content_date);

        return mView;
    }


    private void getData() {
        String uuid = (String) BaseApplication.dataMap.get("token");
        BaseApplication.iService.getMain(uuid).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int msg = jsonObject.getInt("msg");
                    JSONObject news = jsonObject.getJSONObject("news");
                    mCapital = jsonObject.getDouble("capital");
                    mNewsRevenue = jsonObject.getDouble("newsRevenue");
                    mDividend = jsonObject.getDouble("dividend");
                    mTotalRevenue = jsonObject.getDouble("totalRevenue");
                    mMaxRevenue = jsonObject.getDouble("maxRevenue");

                    mContent = news.getString("content");
                    mCreateDate = news.getString("createDate");
                    mId = news.getInt("id");
                    mStatus = news.getInt("status");

                    mGuquan.setText(mCapital + "");
                    mTotal_fee.setText(mTotalRevenue + "");
                    mMain_content.setText("    " + mContent);
                    mContent_date.setText(mCreateDate);
                    mFen_new.setText(mNewsRevenue + "");
                    mMost_high.setText(mMaxRevenue + "");
                    mFenhong.setText(mDividend + "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();

    }


}
