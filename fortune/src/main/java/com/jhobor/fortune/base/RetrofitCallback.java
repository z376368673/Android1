package com.jhobor.fortune.base;

import android.content.Context;
import android.util.Log;

import com.jhobor.fortune.utils.ErrorUtil;

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
            parser.parse("{ error :"+response.raw().toString()+" }");
            Log.i(">>", response.raw().toString());
            //Toast.makeText(context, "获取的数据为：null", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String data = response.body().string();
            Log.i(">>", data);
            parser.parse(data);
        } catch (IOException e) {
            Log.i(">>", e.getMessage());
            parser.parse("{ IOException :"+e.getMessage()+" }");
            ErrorUtil.retrofitResponseParseFail(context, e);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.i(">>", t.getMessage());
        ErrorUtil.retrofitGetDataFail(context, t);
    }

    public interface DataParser {
        void parse(String data);
    }
}
