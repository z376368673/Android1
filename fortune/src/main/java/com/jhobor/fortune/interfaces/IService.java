package com.jhobor.fortune.interfaces;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/5.
 */

public interface IService {

    /**
     * 注册
     *
     * @param mobile
     * @param loginPwd
     * @param name
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("userInfo/register")
    Call<ResponseBody> register(@Field("name") String name,@Field("mobile") String mobile, @Field("loginPwd") String loginPwd, @Field("phone") String phone);

    /**
     * 登录
     *
     * @param mobile
     * @param loginPwd
     * @return
     */
    @POST("userInfo/login")
    Call<ResponseBody> login(@Query("mobile") String mobile, @Query("loginPwd") String loginPwd);

    /**
     * 查询首页我的盈亏额，理财条目数据
     *
     * @param token
     * @return
     */
    @POST("order/myFortune.do")
    Call<ResponseBody> myFortune(@Query("uuid") String token);

    /**
     * 查询理财条目详情
     *
     * @param token
     * @param orderId
     * @return
     */
    @POST("collection/collectionDetail.do")
    Call<ResponseBody> fortuneDetails(@Query("uuid") String token, @Query("orderId") int orderId);

    /**
     * 添加理财
     *
     * @param token
     * @param money
     * @return
     */
    @POST("order/add.do")
    Call<ResponseBody> manageProperty(@Query("uuid") String token, @Query("money") int money);

    /**
     * 我的团队
     *
     * @param token
     * @return http://tz.1yuanpf.com/rentalcarUsb/groupInfo/myGroupInfo
     */
    @POST("groupInfo/myGroupInfo.do")
    Call<ResponseBody> myLower(@Query("uuid") String token);

    /**
     * 我的团队
     * 查询下级团队
     * @param token
     * @return http://tz.1yuanpf.com/rentalcarUsb/groupInfo/myGroupInfo
     */
    @POST("groupInfo/queryLowerLevel.do")
    Call<ResponseBody> queryLowerLevel(@Query("uuid") String token,@Query("mobile") String mobile);



    /**
     * 钱包金额及下线金额
     *
     * @param token
     * @return
     */
    @POST("userInfo/myInvest.do")
    Call<ResponseBody> myInvest(@Query("uuid") String token);

    /**
     * 钱包明细，收益明细
     *
     * @param token
     * @param pageIndex
     * @return
     */
    @POST("record/myRecord.do")
    Call<ResponseBody> walletsDetails(@Query("uuid") String token, @Query("pageIndex") int pageIndex);

    /**
     * 个人页面信息
     *
     * @param token
     * @return
     */
    @POST("userInfo/mine.do")
    Call<ResponseBody> mine(@Query("uuid") String token);

    /**
     * 收款方式信息
     *
     * @param token
     * @return
     */
    @POST("optionMode/myOptionMode.do")
    Call<ResponseBody> receiptWay(@Query("uuid") String token);

    /**
     * 添加银联卡信息
     *
     * @param token
     * @param bankName
     * @param name
     * @param account
     * @return
     */
    @FormUrlEncoded
    @POST("optionMode/add.do")
    Call<ResponseBody> addAccountInfo(@Field("uuid") String token,
                                      @Field("bankName") String bankName,
                                      @Field("name") String name,
                                      @Field("account") String account,
                                      @Field("type") String type);

    /**
     * 删除银行卡
     * @param token
     * @return
     */
    @POST("bankCard/delete")
    Call<ResponseBody> delBank(@Query("uuid") String token,
                                  @Query("bankCardId") int bankCardId
                                  );

    /**
     * 我的账户 银行卡列表
     * @param token
     * @return
     */
    @POST("bankCard/list")
    Call<ResponseBody> myBankList(@Query("uuid") String token);

    /**
     * 添加银行卡
     *
     * @param token
     * @param name
     * @param bankName 银行类型 名称
     * @param bankNo   卡号
     * @return
     */
    @FormUrlEncoded
    @POST("bankCard/add")
    Call<ResponseBody> addBank(@Field("uuid") String token,
                               @Field("name") String name,
                               @Field("identityCard") String identityCard,
                               @Field("bankName") String bankName,
                               @Field("bankNo") String bankNo,
                               @Field("subbranch") String subbranch );


    @FormUrlEncoded
    @POST("optionMode/edit.do")
    Call<ResponseBody> changedAccountInfo(@Field("id") String id,
                                          @Field("uuid") String token,
                                          @Field("bankName") String bankName,
                                          @Field("name") String name,
                                          @Field("account") String account,
                                          @Field("type") String type);


    /**
     * 交易记录
     *
     * @param token
     * @return
     */
    @POST("order/fortuneRecord.do")
    Call<ResponseBody> tradeRecord(@Query("uuid") String token);

    /**
     * 提现记录
     *
     * @param token
     * @param pageIndex
     * @return
     */
    @POST("withdraw/myWithdraw.do")
    Call<ResponseBody> withdrawRecord(@Query("uuid") String token, @Query("pageIndex") int pageIndex);

    /**
     * 可用金额，提现前查询
     *
     * @param token
     * @return
     */
    @POST("withdraw/withdrawWay.do")
    Call<ResponseBody> myBalance(@Query("uuid") String token);

    /**
     * 提交提现申请
     *
     * @param token
     * @param money
     * @param optionModeId
     * @return
     */
    @POST("withdraw/add.do")
    Call<ResponseBody> withdraw(@Query("uuid") String token,
                                @Query("money") int money,
                                @Query("optionModeId") int optionModeId);

    /**
     * 修改密码
     *
     * @param token
     * @param loginPwd
     * @param oldPwd
     * @return
     */
    @POST("userInfo/update.do")
    Call<ResponseBody> updatePass(@Query("uuid") String token, @Query("loginPwd") String loginPwd, @Query("oldPwd") String oldPwd);

    /**
     * 重置密码
     *
     * @param mobile
     * @param loginPwd
     * @return
     */
    @POST("userInfo/forgetPwd")
    Call<ResponseBody> resetPass(@Query("mobile") String mobile, @Query("loginPwd") String loginPwd);

    /**
     * 手机号码存在于数据库的验证码获取
     *
     * @param mobile
     * @return
     */
    @POST("userInfo/otherVerify")
    Call<ResponseBody> otherVerify(@Query("mobile") String mobile);

    /**
     * 手机号码不存在于数据库的验证码获取接口
     *
     * @param mobile
     * @return
     */
    @POST("userInfo/registerVerify")
    Call<ResponseBody> registerVerify(@Query("mobile") String mobile);

    /**
     * 交易查看页面，从首页进
     *
     * @param token
     * @return
     */
    @POST("help/myHelp.do")
    Call<ResponseBody> tradeDetails(@Query("uuid") String token);

    /**
     * 提供帮助 或 获得帮助
     *
     * @param token
     * @param money
     * @param tag
     * @return
     */
    @POST("help/add.do")
    Call<ResponseBody> help(@Query("uuid") String token, @Query("money") int money, @Query("tag") int tag, @Query("omId") int omId);

    /**
     * 从奖金钱包提现
     *
     * @param token
     * @param money
     * @param omId
     * @return
     */
    @POST("help/addMarker.do")
    Call<ResponseBody> withdrawFromPrize(@Query("uuid") String token, @Query("money") int money, @Query("omId") int omId);

    /**
     * 静态钱包、冻结钱包明细
     *
     * @param token
     * @param tag
     * @return
     */
    @POST("help/helpList.do")
    Call<ResponseBody> balanceRecord(@Query("uuid") String token, @Query("tag") int tag);

    /**
     * 冻结钱包-利息记录
     *
     * @param token
     * @param pageIndex
     * @return
     */
    @POST("interestInfo/myInterestInfo.do")
    Call<ResponseBody> myInterestInfo(@Query("uuid") String token, @Query("pageIndex") int pageIndex);

    /**
     * 排单详情
     *
     * @param token
     * @param helpId
     * @param tag
     * @return
     */
    @POST("help/detail.do")
    Call<ResponseBody> boodingDetails(@Query("uuid") String token, @Query("helpId") int helpId, @Query("tag") int tag);

    /**
     * 上传凭证
     *
     * @param partList
     * @return
     */
    @Multipart
    @POST("help/editHelp.do")
    Call<ResponseBody> uploadCertificate(@Part List<MultipartBody.Part> partList);

    /**
     * 确认得到帮助
     *
     * @param token
     * @param helpId
     * @return
     */
    @POST("help/update.do")
    Call<ResponseBody> confirmGetHelp(@Query("uuid") String token, @Query("helpId") int helpId);


    @POST("help/myHelpRecord.do")
    Call<ResponseBody> tradeRecordHelp(@Query("uuid") String token, @Query("tag") int tag);

    /**
     * 获取排单币
     *
     * @param token
     * @return
     */
    @POST("userInfo/myCoin.do")
    Call<ResponseBody> myCoin(@Query("uuid") String token);

    /**
     * 转让排单币
     *
     * @param token
     * @param count
     * @param mobile
     * @return
     */
    @POST("userInfo/editCoin.do")
    //@POST("bonusRecord/transfer.do")
    Call<ResponseBody> transferCoin(@Query("uuid") String token, @Query("count") int count, @Query("mobile") String mobile);

    /**
     * 获取可用的提现方式
     *
     * @param token
     * @return
     */
    @POST("userInfo/myOm.do")
    Call<ResponseBody> myOm(@Query("uuid") String token);


    /**
     * 激活用户账号
     *
     * @param uuid
     * @return
     */
    @POST("userInfo/activation.do")
    Call<ResponseBody> account(@Query("uuid") String uuid);

    /**
     * 激活用户账号
     *
     * @param uuid
     * @return
     */
    @POST("bonusRecord/list.do")
    Call<ResponseBody> dynmic(@Query("uuid") String uuid);

    /**
     * 转让激活码
     *
     * @param token
     * @param count
     * @param mobile
     * @return
     */

    //@POST("bonusRecord/transfer.do")
    @POST("userInfo/transfer.do")
    Call<ResponseBody> transferActiveCode(@Query("uuid") String token, @Query("count") int count, @Query("mobile") String mobile);

    //@POST("bonusRecord/transfer.do")
    @POST("help/addMarker.do")
    Call<ResponseBody> getOmList(@Query("uuid") String token,
                                 @Query("money") int money,
                                 @Query("tag") String tag,
                                 @Query("omId") String omId);

    /**
     * @param uuid
     * @return
     */
    @POST("userInfo/userHome")
    Call<ResponseBody> getMain(@Query("uuid") String uuid);

    @POST("userInfo/myReport")
    Call<ResponseBody> getmyReport(@Query("uuid") String uuid);

    /**
     *获取积分记录
     * @param uuid
     * @param tag
     * @return
     */
    @POST("integralRecord/list")
    Call<ResponseBody> getIntegralList(@Query("uuid") String uuid,@Query("tag") int tag);
    /**
     *获取微积分记录
     * @param uuid
     * @param tag
     * @return
     */
    @POST("calculusRecord/list")
    Call<ResponseBody> getalculusRecordList(@Query("uuid") String uuid,@Query("tag") int tag);








    @POST("amountRecord/list")
    Call<ResponseBody> getRecordlList(@Query("uuid") String uuid,@Query("tag") int tag);


    @POST("userInfo/addCapital")
    Call<ResponseBody> addMoney(@Query("uuid") String uuid, @Query("money") double bigDecimal);

    /**
     * 积分提现接口
     * @param token
     * @param loginPwd
     * @param bankCardId
     * @param money
     * @return
     */
    @POST("withdraw/add")
    Call<ResponseBody> withdrawMoney(@Query("uuid") String token,
                                     @Query("loginPwd") String loginPwd,
                                     @Query("bankCardId") String bankCardId,
                                     @Query("money") BigDecimal money);

    /**
     * 电商积分提现接口
     * @param token
     * @param loginPwd
     * @param bankCardId
     * @param money
     * @return
     */
    @POST("calculusWithdraw/add")
    Call<ResponseBody> myWithdrawMoney(@Query("uuid") String token,
                                     @Query("loginPwd") String loginPwd,
                                     @Query("bankCardId") String bankCardId,
                                     @Query("calculus") BigDecimal money);

    /**
     * 服务积分提现接口
     * @param token
     * @param loginPwd
     * @param bankCardId
     * @param money
     * @return
     */
    @POST("billWithdraw/add")
    Call<ResponseBody> ReportWithdrawMoney(@Query("uuid") String token,
                                       @Query("loginPwd") String loginPwd,
                                       @Query("bankCardId") String bankCardId,
                                       @Query("billIntegral") BigDecimal money);

    /**
     * 积分转让接口
     * @param token
     * @param loginPwd
     * @param mobile
     * @param omId
     * @return
     */
    @POST("userInfo/integralTransfer")
    Call<ResponseBody> integralTransfer(@Query("uuid") String token,
                                        @Query("loginPwd") String loginPwd,
                                        @Query("mobile") String mobile,
                                        @Query("integral") BigDecimal omId);
    /**
     * 微积分转让接口
     * @param token
     * @param loginPwd
     * @param mobile
     * @param omId
     * @return
     */
    @POST("userInfo/calculusTransfer")
    Call<ResponseBody> calculusTransfer(@Query("uuid") String token,
                                        @Query("loginPwd") String loginPwd,
                                        @Query("mobile") String mobile,
                                        @Query("calculus") BigDecimal omId);

    /**
     * 服务分转让接口
     * @param token
     * @param loginPwd
     * @param mobile
     * @param omId
     * @return
     */
    @POST("userInfo/billIntegralTransfer")
    Call<ResponseBody> reportTransfer(@Query("uuid") String token,
                                        @Query("loginPwd") String loginPwd,
                                        @Query("mobile") String mobile,
                                        @Query("billIntegral") BigDecimal omId);


    /**
     * 意见反馈
     * @param token
     * @return
     */
    @POST("userInfo/myQrCode")
    Call<ResponseBody> share(@Query("uuid") String token,
                               @Query("str") String str);

    /**
     * 关于我们
     * @param uuid
     * @return
     */
    @POST("aboutWe/list")
    Call<ResponseBody> aboutWe(@Query("uuid") String uuid);

    /***
     * 意见反馈
     * @param uuid
     * @param content
     * @return
     */
    @POST("feedback/add")
    Call<ResponseBody> feedback(@Query("uuid") String uuid,
                               @Query("content") String content);

    /***
     * 分享产品界面的接口
     * @param token
     * @return
     */
    @POST("product/list")
    Call<ResponseBody> shareProduct(@Query("uuid") String token);

    /***
     * 分享界面的接口
     * @param token
     * @return
     */
    @POST("userInfo/myQrCode")
    Call<ResponseBody> share(@Query("uuid") String token);



    @Multipart
    @POST("userInfo/updateImg")
    Call<ResponseBody> uploadheadImg(@Part List<MultipartBody.Part> partList);



    /**
     * 查询服务中心的状态
     *
     * @param token
     * @return   status：msg为1时返回，判断状态0.不是，1.是，2审核中
     */
    @POST("billCenter/isBillCenter")
    Call<ResponseBody> isBillCenter(@Query("uuid") String token);

    /**
     * 服务中心 获取下属数量和投资金额
     *
     * @param token
     * @return http://tz.1yuanpf.com/rentalcarUsb/groupInfo/myGroupInfo
     */
    @POST("billCenter/application")
    Call<ResponseBody> applyChecked(@Query("uuid") String token);

    /**
     * 申请加入服务中心
     *
     * @param token
     * @return http://tz.1yuanpf.com/rentalcarUsb/groupInfo/myGroupInfo
     */
    @POST("billCenter/add")
    Call<ResponseBody> applyAdd(@Query("uuid") String token);


    /**
     * 服务中心首页
     *
     * @param token
     * @return
     */
    @POST("billCenter/billCenterHome")
    Call<ResponseBody> billCenterHome(@Query("uuid") String token);


    /**
     * 服务中心首页之旗下服务中心
     *
     * @param token
     * @return
     */
    @POST("billCenter/childBillCenter")
    Call<ResponseBody> childBillCenter(@Query("uuid") String token);


    /**
     * 服务中心首页之非服务中心
     *
     * @param token
     * @return
     */
    @POST("billCenter/notBillCenter")
    Call<ResponseBody> notBillCenter(@Query("uuid") String token);


    /**
     * 服务中心-服务积分增长记录
     *
     * @param token
     * @return
     */
    @POST("billCenter/billIntegralRecord")
    Call<ResponseBody> billIntegralRecord(@Query("uuid") String token);


    /**
     *  团队投资明细-增资记录
     *
     * @param token
     * @return
     */
    @POST("billCenter/groupInvestRecord")
    Call<ResponseBody> groupInvestRecord(@Query("uuid") String token);

    /**
     *  团队投资明细-未投资记录
     *
     * @param token
     * @return
     */
    @POST("billCenter/noInvestRecord")
    Call<ResponseBody> noInvestRecord(@Query("uuid") String token);

    /**
     *获取服务积分记录
     * @param uuid
     * @param tag
     * @return
     */
    @POST("billIntegralRecord/list")
    Call<ResponseBody> billIntegralRecord(@Query("uuid") String uuid,@Query("tag") int tag);

}
