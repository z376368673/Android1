package com.huazong.app.huazong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huazong.app.huazong.base.BarUtil;
import com.huazong.app.huazong.base.BaseApplication;
import com.huazong.app.huazong.base.RetrofitCallback;
import com.huazong.app.huazong.entity.Friend;
import com.huazong.app.huazong.utils.ErrorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class FriendsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int ADD_FRIEND = 100;
    ListView allFriendsView;
    ImageView addFriend;

    AllFriendsAdapter adapter;
    private List<Friend> allFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friends);

        allFriendsView = (ListView) findViewById(R.id.allFriendsView);
        addFriend = (ImageView) findViewById(R.id.addFriend);

        addFriend.setOnClickListener(this);
        Intent intent = getIntent();
        BarUtil.topBar(this,intent.getStringExtra("title"));

        getData();
    }

    private void getData() {
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.friendList(openid,arrowOrGun).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    //每关最高分用户信息
                    JSONArray data = jsonObject.getJSONArray("data");
                    //关注好友openid，json数组，将会解析到后面的集合中
                    JSONArray friendList = jsonObject.getJSONArray("friendList");
                    //关注好友的信息
                    JSONArray followList = jsonObject.getJSONArray("FollowList");
                    int dataLength = data.length();
                    int friendListLength = friendList.length();
                    //关注好友的openid，集合
                    List<String> friendIds = new ArrayList<>(friendListLength>0?friendListLength:1);
                    for (int i = 0; i < friendListLength; i++) {
                        String openid = friendList.getString(i);
                        friendIds.add(openid);
                    }
                    allFriends = new LinkedList<>();
                    for (int j = 0; j < dataLength; j++) {
                        JSONArray jsonArray = data.getJSONArray(j);
                        String openId = jsonArray.getString(0);
                        int userId = jsonArray.getInt(1);
                        String name = jsonArray.getString(2);
                        String picture = jsonArray.getString(3);
                        boolean check = false;
                        for (int k = 0; k < friendIds.size(); k++) {
                            if (openId.equals(friendIds.get(k))) {
                                check = true;
                                //openid匹配，说明好友已在最高分里面，移除该openid，减少后面的循环次数
                                friendIds.remove(k);
                                break;
                            }
                        }
                        Friend friend = new Friend(userId, openId, picture, name, check);
                        if (!allFriends.contains(friend)) {
                            allFriends.add(friend);
                        }
                    }
                    for (int m = 0; m < followList.length(); m++) {
                        JSONObject follow = followList.getJSONObject(m);
                        String openid = follow.getString("openId");
                        //剩余的openid就是不在最高分里面的，这些也是好友信息
                        for (int n = 0; n < friendIds.size(); n++) {
                            if (friendIds.get(n).equals(openid)) {
                                int id = follow.getInt("id");
                                String nickname = follow.getString("nickname");
                                String headImgUrl = follow.getString("headImgUrl");
                                Friend friend = new Friend(id, openid, headImgUrl, nickname, true);
                                if (!allFriends.contains(friend)) {
                                    allFriends.add(friend);
                                }
                                break;
                            }
                        }
                    }
                    adapter = new AllFriendsAdapter(FriendsActivity.this, R.layout.item_a_friend, allFriends);
                    allFriendsView.setAdapter(adapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (view == addFriend) {
            startActivityForResult(new Intent(this, AddFriendActivity.class), ADD_FRIEND);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_FRIEND) {
            getData();
        }
    }

    private class AllFriendsAdapter extends ArrayAdapter<Friend> {
        LayoutInflater inflater;
        int resource;

        AllFriendsAdapter(Context context, int resource, List<Friend> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, parent, false);
                holder.friendIcon = (ImageView) convertView.findViewById(R.id.friendIcon);
                holder.friendName = (TextView) convertView.findViewById(R.id.friendName);
                holder.care = (CheckBox) convertView.findViewById(R.id.care);
                holder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean b = ((CheckBox) view).isChecked();
                        if (allFriends.get(position).isChecked()!=b) {
                            careFriend(position, b ? 1 : 0);
                        }

                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Friend item = getItem(position);
            Glide.with(getContext())
                    .load(item.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(BaseApplication.GLIDE_ERROR)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(holder.friendIcon);
            holder.friendName.setText(item.getName());
            holder.care.setChecked(item.isChecked());

            return convertView;
        }

        class ViewHolder {
            ImageView friendIcon;
            TextView friendName;
            CheckBox care;
        }
    }

    private void careFriend(final int pos, final int checked) {
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.addFriend(openid,allFriends.get(pos).getUserId(),arrowOrGun,checked).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                if (data.equals("success")) {
                    if (checked == 1) {
                        Toast.makeText(getBaseContext(),"关注成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getBaseContext(),"已取消关注",Toast.LENGTH_SHORT).show();
                    }
                    allFriends.get(pos).setChecked(checked == 1);
                    adapter.notifyDataSetChanged();
                }
            }
        }));
    }
}
