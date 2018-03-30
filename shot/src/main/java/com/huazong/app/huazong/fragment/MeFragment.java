package com.huazong.app.huazong.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.AboutActivity;
import com.huazong.app.huazong.EquipmentActivity;
import com.huazong.app.huazong.FriendsActivity;
import com.huazong.app.huazong.MessageActivity;
import com.huazong.app.huazong.OrdersActivity;
import com.huazong.app.huazong.R;
import com.huazong.app.huazong.RankActivity;
import com.huazong.app.huazong.ShareActivity;
import com.huazong.app.huazong.TrainingRecordActivity;
import com.huazong.app.huazong.WalletActivity;
import com.huazong.app.huazong.adapter.MeArrayAdapter;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.MyInfo;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MeFragment extends Fragment implements AdapterView.OnItemClickListener {
    TextView userName, userLevel, userId, score;
    ListView listView;
    View mView;
    ImageView userIcon, userIconBorder;

    MyInfo myInfo;
    String[] itemTitle = new String[]{"训练记录", "排行榜", "钱包", "好友", "装备", "订单", "分享", "关于", "推送消息"};
    int[] itemIcons = new int[]{
        R.mipmap.me_item_records,
                R.mipmap.me_item_rank,
                R.mipmap.me_item_wallet,
                R.mipmap.me_item_friends,
                R.mipmap.me_item_equipment,
                R.mipmap.me_item_trade,
                R.mipmap.me_item_share,
                R.mipmap.me_item_about,
                R.mipmap.me_item_msg
    };

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_me, container, false);
        userName = (TextView) mView.findViewById(R.id.userName);
        userLevel = (TextView) mView.findViewById(R.id.userLevel);
        userId = (TextView) mView.findViewById(R.id.userId);
        score = (TextView) mView.findViewById(R.id.score);
        listView = (ListView) mView.findViewById(R.id.listView);
        userIcon = (ImageView) mView.findViewById(R.id.userIcon);
        userIconBorder = (ImageView) mView.findViewById(R.id.userIconBorder);

        MeArrayAdapter adapter = new MeArrayAdapter(getContext(), R.layout.item_me, itemTitle, itemIcons);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if (myInfo==null) {
            getData();
        }else {
            showData();
        }

        return mView;
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.myInfo(openid,arrowOrGun).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String headImgUrl = jsonObject.getString("headImgUrl");
                    String nickName = jsonObject.getString("nickname");
                    String rankName = jsonObject.getString("level");
                    int iscore = jsonObject.getInt("score");
                    int uid = jsonObject.getInt("userId");
                    myInfo = new MyInfo(headImgUrl, nickName, iscore, rankName, uid);
                    BaseApplication.dataMap.put("userId",uid);
                    showData();
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getContext(),e);
                }
            }
        }));
    }

    private void showData() {
        Glide.with(getContext())
                .load(myInfo.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(BaseApplication.GLIDE_ERROR)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(userIcon);
        userName.setText(myInfo.getName());
        String rankStr = "等级：" + myInfo.getRankName();
        userLevel.setText(rankStr);
        String userIdStr = "ID：" + (myInfo.getUserId() > 0 ? myInfo.getUserId() : 0);
        userId.setText(userIdStr);
        score.setText(String.valueOf(myInfo.getScore()));
        userIconBorder.setVisibility(View.VISIBLE);
    }

    void jumpTo(Class<?> cls, int position) {
        Intent intent = new Intent(getContext(), cls);
        intent.putExtra("title", itemTitle[position]);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                jumpTo(TrainingRecordActivity.class, position);
                break;
            case 1:
                jumpTo(RankActivity.class, position);
                break;
            case 2:
                jumpTo(WalletActivity.class, position);
                break;
            case 3:
                jumpTo(FriendsActivity.class, position);
                break;
            case 4:
                jumpTo(EquipmentActivity.class, position);
                break;
            case 5:
                jumpTo(OrdersActivity.class, position);
                break;
            case 6:
                jumpTo(ShareActivity.class, position);
                break;
            case 7:
                jumpTo(AboutActivity.class, position);
                break;
            case 8:
                jumpTo(MessageActivity.class, position);
                break;
        }
    }
}
