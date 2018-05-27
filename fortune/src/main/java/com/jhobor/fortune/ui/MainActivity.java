package com.jhobor.fortune.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jhobor.fortune.R;
import com.jhobor.fortune.ui.fragment.NewMainFragment;
import com.jhobor.fortune.ui.fragment.PersonalFragment;
import com.jhobor.fortune.ui.fragment.TeamFragment;
import com.jhobor.fortune.utils.DisplayUtil;
import com.jhobor.fortune.utils.HideIMEUtil;


import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {
    PageBottomTabLayout bottomTab;

    //List<Fragment> fragmentList = new ArrayList<>(4);
    List<Fragment> fragmentList = new ArrayList<>(3);
    long millis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideIMEUtil.wrap(this);

        bottomTab = (PageBottomTabLayout) findViewById(R.id.bottomTab);
        int normalColor = ContextCompat.getColor(this, R.color.tabNormal);
        int activedColor = ContextCompat.getColor(this, R.color.tabActived);
        NormalItemView fortune = new NormalItemView(this);
        fortune.initialize(R.mipmap.home, R.mipmap.home_actived, "首页");
        fortune.setTextDefaultColor(normalColor);
        fortune.setTextCheckedColor(activedColor);
        NormalItemView team = new NormalItemView(this);
        team.initialize(R.mipmap.team, R.mipmap.team_actived, "团队");
        team.setTextDefaultColor(normalColor);
        team.setTextCheckedColor(activedColor);
        NormalItemView wallets = new NormalItemView(this);
        wallets.initialize(R.mipmap.wallets, R.mipmap.wallets_actived, "钱包");
        wallets.setTextDefaultColor(normalColor);
        wallets.setTextCheckedColor(activedColor);
        NormalItemView personal = new NormalItemView(this);
        personal.initialize(R.mipmap.personal, R.mipmap.personal_actived, "个人");
        personal.setTextDefaultColor(normalColor);
        personal.setTextCheckedColor(activedColor);
        NavigationController controller = bottomTab.custom()
                .addItem(fortune)
                .addItem(team)
                //.addItem(wallets)
                .addItem(personal)
                .build();

        fragmentList.add(new NewMainFragment());
        fragmentList.add(new TeamFragment());
        //fragmentList.add(new WalletsFragment());
        fragmentList.add(new PersonalFragment());
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragmentList.get(0)).commitAllowingStateLoss();
        controller.addTabItemSelectedListener(this);
        int[] pxs = DisplayUtil.getScreenRect(this);

        /*关闭悬浮球
        FloatBall floatBall = new FloatBall.Builder(this, (ViewGroup) findViewById(R.id.rootView))
                .setBottomMargin(pxs[1] - 500)//悬浮球初始位置BottomMargin
                .setRightMargin(0)//悬浮球初始位置RightMargin
                .setHeight(120)//悬浮球高度
                .setWidth(120)//悬浮球宽度
                .setRes(R.mipmap.finance_icon)//图片资源
                .setDuration(300)//靠边动画时间
                .setOnClickListener(new View.OnClickListener() {//悬浮球点击事件
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), ManagePropertyActivity.class));
                    }
                })
                .setBall(new ImageView(this))//默认悬浮球是View，如需设置特定View则设置此项，反则无需设置
                .build();*/
    }

    @Override
    public void onSelected(int index, int old) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragmentList.get(index)).commitAllowingStateLoss();
    }

    @Override
    public void onRepeat(int index) {

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - millis > 800) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            millis = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
