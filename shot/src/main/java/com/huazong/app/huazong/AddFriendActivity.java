package com.huazong.app.huazong;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class AddFriendActivity extends AppCompatActivity implements OnClickListener {
    EditText friendId;
    Button ok;
    TextView topTitle;
    FrameLayout back;
    ListView searchResult;

    private List<Friend> allFriends;
    private SearchResultArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_friend);

        back = (FrameLayout) findViewById(R.id.back);
        topTitle = (TextView) findViewById(R.id.topTitle);
        friendId = (EditText) findViewById(R.id.friendId);
        ok = (Button) findViewById(R.id.ok);
        searchResult = (ListView) findViewById(R.id.searchResult);

        back.setOnClickListener(this);
        BarUtil.topBar(this,"添加好友");
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ok) {
            getData();
        }
    }

    private void getData() {
        String friendIdText = friendId.getText().toString();
        if (friendIdText.isEmpty()){
            Toast.makeText(getBaseContext(),"ID不可为空",Toast.LENGTH_SHORT).show();
            return;
        }
        int fId = Integer.parseInt(friendIdText);
        String openid = (String) BaseApplication.dataMap.get("openid");
        int arrowOrGun = (int) BaseApplication.dataMap.get("arrowOrGun");
        BaseApplication.iService.searchFriend(openid,arrowOrGun,fId).enqueue(new RetrofitCallback(getBaseContext(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                try {
                    if (data.isEmpty()){
                        Toast.makeText(getBaseContext(),"该ID的用户不存在",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(data);
                    String picture = jsonObject.getString("picture");
                    String nickName = jsonObject.getString("nickName");
                    int userId = jsonObject.getInt("userId");
                    int score = jsonObject.getInt("score");
                    int pass = jsonObject.getInt("pass");
                    boolean check = jsonObject.getString("friend").equals("ok");
                    Friend friend = new Friend(userId, picture, nickName, score, pass, check);
                    allFriends = new ArrayList<>(1);
                    allFriends.add(friend);
                    adapter = new SearchResultArrayAdapter(getBaseContext(), R.layout.item_add_friend, allFriends);
                    searchResult.setAdapter(adapter);
                } catch (JSONException e) {
                    ErrorUtil.retrofitResponseParseFail(getBaseContext(),e);
                }
            }
        }));
    }

    @Override
    protected void onDestroy() {
        setResult(RESULT_OK);
        super.onDestroy();
    }

    private class SearchResultArrayAdapter extends ArrayAdapter<Friend> {
        int resource;
        LayoutInflater inflater;

        SearchResultArrayAdapter(Context context, int resource, List<Friend> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, parent, false);
                holder.userId = (TextView) convertView.findViewById(R.id.userId);
                holder.userName = (TextView) convertView.findViewById(R.id.userName);
                holder.score = (TextView) convertView.findViewById(R.id.score);
                holder.depth = (TextView) convertView.findViewById(R.id.depth);
                holder.userIcon = (ImageView) convertView.findViewById(R.id.userIcon);
                holder.check = (CheckBox) convertView.findViewById(R.id.checkBox);
                holder.check.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean b = ((CheckBox) view).isChecked();
                        careFriend(position,b ? 1 : 0);
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Friend item = getItem(position);
            Glide.with(getContext())
                    .load(item.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .error(BaseApplication.GLIDE_ERROR)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(holder.userIcon);
            holder.userId.setText(String.valueOf(item.getUserId()));
            holder.userName.setText(item.getName());
            holder.score.setText(String.valueOf(item.getScore()));
            holder.depth.setText(String.valueOf(item.getDepth()));
            holder.check.setChecked(item.isChecked());

            return convertView;
        }

        class ViewHolder {
            TextView userId, userName, score, depth;
            ImageView userIcon;
            CheckBox check;
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
