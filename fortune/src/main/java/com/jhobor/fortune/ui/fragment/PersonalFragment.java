package com.jhobor.fortune.ui.fragment;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.jhobor.fortune.entity.ShareProduct;
import com.jhobor.fortune.ui.LoginActivity;
import com.jhobor.fortune.R;
import com.jhobor.fortune.base.BaseApplication;
import com.jhobor.fortune.base.RetrofitCallback;
import com.jhobor.fortune.ui.ReportCenterActivity;
import com.jhobor.fortune.ui.ReportCenterApplyActivity;
import com.jhobor.fortune.ui.ReportCenterApplyingActivity;
import com.jhobor.fortune.ui.ShareProductActivity;
import com.jhobor.fortune.utils.ActionSheetDialog;
import com.jhobor.fortune.utils.BitmapUtlis;
import com.jhobor.fortune.ui.ConnectActivity;
import com.jhobor.fortune.ui.FankuiActivity;
import com.jhobor.fortune.ui.GridManagerActivity;
import com.jhobor.fortune.ui.MyAccountActivity;
import com.jhobor.fortune.ui.SettingUserActivity;
import com.jhobor.fortune.ui.ShareActivity;
import com.jhobor.fortune.utils.ErrorUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AAAAAAA";
    ImageView headImg, iv_isactivation;
    TextView userName, joinTime, mobile, refMobile, tv_isactivation, nu_pdb, nu_jhm;
    LinearLayout receiptWay, reportCenter, modifyPass, logout, about, pdb, jhm, help_record;
    View view;


    String strName, strMobile, strRegDate, strPhone, boodingCoin, activationCode;
    boolean hasData = false;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        headImg = (ImageView) view.findViewById(R.id.headImg);
        userName = (TextView) view.findViewById(R.id.userName);
        joinTime = (TextView) view.findViewById(R.id.joinTime);
        mobile = (TextView) view.findViewById(R.id.mobile);
        refMobile = (TextView) view.findViewById(R.id.refMobile);
        receiptWay = (LinearLayout) view.findViewById(R.id.receiptWay);
        reportCenter = (LinearLayout) view.findViewById(R.id.reportCenter);
        modifyPass = (LinearLayout) view.findViewById(R.id.modifyPass);
        logout = (LinearLayout) view.findViewById(R.id.logout);
        about = (LinearLayout) view.findViewById(R.id.about);
        pdb = (LinearLayout) view.findViewById(R.id.pdb);//排单币
        jhm = (LinearLayout) view.findViewById(R.id.jhm);//激活码
        help_record = (LinearLayout) view.findViewById(R.id.help_record);//帮助记录
        iv_isactivation = (ImageView) view.findViewById(R.id.iv_isactivation);
        tv_isactivation = (TextView) view.findViewById(R.id.tv_isactivation);
        nu_pdb = (TextView) view.findViewById(R.id.nu_pdb);
        nu_jhm = (TextView) view.findViewById(R.id.nu_jhm);

        receiptWay.setOnClickListener(this);
        reportCenter.setOnClickListener(this);
        modifyPass.setOnClickListener(this);
        logout.setOnClickListener(this);
        about.setOnClickListener(this);
        pdb.setOnClickListener(this);
        jhm.setOnClickListener(this);
        help_record.setOnClickListener(this);
        iv_isactivation.setOnClickListener(this);
        headImg.setOnClickListener(this);

        if (hasData) {
            setData();
        } else {
            getData();
        }

        int isActivation_nu = BaseApplication.prefs.getInt("isActivation_nu", 1);
        if (0 == isActivation_nu) {
            iv_isactivation.setImageResource(R.mipmap.wjh);
            tv_isactivation.setText("(未激活)");
        } else {
            iv_isactivation.setImageResource(R.mipmap.jh);
            tv_isactivation.setVisibility(View.GONE);
        }

        return view;
    }

    private void getData() {
//        String token = (String) BaseApplication.dataMap.get("token");
//        BaseApplication.iService.mine(token).enqueue(new RetrofitCallback(getContext(), new RetrofitCallback.DataParser() {
//            @Override
//            public void parse(String data) {
//                try {
//                    JSONObject jsonObject = JSON.parseObject(data);
//                    int isLogin = jsonObject.getIntValue("isLogin");
//                    if (isLogin == 1) {
//                        strName = jsonObject.getString("name");
//                        strMobile = jsonObject.getString("mobile");
//                        strRegDate = jsonObject.getString("regDate");
//                        boodingCoin = jsonObject.getIntValue("boodingCoin") + "";
//                        activationCode = jsonObject.getIntValue("activationCode") + "";
//                        boolean hasPhone = jsonObject.getBooleanValue("phone");
//                        if (hasPhone) {
//                            strPhone = jsonObject.getString("phone");
//                        }
//                        setData();
//                        hasData = true;
//                    } else {
//                        Toast.makeText(getContext(), "账户信息失效，无法获取数据", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    ErrorUtil.retrofitResponseParseFail(getContext(), e);
//                }
//            }
//        }));
    }

    private void setData() {
        //如果没有token 则取登陆界面，并且销毁此界面
        String token = BaseApplication.prefs.getString("token", "");
        if (TextUtils.isEmpty(token)) {
            getActivity().finish();
        } else {
            userName.setText(strName);
            String imgUrl = BaseApplication.prefs.getString("imgUrl", "");
            Glide.with(getContext()).load(imgUrl).error(R.mipmap.head_img).into(headImg);
            joinTime.setText("加入日期" + strRegDate);
            //mobile.setText(String.format("手机号码：%s", strMobile));
            String string = BaseApplication.prefs.getString("phone", "null");
            mobile.setText(String.format("通众会员：%s", string));
            refMobile.setText(String.format("推荐号码：%s", strPhone));
            nu_pdb.setText(boodingCoin);
            nu_jhm.setText(activationCode);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == receiptWay) {
            //账户信息 （添加银行卡的）
            //startActivity(new Intent(getContext(), ReceiptWayActivity.class));
            startActivity(new Intent(getContext(), MyAccountActivity.class));
        } else if (v == pdb) {
            //租车积分管理页面
            startActivity(new Intent(getActivity(), GridManagerActivity.class));
            //startActivity(new Intent(getActivity(), BoodingCoinActivity.class));
        } else if (v == reportCenter) {
            //服务中心
            //startActivity(new Intent(getContext(), ReportCenterApplyActivity.class));
            startActivity(new Intent(getContext(), ReportCenterApplyingActivity.class));
        } else if (v == jhm) {
            //用户反馈
           /* Intent intent = new Intent(getActivity(), JiHuomaActivity.class);
            intent.putExtra("jhm", Integer.parseInt(activationCode));*/
            Intent intent = new Intent(getActivity(), FankuiActivity.class);
            startActivity(intent);
            // startActivity(new Intent(getActivity(), TradeRecordHelpActivity.class));
        } else if (v == help_record) {
            //联系我们
            //startActivity(new Intent(getActivity(), TradeRecordHelpActivity.class));
            startActivity(new Intent(getActivity(), ConnectActivity.class));
        }
        else if (v == modifyPass) { //设置
            // TODO: 2018/3/27
            //startActivity(new Intent(getContext(), ModifyPassActivity.class));
            startActivity(new Intent(getContext(), SettingUserActivity.class));
        } else if (v == about) {//分享
            //startActivity(new Intent(getContext(), BrowserActivity.class));
            startActivity(new Intent(getContext(), ShareProductActivity.class));
        }  else if (v == logout) {
            new BottomDialog.Builder(getContext())
                    .setTitle("退出")
                    .setContent("确定要退出登录吗？")
                    .setNegativeText("取消")
                    .setNegativeTextColorResource(R.color.green)
                    .setPositiveText("退出")
                    .setPositiveBackgroundColorResource(R.color.red)
                    .onPositive(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull BottomDialog bottomDialog) {
                            BaseApplication.prefs.edit().remove("token").remove("m").remove("p").remove("isActivation_nu").apply();
                            Toast.makeText(getContext(), "已退出登录", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                    })
                    .show();
        } else if (v == headImg) {
            //换头像
            checkP();
        }
    }

    private void checkP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.INTERNET
                        }, 1);
            } else {
                changedOption();
            }
        }
    }

    private void changedOption() {
        ActionSheetDialog mDialog = new ActionSheetDialog(getActivity()).builder();
        mDialog.setTitle("选择");
        mDialog.setCancelable(false);
        mDialog.addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                choseImg();
            }
        }).show();
    }

    String imgPath = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            List<Uri> imgs = Matisse.obtainResult(data);
            imgPath = getRealFilePath(getActivity(), imgs.get(0));
            imgPath = new BitmapUtlis().compressImageByPath(imgPath);
            Log.e("onActivityResult", "imgPath = " + imgPath);
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            String bit = convertIconToString(bitmap);
            upDataHeadImg();
        }
    }

    /**
     * 选择图片 先检查是否有相应的权限
     */
    private void choseImg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                Matisse.from(this)
                        .choose(MimeType.allOf())
                        .theme(R.style.Matisse_Dracula)//内置的主题
                        .countable(false)//使用 countable(true) 来显示一个从 1 开始的数字
                        .maxSelectable(1)//限制可选择的最大数目
                        .imageEngine(new GlideEngine())
                        .forResult(1000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choseImg();
            } else {
                Toast.makeText(getActivity(), "权限已拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        setData();
    }

    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void upDataHeadImg() {
        if (TextUtils.isEmpty(imgPath)) {
            return;
        }
        String token = BaseApplication.prefs.getString("token", "");
        File file = new File(imgPath);
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uuid", token)
                .addFormDataPart("goodsPhoto", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        List<MultipartBody.Part> parts = build.parts();
        BaseApplication.iService.uploadheadImg(parts).enqueue(new RetrofitCallback(getActivity(), new RetrofitCallback.DataParser() {
            @Override
            public void parse(String data) {
                com.alibaba.fastjson.JSONObject jsObj = JSON.parseObject(data);
                int msg = jsObj.getIntValue("msg");
                if (msg == 1) {
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    Glide.with(getActivity())
                            .load(new File(imgPath))
                            .into(headImg);
                } else {
                    Toast.makeText(getActivity(), jsObj.getString("errorInfo"), Toast.LENGTH_SHORT).show();
                }
            }
        }));

    }

}
