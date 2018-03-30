package com.huazong.app.huazong.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface IService {

    /**
     * 获取广告图片
     * @return
     */
    @POST("picture_list.action")
    Call<ResponseBody> getAd();

    /**
     * 提交数据登录
     * @param jsonData
     * @param userInfo
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("userInfo_addUI.action")
    Call<ResponseBody> login(@Field("jsonData") String jsonData, @Field("userInfo")String userInfo, @Field("type")String type);

    /**
     * 获取首页中射箭和射击的关数（总关数、已玩关数）
     * @param openid
     * @return
     */
    @POST("ranklist_myPass.action")
    Call<ResponseBody> getHomeData(@Query("openid") String openid);

    /**
     * 获取订单列表
     * @param openid
     * @param type
     * @return
     */
    @POST("orders_list.action")
    Call<ResponseBody> orderList(@Query("openid") String openid, @Query("type")int type);

    /**
     * 店铺列表
     * @return
     */
    @POST("store_list.action")
    Call<ResponseBody> storeList();

    /**
     * 获取某个店铺某天的下单页面数据
     * @param storeId
     * @param date
     * @return
     */
    @POST("rented_list.action")
    Call<ResponseBody> storeDetails(@Query("storeId") int storeId, @Query("date")String date);

    /**
     * 提交购买信息，获得支付宝需要的支付信息
     * @param params
     * @return
     */
    @POST("rented_rentedQuery.action")
    Call<ResponseBody> payWithAli(@Query("params") String params);

    /**
     * 取消订单
     * @param out_trade_no
     * @return
     */
    @POST("orders_orderFailure.action")
    Call<ResponseBody> cancelOrder(@Query("out_trade_no") String out_trade_no);

    /**
     * 提交购买信息，获得微信需要的支付信息
     * @param params
     * @return
     */
    @POST("rented_weChatPay.action")
    Call<ResponseBody> payWithWechat(@Query("params") String params);

    /**
     * 提交购买信息，并返回用电子钱包的支付结果
     * @param params
     * @return
     */
    @POST("rented_memberPay.action")
    Call<ResponseBody> payWithWallet(@Query("params") String params);

    /**
     * 刚购买订单的数据
     * @param openid
     * @param type
     * @param orderNo
     * @return
     */
    @POST("orders_myList.action")
    Call<ResponseBody> justBuyOrderList(@Query("openid") String openid, @Query("type")int type, @Query("orderNo")String orderNo);

    /**
     * 个人信息
     * @param openid
     * @param type
     * @return
     */
    @POST("ranklist_myScore.action")
    Call<ResponseBody> myInfo(@Query("openid") String openid, @Query("type")int type);

    /**
     * 训练记录
     * @param openid
     * @param type
     * @return
     */
    @POST("record_list.action")
    Call<ResponseBody> trainRecordList(@Query("openid") String openid, @Query("type")int type);

    /**
     * 排行榜
     * @param openid
     * @param type
     * @return
     */
    @POST("ranklist_scoreRank.action")
    Call<ResponseBody> rankList(@Query("openid") String openid, @Query("type")int type);

    /**
     * 查询余额
     * @param openid
     * @return
     */
    @POST("fund_checkMoney.action")
    Call<ResponseBody> getBalance(@Query("openid") String openid);

    /**
     * 用微信支付充值
     * @param openid
     * @param totalfee
     * @param ip
     * @return
     */
    @POST("fund_weChatFundPay.action")
    Call<ResponseBody> rechargeWithWechat(@Query("openid") String openid,@Query("totalfee") int totalfee,@Query("ip")String ip);

    /**
     * 用支付宝充值
     * @param openid
     * @param money
     * @return
     */
    @POST("fund_recharge.action")
    Call<ResponseBody> rechargeWithAli(@Query("openid") String openid, @Query("money") int money);

    /**
     * 好友列表
     * @param openid
     * @param type
     * @return
     */
    @POST("record_getRecordScore.action")
    Call<ResponseBody> friendList(@Query("openid") String openid, @Query("type")int type);

    /**
     * 关注或取关好友
     * @param openid
     * @param type
     * @param check
     * @return
     */
    @POST("friend_addFriend.action")
    Call<ResponseBody> addFriend(@Query("openid") String openid, @Query("userId")int userId, @Query("type")int type, @Query("check")int check);

    /**
     * 查找好友
     * @param openid
     * @param type
     * @param friendId
     * @return
     */
    @POST("friend_queryFriend.action")
    Call<ResponseBody> searchFriend(@Query("openid") String openid, @Query("type")int type, @Query("friendId")int friendId);

    /**
     * 我的装备
     * @param openid
     * @return
     */
    @POST("outfit_list.action")
    Call<ResponseBody> myEquipment(@Query("openid") String openid);

    /**
     * 获取分享图片
     * @param openid
     * @param type
     * @return
     */
    @POST("record_share.action")
    Call<ResponseBody> sharePhoto(@Query("openid") String openid, @Query("type")int type);

    /**
     * 关于信息
     * @return
     */
    @POST("about_list.action")
    Call<ResponseBody> about();

    /**
     * 消息列表
     * @param openid
     * @return
     */
    @POST("news_list.action")
    Call<ResponseBody> newsList(@Query("openid") String openid);

}
