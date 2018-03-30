package com.jhobor.ddc.interfaces;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/2/10.
 * 用户表网络数据获取
 */

public interface IService {
    /*注册*/
    @POST("userInfo/register.do")
    Call<ResponseBody> register(@Query("mobile") String mobile, @Query("loginPwd") String longPwd);

    /*登录*/
    @POST("userInfo/login.do")
    Call<ResponseBody> login(@Query("mobile") String mobile, @Query("loginPwd") String longPwd);

    /*获取首页数据*/
    @POST("store/home.do")
    Call<ResponseBody> getHomeData(@Query("lng") double lng, @Query("lat") double lat);

    /*我的*/
    @POST("userInfo/mine.do")
    Call<ResponseBody> mine(@Query("uuid") String uuid);

    /*我的订单*/
    @POST("orders/myOrders.do")
    Call<ResponseBody> myOrders(@Query("uuid") String uuid);

    /*订单物流信息*/
    @POST("orders/ordersStream.do")
    Call<ResponseBody> ordersStream(@Query("uuid") String uuid, @Query("ordersId") int ordersId);

    /*确认收货*/
    @POST("orders/receive.do")
    Call<ResponseBody> confirmReceived(@Query("uuid") String uuid, @Query("ordersId") long ordersId);

    /*评价商品*/
    @Multipart
    @POST("evaluate/add.do")
    Call<ResponseBody> comment(@Part List<MultipartBody.Part> partList);

    /*我的消息*/
    @POST("info/myInfo.do")
    Call<ResponseBody> myMsg(@Query("uuid") String uuid, @Query("pageIndex") int pageIndex);

    /*消息详情*/
    @POST("info/details.do")
    Call<ResponseBody> msgDetails(@Query("infoId") int infoId);

    /*我的券包*/
    @POST("userCoupon/myCoupon.do")
    Call<ResponseBody> myTickets(@Query("uuid") String uuid);

    /*上传头像，图文*/
    @POST("userInfo/editGravatar.do")
    Call<ResponseBody> uploadUserPicture(@Body RequestBody body);

    @Multipart
    @POST("userInfo/editGravatar.do")
    Call<ResponseBody> uploadUserPicture2(@Part List<MultipartBody.Part> partList);

    /*修改昵称*/
    @POST("userInfo/editName.do")
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
    Call<ResponseBody> editNickname(@Field("uuid") String uuid, @Field("name") String nickname);

    /*提现*/
    @POST("withdraw/add.do")
    Call<ResponseBody> withdraw(@Query("uuid") String uuid, @Query("money") int money);

    /*绑定银行卡*/
    @POST("bankCard/add.do")
    Call<ResponseBody> bindCard(
            @Query("uuid") String uuid,
            @Query("name") String name,
            @Query("bankName") String bankName,
            @Query("bankNo") String bankNo,
            @Query("city") String city,
            @Query("branch") String branch
    );

    /*银行卡详情*/
    @POST("bankCard/details.do")
    Call<ResponseBody> bankCardDetails(@Query("uuid") String uuid);

    /*我的评论*/
    @POST("evaluate/myEvaluate.do")
    Call<ResponseBody> myComments(@Query("uuid") String uuid, @Query("pageIndex") int pageIndex);

    /*申请开店*/
    @POST("applyforStore/add.do")
    Call<ResponseBody> applyForStore(
            @Query("uuid") String uuid,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("type") String type,
            @Query("addr") String addr
    );

    /*是否申请过派单员了*/
    @POST("realName/myApplyfor.do")
    Call<ResponseBody> hasApply4sender(@Query("uuid") String uuid);

    /*申请成为派单员*/
    @Multipart
    @POST("realName/add.do")
    Call<ResponseBody> apply4sender(@Part List<MultipartBody.Part> partList);

    /*我的收藏*/
    @POST("collect/myCollect.do")
    Call<ResponseBody> myCollection(@Query("uuid") String uuid, @Query("pageIndex") int pageIndex);

    /*新增收货地址*/
    @POST("addr/add.do")
    Call<ResponseBody> addAddr(
            @Query("uuid") String uuid,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("address") String address
    );

    /*我的收货地址*/
    @POST("addr/myAddr.do")
    Call<ResponseBody> myAddr(@Query("uuid") String uuid);

    /*设置默认收货地址*/
    @POST("addr/defaultAddr.do")
    Call<ResponseBody> setDefaultAddr(@Query("uuid") String uuid, @Query("addrId") int addrId, @Query("status") int status);

    /*删除收货地址*/
    @POST("addr/delete.do")
    Call<ResponseBody> delAddr(@Query("uuid") String uuid, @Query("addrId") int addrId);

    /*修改收货地址*/
    @POST("addr/edit.do")
    Call<ResponseBody> editAddr(
            @Query("uuid") String uuid,
            @Query("id") int id,
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("address") String address
    );

    /*获取默认收货地址*/
    @POST("addr/defAddr.do")
    Call<ResponseBody> getDefaultAddr(@Query("uuid") String uuid);

    /*获取验证码*/
    @POST("userInfo/otherVerify.do")
    Call<ResponseBody> getVerifyCode(@Query("mobile") String mobile);

    /*修改密码*/
    @POST("userInfo/update.do")
    Call<ResponseBody> updatePwd(@Query("mobile") String mobile, @Query("oldPwd") String oldPwd, @Query("loginPwd") String newPwd);

    /*忘记密码，重置*/
    @POST("userInfo/forgetPwd.do")
    Call<ResponseBody> resetPwd(@Query("mobile") String mobile, @Query("loginPwd") String newPwd);

    /*获取验证码，注册时使用*/
    @POST("userInfo/registerVerify.do")
    Call<ResponseBody> getRegisterVerifyCode(@Query("mobile") String mobile);

    /*我的派送单*/
    @POST("dispatch/myDispatch.do")
    Call<ResponseBody> myDelivery(@Query("uuid") String uuid, @Query("pageIndex") int pageIndex);

    /*附近购物，tag=1是评分排序（好评），tag=2是销量排序（人气）*/
    @POST("store/storeModule.do")
    Call<ResponseBody> shopNearby(
            @Query("moduleId") int moduleId,
            @Query("lng") double lng,
            @Query("lat") double lat,
            @Query("pageIndex") int pageIndex,
            @Query("tag") int tag
    );

    /*物流抢单*/
    @POST("delivery/list.do")
    Call<ResponseBody> rushOrders(@Query("lat") double lat, @Query("lng") double lng, @Query("pageIndex") int pageIndex);

    /*抢单*/
    @POST("dispatch/add.do")
    Call<ResponseBody> rush(@Query("uuid") String uuid, @Query("deliveryId") int deliveryId);

    /*抢单*/
    @POST("city/list.do")
    Call<ResponseBody> listCity();

    /*农产品  tag 0.评分排序（好评）  1.销量排序（人气） */
    @POST("store/storeFarm.do")
    Call<ResponseBody> getFarmingGoods(
            @Query("moduleId") int moduleId,
            @Query("pageIndex") int pageIndex,
            @Query("optionCity") int optionCity,
            @Query("tag") int tag
    );

    /*我的店铺*/
    @POST("store/myStoreInfo.do")
    Call<ResponseBody> myStore(@Query("uuid") String uuid);

    /*修改店铺昵称*/
    @POST("store/editName.do")
    Call<ResponseBody> editStoreName(@Query("uuid") String uuid, @Query("name") String nickname);

    /*上传店铺图片*/
    @Multipart
    @POST("store/editPicture.do")
    Call<ResponseBody> uploadStorePicture(@Part List<MultipartBody.Part> partList);

    /*卖家中心的产品分类*/
    @POST("category/list.do")
    Call<ResponseBody> storeCategorys(@Query("uuid") String uuid);

    /*添加产品分类*/
    @POST("category/add.do")
    Call<ResponseBody> addStoreCategory(@Query("uuid") String uuid, @Query("name") String name);

    /*添加产品分类*/
    @POST("category/delete.do")
    Call<ResponseBody> delCategory(@Query("uuid") String uuid, @Query("categoryId") int categoryId);

    /*发布产品*/
    @Multipart
    @POST("goods/add.do")
    Call<ResponseBody> publishGoods(@Part List<MultipartBody.Part> partList);

    /*店铺订单管理  status 0-全部，1-待发货，2-待收货，3-已完成 */
    @POST("store/storeOrder.do")
    Call<ResponseBody> storeOrdersManage(@Query("uuid") String uuid, @Query("pageIndex") int pageIndex, @Query("status") int status);

    /*填写第三方物流派送信息*/
    @POST("orders/orderThirdStream.do")
    Call<ResponseBody> othersStream(@Query("uuid") String uuid, @Query("ordersId") int ordersId, @Query("streamNo") String streamNo);

    /*发布抢单派送信息*/
    @POST("delivery/add.do")
    Call<ResponseBody> rushStream(
            @Query("uuid") String uuid,
            @Query("ordersId") int ordersId,
            @Query("title") String title,
            @Query("score") String score,
            @Query("money") String fee,
            @Query("weight") String scale,
            @Query("sendDate") String minTime
    );

    /*抢单派送详情*/
    @POST("delivery/detail.do")
    Call<ResponseBody> rushDetails(@Query("ordersId") int ordersId);

    /*删除抢单派送*/
    @POST("delivery/delete.do")
    Call<ResponseBody> rushOrdersDelete(@Query("uuid") String uuid, @Query("deliveryId") int deliveryId);

    /*订单详情*/
    @POST("orders/detail.do")
    Call<ResponseBody> ordersDetails(@Query("uuid") String uuid, @Query("ordersId") int ordersId);

    /*获得店铺产品，分页*/
    @POST("goods/storeGoods.do")
    Call<ResponseBody> storeGoods(@Query("uuid") String uuid, @Query("pageIndex") int pageIndex, @Query("categoryId") int categoryId);

    /*删除店铺产品*/
    @POST("goods/delete.do")
    Call<ResponseBody> delStoreGoods(@Query("uuid") String uuid, @Query("goodsId") int goodsId);

    /*产品详情*/
    @POST("goods/editUI.do")
    Call<ResponseBody> goodsDetails(@Query("uuid") String uuid, @Query("goodsId") int goodsId);

    /*修改商品小图*/
    @Multipart
    @POST("goods/goodsEdit.do")
    Call<ResponseBody> updateSmallPhoto(@Part List<MultipartBody.Part> partList);

    /*添加商品图片*/
    @Multipart
    @POST("goods/goodsPictureAdd.do")
    Call<ResponseBody> addGoodsPhoto(@Part List<MultipartBody.Part> partList);

    /*修改商品图片*/
    @Multipart
    @POST("goods/goodsPictureEdit.do")
    Call<ResponseBody> updateGoodsPhoto(@Part List<MultipartBody.Part> partList);

    /*修改产品信息*/
    @POST("goods/edit.do")
    Call<ResponseBody> editGoods(
            @Query("uuid") String uuid,
            @Query("id") int goodsId,
            @Query("name") String name,
            @Query("categoryId") int categoryId,
            @Query("price") String price,
            @Query("wholesalePirce") String wholesalePrice,
            @Query("wholesaleCount") String wholesaleCount,
            @Query("stock") String stock
    );

    /*发布优惠券*/
    @POST("coupon/add.do")
    Call<ResponseBody> publishTicket(@Query("uuid") String uuid, @Query("fullMoney") String fullMoney, @Query("cutMoney") String cutMoney, @Query("count") String count);

    /*获取店铺优惠券*/
    @POST("coupon/storeCoupon.do")
    Call<ResponseBody> storeTickets(@Query("uuid") String uuid);

    /*删除店铺优惠券*/
    @POST("coupon/delete.do")
    Call<ResponseBody> delStoreTickets(@Query("uuid") String uuid, @Query("couponId") int couponId);

    /*获取店铺余额、银行卡信息*/
    @POST("withdraw/withdrawInfo.do")
    Call<ResponseBody> getStoreBalance(@Query("uuid") String uuid);

    /*获取店铺余额、银行卡信息*/
    @POST("withdraw/storeWithdraw.do")
    Call<ResponseBody> storeWithdraw(@Query("uuid") String uuid, @Query("money") int money);

    /*获取店铺信息、店铺分类、第一个分类首页产品*/
    @POST("store/details.do")
    Call<ResponseBody> storeGoodsAndInfo(@Query("storeId") int storeId);

    /*获取店铺某个分类某页的产品*/
    @POST("store/storeCategory.do")
    Call<ResponseBody> storeCategoryGoods(@Query("storeId") int storeId, @Query("categoryId") int categoryId, @Query("pageIndex") int pageIndex);

    /*获取店铺评分和评论*/
    @POST("store/myStore.do")
    Call<ResponseBody> storeComments(@Query("storeId") int storeId);

    /*获取店铺分页评论*/
    @POST("evaluate/storeEvaluate.do")
    Call<ResponseBody> storePageComments(@Query("storeId") int storeId, @Query("pageIndex") int pageIndex);

    /*获取店铺相关信息*/
    @POST("store/storeInfo.do")
    Call<ResponseBody> storeInfo(@Query("storeId") int storeId);

    /*获取批量产品信息，用于更新购物车的产品信息*/
    @POST("shopCar/myShopCar.do")
    Call<ResponseBody> batchGoodsInfo(@Query("goodsIds") int[] goodsIds);

    /*提交购买的商品*/
    @POST("orders/add.do")
    Call<ResponseBody> startBuy(@Query("params") String json);

    /*删除待付款订单*/
    @POST("orders/delete.do")
    Call<ResponseBody> delOrders(@Query("uuid") String uuid, @Query("ordersId") long ordersId);

}
