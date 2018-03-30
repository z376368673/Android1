package com.jhobor.ddc.base;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jhobor.ddc.utils.ErrorUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/25.
 */

public class RetrofitCallback implements Callback<ResponseBody> {
    private Context context;
    private DataParser parser;

    public RetrofitCallback(Context context, DataParser parser) {
        this.context = context;
        this.parser = parser;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.body() == null) {
            Toast.makeText(context, "获取的数据为：null", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String data = response.body().string();
            Log.i(">>", data);
            parser.parse(data);
        } catch (IOException e) {
            ErrorUtil.retrofitResponseParseFail(context, e);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        ErrorUtil.retrofitGetDataFail(context, t);
    }

    public interface DataParser {
        void parse(String data);
    }
}
