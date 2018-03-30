package com.jhobor.zzb.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhobor.zzb.PayForVipActivity;
import com.jhobor.zzb.R;
import com.jhobor.zzb.UpdateInfoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment implements View.OnClickListener {
    ImageView avatar,picture;
    TextView userName,level,expires, renewal;
    LinearLayout infoBox;

    private static final int REQUEST_CODE_UPDATE_INFO = 100;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        picture = (ImageView) view.findViewById(R.id.picture);
        userName = (TextView) view.findViewById(R.id.userName);
        level = (TextView) view.findViewById(R.id.level);
        expires = (TextView) view.findViewById(R.id.expires);
        renewal = (TextView) view.findViewById(R.id.renewal);
        infoBox = (LinearLayout) view.findViewById(R.id.infoBox);

        infoBox.setOnClickListener(this);
        renewal.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v==infoBox){
            startActivityForResult(new Intent(getContext(), UpdateInfoActivity.class),REQUEST_CODE_UPDATE_INFO);
        }else if (v==renewal){
            startActivity(new Intent(getContext(), PayForVipActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_UPDATE_INFO&&resultCode== Activity.RESULT_OK){

        }
    }
}
