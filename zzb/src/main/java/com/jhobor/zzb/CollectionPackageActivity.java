package com.jhobor.zzb;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhobor.zzb.base.BaseActivity;
import com.jhobor.zzb.fragments.CollectProductFragment;
import com.jhobor.zzb.fragments.CollectStoreFragment;
import com.jhobor.zzb.fragments.MyPrjFragment;

import java.util.ArrayList;
import java.util.List;

public class CollectionPackageActivity extends BaseActivity implements View.OnClickListener {
    ImageView back,help;
    TextView myPrj,product,store;

    int pos;
    List<TextView> textViewList = new ArrayList<>(3);
    List<View> viewList = new ArrayList<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_package);

        back = (ImageView) findViewById(R.id.back);
        help = (ImageView) findViewById(R.id.help);
        myPrj = (TextView) findViewById(R.id.myPrj);
        product = (TextView) findViewById(R.id.product);
        store = (TextView) findViewById(R.id.store);
        textViewList.add(myPrj);
        textViewList.add(product);
        textViewList.add(store);
        viewList.add(findViewById(R.id.myPrjCursor));
        viewList.add(findViewById(R.id.productCursor));
        viewList.add(findViewById(R.id.storeCursor));

        back.setOnClickListener(this);
        help.setOnClickListener(this);
        myPrj.setOnClickListener(this);
        product.setOnClickListener(this);
        store.setOnClickListener(this);
        MyPrjFragment myPrjFragment = new MyPrjFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content,myPrjFragment).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==help){

        }else if (v==myPrj){
            tab(0);
            MyPrjFragment myPrjFragment = new MyPrjFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content,myPrjFragment).commitAllowingStateLoss();
        }else if (v==product){
            tab(1);
            CollectProductFragment productFragment = new CollectProductFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content,productFragment).commitAllowingStateLoss();
        }else if (v==store){
            tab(2);
            CollectStoreFragment storeFragment = new CollectStoreFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content,storeFragment).commitAllowingStateLoss();
        }
    }

    public void tab(int newPos){
        if (newPos==pos&&pos!=0)
            return;
        int grayText = ContextCompat.getColor(getBaseContext(), R.color.grayText2);
        textViewList.get(pos).setTextColor(grayText);
        viewList.get(pos).setVisibility(View.INVISIBLE);

        int purple = ContextCompat.getColor(getBaseContext(), R.color.purple);
        textViewList.get(newPos).setTextColor(purple);
        viewList.get(newPos).setVisibility(View.VISIBLE);
        pos = newPos;
    }
}
