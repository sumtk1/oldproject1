package com.gloiot.hygo.server.network;

import android.content.SharedPreferences;
import android.util.Log;

import com.gloiot.hygo.ui.activity.shopping.Bean.DizhiBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShouhuoAddress;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.zyd.wlwsdk.server.network.HttpManager;
import com.zyd.wlwsdk.server.network.OnDataListener;
import com.zyd.wlwsdk.server.network.utlis.EnDecryptUtlis;
import com.zyd.wlwsdk.server.network.utlis.JsonUtils;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MD5Utlis;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by hygo01 on 2016/7/15.
 * 网络请求
 */
public class RequestAction {

    private static SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences();
    // 登录后随机码
    private static String randomCode = sp.getString(ConstantUtlis.SP_RANDOMCODE, "");


    // 打印请求的参数
    private static void mLog(HashMap<String, Object> hashMap) {
        for (Object key : hashMap.keySet()) {
            L.e("----------", key + "=" + hashMap.get(key));
        }
    }

    private static RequestParams getParams(String func, HashMap<String, Object> hashMap) {
        randomCode = randomCode.equals("") ? MD5Utlis.Md5(func) : randomCode;
        RequestParams params = new RequestParams();
        params.add("func", func);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), randomCode));
        Log.e("funcwords", params.toString());
        return params;
    }


    // 获取支付地址
    public final static int TAG_PAYWEBURL = 10086;

    public RequestHandle getPayWebUrl(OnDataListener onDataListener, int showLoad, int time) {
        RequestParams params = new RequestParams();
        params.add("func", "p_jssdk");
        params.add("category", "android");
        return HttpManager.doPost(ConstantUtlis.WEBJS_URL, TAG_PAYWEBURL, params, onDataListener, showLoad, time);
    }


    // 用户登录p_login->p_login_two->p_login_three
    public final static int TAG_USERLOGIN = 1;

    public RequestHandle UserLogin(OnDataListener onDataListener, String userId, String yanzhengma, String phoneid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", "");
        hashMap.put("手机号", userId);
        hashMap.put("验证码", yanzhengma);
        hashMap.put("随机码", MD5Utlis.Md5("p_login_Version2.0.0"));
        hashMap.put("phoneid", phoneid);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_USERLOGIN, getParams("p_login_Version2.0.0", hashMap), onDataListener, 0);
    }


    // 微信登录p_fw_weixin->p_fw_weixin_two->p_fw_weixin_three
    public final static int TAG_WXLOGIN = 2;

    public RequestHandle WXLogin(OnDataListener onDataListener, String wxCode, String phoneid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("随机码", MD5Utlis.Md5("p_fw_weixin_Version3.0.0"));
        hashMap.put("code", wxCode);
        hashMap.put("phoneid", phoneid);
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_WXLOGIN, getParams("p_fw_weixin_Version3.0.0", hashMap), onDataListener, 0);
    }

    // 获取验证码p_sendsms->p_sendsms_two->p_sendsms_three
    public final static int TAG_YZM = 3;

    public RequestHandle YZM(OnDataListener onDataListener, String num, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号码", num);
        hashMap.put("类别", type);

        if ("成功".equals(sp.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {   //登录状态下
            hashMap.put("账号", ConstantUtlis.SP_USERPHONE);
            hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        } else {
            hashMap.put("账号", "");
            hashMap.put("随机码", MD5Utlis.Md5("p_sendsms_Version2.0.0"));
            hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        }

        mLog(hashMap);
        return HttpManager.doPost(TAG_YZM, getParams("p_short_message", hashMap), onDataListener, 0);
    }

    // 忘记密码p_forgetLoginPassword->p_forgetLoginPassword_two
    public final static int TAG_FORGETPWD = 6;

    public RequestHandle ForgetPwd(OnDataListener onDataListener, String num, String pwd, String yzm) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", num);
        hashMap.put("新密码", pwd);
        hashMap.put("确认密码", pwd);
        hashMap.put("验证码", yzm);
        hashMap.put("随机码", MD5Utlis.Md5("p_forgetLoginPassword_Version2.0.0"));
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_FORGETPWD, getParams("p_forgetLoginPassword_Version2.0.0", hashMap), onDataListener, 0);
    }

    // 忘记支付密码p_forgetPaymentPassword->p_forgetPaymentPassword_two
    public final static int TAG_FORGETZHIFUPWD = 7;

    public RequestHandle ForgetZhiFuPwd(OnDataListener onDataListener, String num, String pwd, String yzm) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", num);
        hashMap.put("新密码", pwd);
        hashMap.put("确认密码", pwd);
        hashMap.put("验证码", yzm);
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_FORGETZHIFUPWD, getParams("p_forgetPaymentPassword_Version2.0.0", hashMap), onDataListener, 0);
    }


    //修改登录密码p_updateLoginPassword->p_updateLoginPassword_two
    public final static int TAG_XIUGAIDENGLUMIMA = 8;

    public RequestHandle XiuGaiDengLuMiMa(OnDataListener onDataListener, String CurrentPwd, String NewPwd, String ConfirmPwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("原密码", MD5Utlis.Md5(CurrentPwd));
        hashMap.put("新密码", MD5Utlis.Md5(NewPwd));
        hashMap.put("确认密码", MD5Utlis.Md5(ConfirmPwd));
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_XIUGAIDENGLUMIMA, getParams("p_updateLoginPassword_Version2.0.0", hashMap), onDataListener, 0);
    }

    //修改支付密码p_updatePaymentPassword->p_updatePaymentPassword_two
    public final static int TAG_XIUGAIZHIFUMIMA = 9;

    public RequestHandle XiuGaiZhiFuMiMa(OnDataListener onDataListener, String CurrentPwd, String NewPwd, String ConfirmPwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("原密码", MD5Utlis.Md5(CurrentPwd));
        hashMap.put("新密码", MD5Utlis.Md5(NewPwd));
        hashMap.put("确认密码", MD5Utlis.Md5(ConfirmPwd));
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_XIUGAIZHIFUMIMA, getParams("p_updatePaymentPassword_Version2.0.0", hashMap), onDataListener, 0);
    }

    // 获取图片清单p_picture_list->p_picture_list_two
    public final static int TAG_PictureList = 10;

    public RequestHandle PictureList(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_PictureList, getParams("p_picture_list_Version2.0.0", hashMap), onDataListener, -1);
    }


    //获取个人资料数据p_member_inform->p_member_inform_two
    public final static int TAG_GETMYDATA = 11;

    public RequestHandle GetMyData(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETMYDATA, getParams("p_member_inform_Version2.0.0", hashMap), onDataListener, 2);
    }

    //修改个人资料p_update_user->p_update_user_two
    public final static int TAG_UPDATEYMYDATA = 12;

    public RequestHandle UpdateMyData(OnDataListener onDataListener, HashMap<String, Object> hashMap) {
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_UPDATEYMYDATA, getParams("p_update_user_Version2.0.0", hashMap), onDataListener, 0);
    }

    //实名认证获取认证状态
    public final static int TAG_GETREALNAMESTATE = 13;

    public RequestHandle GetRealNameState(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETREALNAMESTATE, getParams("p_get_certification", hashMap), onDataListener, 0);
    }

    //进行实名认证p_certification->p_certification_two
    public final static int TAG_GOREALNAME = 15;

    public RequestHandle GoRealame(OnDataListener onDataListener, String phoneNum, String name, String sfn_num, String zhifupwd, String weiyiID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", phoneNum);
        hashMap.put("真实姓名", name);
        hashMap.put("身份证号", sfn_num);
        hashMap.put("支付密码", zhifupwd);
        hashMap.put("唯一id", weiyiID);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GOREALNAME, getParams("p_certification_Version2.0.0", hashMap), onDataListener, 0);
    }

    //我的人脉
    public final static int TAG_GETMYCONTACTS = 16;

    public RequestHandle GetMyContacts(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETMYCONTACTS, getParams("p_my_renmai_Version2.0.0", hashMap), onDataListener, 0);
    }


    //获取银行卡p_bank_typ->p_bank_type_two
    public final static int TAG_XUANZEYINHANGKALEIXING = 17;

    public RequestHandle XuanZeYinHangKaLeiXing(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_XUANZEYINHANGKALEIXING, getParams("p_bank_type_Version2.0.0", hashMap), onDataListener, 0);
    }

    //获取银行卡
    public final static int TAG_HUOQVYINHANGKA = 18;

    public RequestHandle HuoQvYinHangKa(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_HUOQVYINHANGKA, getParams("p_bank_info", hashMap), onDataListener, 0);
    }

    //解绑银行卡p_bindbankcard->p_bindbankcard_two
    public final static int TAG_JIEBANGYINHANGKA = 19;

    public RequestHandle JieBangYinHangKa(OnDataListener onDataListener, String id, String paypwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("银行id", id);
        hashMap.put("支付密码", MD5Utlis.Md5(paypwd));
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        return HttpManager.doPost(TAG_JIEBANGYINHANGKA, getParams("p_bindbankcard_Version2.0.0", hashMap), onDataListener, 0);
    }

    //添加银行卡p_bindingCard->p_bindingCard_two
    public final static int TAG_TIANJAYINHANGKA = 20;

    public RequestHandle TianJiaYinHangKa(OnDataListener onDataListener, String name, String kahao, String type, String xingming, String shoujihao, String yanzhengma) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("银行名", name);
        hashMap.put("银行卡号", kahao);
        hashMap.put("类别", type);
        hashMap.put("姓名", xingming);
        hashMap.put("手机号", shoujihao);
        hashMap.put("验证码", yanzhengma);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        return HttpManager.doPost(TAG_TIANJAYINHANGKA, getParams("p_bindingCard_Version2.0.0", hashMap), onDataListener, 0);
    }

    //意见反馈p_insert_feedback->p_insert_feedback_two->p_insert_feedback_three
    public final static int TAG_YIJIANFANKUI = 21;

    public RequestHandle YiJianFanKui(OnDataListener onDataListener, String type, String miaoshu, String myPicPathArray) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("反馈类别", type);
        hashMap.put("问题描述", miaoshu);
        hashMap.put("图片", myPicPathArray);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_YIJIANFANKUI, getParams("p_insert_feedback_Version2.0.0", hashMap), onDataListener, 0);
    }

    //我的优惠券p_sel_juan->p_sel_juan_two
    public final static int TAG_GETMYDISCOUNTCOUPON = 22;

    public RequestHandle GetMyDiscountCoupon(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType, int page, int requesTag, int showLoad) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", "");
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(requesTag, getParams("p_sel_juan_two", hashMap), onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    //我的优惠券——新的
    public RequestHandle GetMyDiscountCoupon(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("页数", yeshu);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETMYDISCOUNTCOUPON, getParams("shop_New_coupon", hashMap), onDataListener, -1, pullToRefreshLayout, requestType);
    }


    // 获取类别列表p_type_details->p_type_details_two
    public final static int TAG_GETLEIBIELIST = 24;

    public RequestHandle getLeiBieList(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETLEIBIELIST, getParams("p_type_details_Version2.5.0", hashMap), onDataListener, 0);
    }

    //我的现金券p_my_vouchers->p_my_vouchers_two
    public final static int TAG_GETMYCASHCOUPON = 25;

    public RequestHandle GetMyCashCoupon(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType, int page, int requesTag, int showLoad) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", "");
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(requesTag, getParams("p_my_vouchers_Version2.0.0", hashMap), onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    // 设置支付密码p_setupPamentPassword->p_setupPamentPassword_two
    public final static int TAG_SETZHIFUPWD = 26;

    public RequestHandle SetZhiFuPwd(OnDataListener onDataListener, String num, String pwd, String yzm) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", num);
        hashMap.put("新密码", pwd);
        hashMap.put("确认密码", pwd);
        hashMap.put("验证码", yzm);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SETZHIFUPWD, getParams("p_setupPamentPassword_Version2.0.0", hashMap), onDataListener, 0);
    }


    // 获取-红利积分记录/基本积分记录-列表p_bonus_points->p_bonus_points_two
    public RequestHandle getHongLiJiBen(int TAG_REQUEST, int isDialog, OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout,
                                        int requsetType, int page, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账户", type);
        hashMap.put("页数", page);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_REQUEST, getParams("p_bonus_points_Version2.5.0", hashMap), onDataListener, isDialog, pullToRefreshLayout, requsetType);
    }

    // 账户积分数据p_sel_details->p_sel_details_two
    public RequestHandle getRecordJiFen(int TAG_REQUEST, int isDialog, OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout,
                                        int requsetType, int page, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("交易账户", type);
        hashMap.put("页数", page);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_REQUEST, getParams("p_sel_details_Version2.5.0", hashMap), onDataListener, isDialog, pullToRefreshLayout, requsetType);
    }

    //红利(基本积分显示)
    public final static int TAG_PERSONALCENTER = 27;

    public RequestHandle PersonalCenter(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_PERSONALCENTER, getParams("p_personal_center_two", hashMap), onDataListener, 0);
    }

    // 不显示加载框
    public RequestHandle PersonalCenterMy(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_PERSONALCENTER, getParams("p_personal_center_two", hashMap), onDataListener, -1);
    }

    //红利转让获取姓名p_zr_jfzl->p_zr_jfzl_two
    public final static int TAG_GETNAME = 28;

    public RequestHandle GetName(OnDataListener onDataListener, String num) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("接收人账号", num);
        hashMap.put("随机码", randomCode);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETNAME, getParams("p_zr_jfzl_Version2.0.0", hashMap), onDataListener, 0);
    }

    //转让积分p_transferIntegral->p_transferIntegral_two
    public final static int TAG_TRANSFERINTEGRAL = 29;

    public RequestHandle TransferIntegral(OnDataListener onDataListener, String userType, String phone, String pwd, String jifen, String beizhu, String zrLeixing) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号类型", userType);
        hashMap.put("收方账号", phone);
        hashMap.put("支付密码", pwd);
        hashMap.put("积分", jifen);
        hashMap.put("备注", beizhu);
        hashMap.put("转让类型", zrLeixing);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_TRANSFERINTEGRAL, getParams("p_transferIntegral_Version2.0.0", hashMap), onDataListener, 0);
    }

    //积分充值明细p_Recharge_details->p_Recharge_details_two
    public final static int TAG_GETJIFENCHONGZHIMINGXI = 30;

    public RequestHandle GetJifenChongzhiMingxi(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType, int page, int requesTag, int showLoad) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", "");
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(requesTag, getParams("p_Recharge_details_Version2.0.0", hashMap), onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    //积分转让明细p_TransferDetailed->p_TransferDetailed_two
    public final static int TAG_GETJIFENZHUANRANGMINGXI = 31;

    public RequestHandle GetJifenZhuanrangMingxi(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType, int page, int requesTag, int showLoad) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", "");
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(requesTag, getParams("p_TransferDetailed_Version2.0.0", hashMap), onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    //微信授权关联手机号p_bindPhone->p_bindPhone_two
    public final static int TAG_WEIXINGUANLIANSHOUJIHAO = 32;

    public RequestHandle WeChatRelevancePhone(OnDataListener onDataListener, String number, String verificationCode, String zhanghao, String openid, String unionid, String pwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("随机码", MD5Utlis.Md5("p_bindPhone_Version3.0.0"));
        hashMap.put("手机号", number);
        hashMap.put("验证码", verificationCode);
        hashMap.put("账号", zhanghao);
        hashMap.put("微信openid", openid);
        hashMap.put("微信unionid", unionid);
        hashMap.put("密码", pwd);
        hashMap.put("确认密码", pwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        mLog(hashMap);
        return HttpManager.doPost(TAG_WEIXINGUANLIANSHOUJIHAO, getParams("p_bindPhone_Version3.1.0", hashMap), onDataListener, 0);
    }

    //微信授权关联手机号密码设置p_setupPassword->p_setupPassword_two
    public final static int TAG_WECHATPWD = 33;

    public RequestHandle WeChatPwd(OnDataListener onDataListener, String number, String pwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("随机码", MD5Utlis.Md5("p_setupPassword_Version2.0.0"));
        hashMap.put("手机号", number);
        hashMap.put("密码", pwd);
        hashMap.put("确认密码", pwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        return HttpManager.doPost(TAG_WECHATPWD, getParams("p_setupPassword_Version2.0.0", hashMap), onDataListener, 0);
    }


    //查询银行卡p_bank_info—>p_bank_info_two
    public final static int TAG_BANKINFO = 34;

    public RequestHandle BankInfo(OnDataListener onDataListener, String cardType) {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        hashMap.put("version", "2.5.0");
        hashMap.put("平台", "用户");
        hashMap.put("type", cardType);
        mLog(hashMap);
        return HttpManager.doPost(TAG_BANKINFO, getParams("shop_wl_Bankcard_Version1.3.0", hashMap), onDataListener, 0);
    }

    //红利提取p_dividendExchange->p_dividendExchange_two
    public final static int TAG_DIVIDENDEXCHANGE = 35;

    public RequestHandle DividendExchange(OnDataListener onDataListener, String onlyId, String id, String pwd, String jifen) {
        return DividendExchange(onDataListener, onlyId, id, pwd, jifen, false);
    }

    public RequestHandle DividendExchange(OnDataListener onDataListener, String onlyId, String id, String pwd, String jifen, boolean isZhiFuBao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlyId", onlyId);
        hashMap.put("银行卡id", id);
        hashMap.put("积分", jifen);
        hashMap.put("支付密码", pwd);
        hashMap.put("到账类别", "支付宝");
        String func;
        if (isZhiFuBao) {
            func = "p_Exchange_Payment";
        } else {
            func = "p_dividendExchange_Version2.0.0";
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_DIVIDENDEXCHANGE, getParams(func, hashMap), onDataListener, 0);
    }

    //获取赠送积分
    public final static int TAG_GIVEINTEGRAL = 37;

    public RequestHandle GiveIntegral(OnDataListener onDataListener, String jifen, String balance, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("积分", jifen);
        hashMap.put("红利余额", balance);
        hashMap.put("支付方式", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GIVEINTEGRAL, getParams("p_GiveIntegral", hashMap), onDataListener, 0);
    }

    //积分充值下单p_integral_order->p_integral_order_two
    public final static int TAG_INTEGRALORDER = 38;

    public RequestHandle IntegralOrder(OnDataListener onDataListener, String totalMoney) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("总金额", totalMoney);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_INTEGRALORDER, getParams("p_integral_order_Version2.0.0", hashMap), onDataListener, 0);
    }


    //红利提取明细p_shop_bank->p_shop_bank_two
    public final static int TAG_DIVIDENDEXCHANGEMINGXI = 40;

    public RequestHandle DividendExchangeMingxi(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType, int page, int requesTag, int showLoad) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("页数", page + "");
        /*if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", "");
        }*/
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(requesTag, getParams("p_shop_bank_Version2.0.0", hashMap), onDataListener, showLoad, pullToRefreshLayout, requestType);
    }


    //获取首页数据shop_hp_ExhibitionB—>shop_hp_ExhibitionC
    public final static int TAG_SHOUYE = 100;

    public RequestHandle GetShouyeData(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取首页数据", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOUYE, getParams("shop_hp_ExhibitionC", hashMap), onDataListener, -1);
    }

    //获取首页推荐商品数据

    public final static int TAG_SHOP_C_RECOMMEND = 101;

    public RequestHandle GetShouyeTuijianData(OnDataListener onDataListener, String yeshu, PullToRefreshLayout pullToRefreshLayout, int requsetType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        hashMap.put("页数", yeshu);

        return HttpManager.doPost(TAG_SHOP_C_RECOMMEND, getParams("shop_hp_recommendC", hashMap), onDataListener, -1, pullToRefreshLayout, requsetType);
    }

    //收藏
    public final static int TAG_SHOUCANG = 102;

    public RequestHandle ShouCangShangpin(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品id", id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("收藏", e.getKey() + "-" + e.getValue());
        }
        return HttpManager.doPost(TAG_SHOUCANG, getParams("shop_hpt_shoucanglist_add_p", hashMap), onDataListener, 0);
    }

    //取消收藏
    public final static int TAG_QUXIAOSHOUCANG = 103;

    public RequestHandle QuXiaoShouCangShangpin(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品id", id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("取消收藏", e.getKey() + "-" + e.getValue());
        }
        return HttpManager.doPost(TAG_QUXIAOSHOUCANG, getParams("shop_hpt_shoucanglist_one_d", hashMap), onDataListener, 0);
    }

    //获取首页收藏列表
    public final static int TAG_GEISHOUCANG = 104;

    public RequestHandle GetShouyeShouCang(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取收藏列表", e.getKey() + "-" + e.getValue());
        }
        return HttpManager.doPost(TAG_GEISHOUCANG, getParams("shop_hp_shoucanglistA", hashMap), onDataListener, -1);
    }

    //搜索_自动补全
    public static final int TAG_SHOP_HP_ATUOHINT = 105;

    public RequestHandle shop_hp_atuohint(OnDataListener onDataListener, String str) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("搜索", str);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        return HttpManager.doPost(TAG_SHOP_HP_ATUOHINT, getParams("shop_hp_atuohint", hashMap), onDataListener, -1);
    }

    //获取分类数据
    public final static int TAG_SHOUYE_LIEBIAO = 106;

    public RequestHandle getLieBiaoData(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", "");
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取分类数据", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOUYE_LIEBIAO, getParams("shop_hp_lbTiaomu_p", hashMap), onDataListener, -1);
    }

    //获取分类商品数据
    public final static int TAG_SHOUYE_SHNAGPIN_LIST = 107;

    public RequestHandle getFenLei_ShangPinData(OnDataListener onDataListener, String data) {
        Log.e("---", "----");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("分类", data);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取分类商品数据", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOUYE_SHNAGPIN_LIST, getParams("shop_c_lblistsC", hashMap), onDataListener, -1);
    }

    //分类Son，获取分类里面商品数据
    public static final int TAG_SHOP_HP_SPFL = 108;

    public RequestHandle shop_hp_spfl(OnDataListener onDataListener, String condition, String fenlei, String yeshu, String yijiFenLei) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("condition", condition);
        hashMap.put("分类", fenlei);
        hashMap.put("页数", yeshu);
        hashMap.put("一级分类", yijiFenLei);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        return HttpManager.doPost(TAG_SHOP_HP_SPFL, getParams("shop_hp_spflA", hashMap), onDataListener, -1);
    }

    //搜索
    public final static int TAG_GETSOUSUODATA = 109;

    public RequestHandle getSouSuoData(OnDataListener onDataListener, String condition, String neirong, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("搜索", neirong);
        hashMap.put("condition", condition);
        hashMap.put("页数", yeshu);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("搜索", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_GETSOUSUODATA, getParams("shop_hp_searchsB", hashMap), onDataListener, -1);
    }

    //获取特价/精品，特卖/新品
    public final static int TAG_SHOP_HP_CHOICE = 110;

    public RequestHandle GetJinRiTeJiaInfo(OnDataListener onDataListener, String condition, String leixing, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("condition", condition);
        hashMap.put("商品分类", leixing);
        hashMap.put("页数", yeshu);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取特价/精品，特卖/新品", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_HP_CHOICE, getParams("shop_hp_zoneA", hashMap), onDataListener, -1);
    }

    //添加购物车
    public final static int TAG_ADDGOUWUCHE = 111;

    public RequestHandle AddGouWuChe(OnDataListener onDataListener, String id, String yanse, String chicun, String guige, String jiage, String shuliang, String kucun, String kuaidifei) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品id", id);
        if (guige.isEmpty()) {
            hashMap.put("颜色", yanse);
            hashMap.put("尺寸", chicun);
        } else {
            hashMap.put("规格", guige);
        }
        hashMap.put("单价", jiage);
        hashMap.put("商品数量", shuliang);
        hashMap.put("库存", kucun);
        hashMap.put("快递费", kuaidifei);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));


        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("添加购物车", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_ADDGOUWUCHE, getParams("shop_t_pushA_p", hashMap), onDataListener, 0);
    }

    //商品详情
    public final static int TAG_SHANGPINGDETAIL = 112;

    public RequestHandle GetShangpinXiangqing(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("商品详情", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHANGPINGDETAIL, getParams("shop_t_detailsE_newA", hashMap), onDataListener, 0);
    }

    //获取商品详情颜色，码数，等；
    public final static int TAG_SHANGPINGDATA = 113;

    public RequestHandle GetShangpingData(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品id", id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取商品详情颜色，码数，等；", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHANGPINGDATA, getParams("shop_t_buyingC", hashMap), onDataListener, -1);
    }

    //发货地
    public final static int TAG_SHOP_KF_ADDRESS = 114;

    public RequestHandle shop_kf_address(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("发货地", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_KF_ADDRESS, getParams("shop_hpt_address", hashMap), onDataListener, -1);
    }

    //我的钱包——商城
    public final static int TAG_SHOP_C_WALLET = 115;

    public RequestHandle shop_c_wallet(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        return HttpManager.doPost(TAG_SHOP_C_WALLET, getParams("shop_t_wallet_p", hashMap), onDataListener, -1);
    }

    //生成订单(单个)
    public final static int TAG_SHOP_KF_ADD = 116;

    public RequestHandle shop_kf_add(OnDataListener onDataListener, String zhanghuType, ShangpinBean shangpin, String yanse, String chicun, String guige, String shouhuoren, String dizhi, String shoujihao, String jiesuanjia, String youhuiquanID, String youhuiquanJinE,
                                     String note) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("收货人", shouhuoren);
        hashMap.put("收货地址", dizhi);
        hashMap.put("收货人手机号", shoujihao);
        hashMap.put("商品id", shangpin.getShangpin_id());
        hashMap.put("商品数量", shangpin.getShangpin_shuliang());
        hashMap.put("version", "2.3.0");
        hashMap.put("抵扣账户", zhanghuType);

        //备注
        hashMap.put("note", note);

        if (youhuiquanID != null && youhuiquanJinE != null) {
            hashMap.put("优惠券id", youhuiquanID);
            hashMap.put("优惠金额", youhuiquanJinE);
        }

        //账户抵用
        if (!"".equals(zhanghuType)) {
            hashMap.put("points", 1);
        } else {
            hashMap.put("points", 0);
        }

        if (guige.length() == 0) {
            hashMap.put("颜色", yanse);
            hashMap.put("尺寸", chicun);
        } else {
            hashMap.put("规格", guige);
        }

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("确认订单 单个", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_KF_ADD, getParams("shop_t_buyshopF", hashMap), onDataListener, 0);
    }


    //确认订单(购物车)
    public final static int TAG_SHOP_C_CARTPAY = 117;

    public RequestHandle shop_c_cartPay(OnDataListener onDataListener, ArrayList<ShangpinBean> alldingdan, String shouhuoren, String dizhi, String shoujihao,
                                        String youhuiquanID, String youhuiquanJinE, String Shangjia, String zhangHuDiYong, String node) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONArray jSONArrayShangpin = new JSONArray();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        for (int i = 0; i < alldingdan.size(); i++) {
            JSONObject jsonobj = new JSONObject();

            try {
                //描述jsonArray组建
                jsonobj.put("id", alldingdan.get(i).getId());
                jSONArrayShangpin.put(jsonobj);
                hashMap.put("id", URLEncoder.encode(jSONArrayShangpin.toString(), "UTF-8"));
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        hashMap.put("收货人", shouhuoren);
        hashMap.put("收货地址", dizhi);
        hashMap.put("收货人手机号", shoujihao);
        hashMap.put("抵扣账户", zhangHuDiYong);

        //账户抵用
        if (!"".equals(zhangHuDiYong)) {
            hashMap.put("积分抵扣", "是");
        } else {
            hashMap.put("积分抵扣", "否");
        }

        //买家留言
        hashMap.put("买家留言", node);

        hashMap.put("version", "2.4.0");

        if (youhuiquanID != null && youhuiquanJinE != null && Shangjia != null) {
            hashMap.put("优惠券id", youhuiquanID);
            hashMap.put("优惠金额", youhuiquanJinE);
        }
        if (Shangjia != null) {
            hashMap.put("商家账号", Shangjia);
        } else {
            hashMap.put("商家账号", "");
        }

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("确认订单", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_C_CARTPAY, getParams("shop_hp_cartPayE", hashMap), onDataListener, 0);
    }


    //删除发货地
    public final static int TAG_SHOP_KF_SC = 118;

    public RequestHandle shop_kf_sc(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("id", id);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("删除发货地", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_KF_SC, getParams("shop_hpt_address_d", hashMap), onDataListener, 0);
    }

    //添加 修改发货地
    public final static int TAG_SHOP_KF_ADDUP = 120;

    public RequestHandle shop_kf_addup(OnDataListener onDataListener, DizhiBean dizhi) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("地址", dizhi.getDizhi());
        hashMap.put("收货人", dizhi.getShouhuoren());
        hashMap.put("手机号", dizhi.getShoujihao());
        hashMap.put("地状态", dizhi.getZhuangtai());
        if (!dizhi.getId().equals("")) {
            hashMap.put("id", dizhi.getId());
        }

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("添加 修改 发货地", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_KF_ADDUP, getParams("shop_hpt_address_add", hashMap), onDataListener, 0);
    }

    //网页支付（全球物联网）
    public final static int TAG_SHOP_PAY = 121;

    public RequestHandle shop_pay(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("orderid", id);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("下单支付", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_PAY, getParams("shop_place_s", hashMap), onDataListener, 0);
    }

    //网页支付（全球物联网）
    public final static int TAG_SHOP_REPAY = 121;

    public RequestHandle shop_repay(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("orderid", id);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("下单支付", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_REPAY, getParams("shop_replace_s", hashMap), onDataListener, 0);
    }

    //购物车
    public final static int TAG_SHOPLISTS = 122;

    public RequestHandle shop_c_clists(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("购物车列表", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOPLISTS, getParams("shop_hp_clists_p_new", hashMap), onDataListener, -1);
    }

    //购物车删除
    public final static int TAG_SHOP_C_DELETE = 123;

    public RequestHandle shop_c_delete(OnDataListener onDataListener, ArrayList<String> ids) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < ids.size(); i++) {
            JSONObject jsonobj = new JSONObject();
            try {
                //描述jsonArray组建
                jsonobj.put("id", ids.get(i));
                jsonArray.put(jsonobj);
                hashMap.put("id", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("购物车删除", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_C_DELETE, getParams("shop_hp_clists_d", hashMap), onDataListener, 0);
    }

    //购物车加减
    public final static int TAG_SHOP_C_CARTNUM = 124;

    public RequestHandle shop_c_cartNum(OnDataListener onDataListener, String id, String shuliang) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("id", id);
        hashMap.put("数量", shuliang);


        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("购物车加减", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_C_CARTNUM, getParams("shop_hp_cartNum", hashMap), onDataListener, 0);
    }

    // 查看物流shop_kuaidi_query
    public final static int TAG_CHAKANWULIU = 125;

    public RequestHandle shop_kuaidi_query(OnDataListener onDataListener, String id, String shangPiId, String wuliuLeixing, String dingdanID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("id", id);
        hashMap.put("商品id", shangPiId);
        hashMap.put("物流类型", wuliuLeixing);

        //消息模块进去查看物流不需要订单销售id
        hashMap.put("订单销售id", dingdanID);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("查看物流", e.getKey() + "-" + e.getValue());
        }
        return HttpManager.doPost(TAG_CHAKANWULIU, getParams("shop_kuaidi_queryC", hashMap), onDataListener, 0);
    }

    //我的订单
    public final static int TAG_WODEDINGDAN_QUANBU = 126;
    public final static int TAG_WODEDINGDAN_DAIFUKUAN = 127;
    public final static int TAG_WODEDINGDAN_DAIFAHUO = 128;
    public final static int TAG_WODEDINGDAN_DAISHOUHUO = 129;
    public final static int TAG_WODEDINGDAN_DAIPINGJIA = 130;

    public RequestHandle shop_wodedingdan(OnDataListener onDataListener, String dingdanzhuangtai, String yeshu, PullToRefreshLayout pullToRefreshLayout, int requsetType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("订单状态", dingdanzhuangtai);
        hashMap.put("页数", yeshu);
        hashMap.put("version", "2.2.0");

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("我的订单", e.getKey() + "=" + e.getValue());
        }

        int intInfo = 0;
        switch (dingdanzhuangtai) {
            case "全部":
                intInfo = TAG_WODEDINGDAN_QUANBU;
                break;
            case "待付款":
                intInfo = TAG_WODEDINGDAN_DAIFUKUAN;
                break;
            case "待发货":
                intInfo = TAG_WODEDINGDAN_DAIFAHUO;
                break;
            case "待收货":
                intInfo = TAG_WODEDINGDAN_DAISHOUHUO;
                break;
            case "待评价":
                intInfo = TAG_WODEDINGDAN_DAIPINGJIA;
                break;
        }
        Log.e("intInfo", intInfo + "");

        return HttpManager.doPost(intInfo, getParams("shop_oInfo_ordersG_twoC", hashMap), onDataListener, -1, pullToRefreshLayout, requsetType);
    }

    //取消订单
    public final static int TAG_DINGDANCAOZUO_QUXIAODINGDAN = 131;

    public RequestHandle shop_wodedingdan_quxiao(OnDataListener onDataListener, String dingdanid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("订单id", dingdanid);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("取消订单", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_DINGDANCAOZUO_QUXIAODINGDAN, getParams("shop_hpt_order_cancle_p", hashMap), onDataListener, 0);
    }

    // 确认收货
    public final static int TAG_SHOP_QUERENSHOUHUO = 132;

    public RequestHandle queRenShouHuo(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("订单id", id);

        return HttpManager.doPost(TAG_SHOP_QUERENSHOUHUO, getParams("shop_hpt_order_confirm", hashMap), onDataListener, 0);
    }

    //订单详情
    public final static int TAG_SHOP_C_ORDERSINFO = 133;

    public RequestHandle shop_c_ordersInfo(OnDataListener onDataListener, String zhuangtai, String id, String shangPinArray) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("订单状态", zhuangtai);
        hashMap.put("订单id", id);
        hashMap.put("version", "2.2.0");

        try {
            hashMap.put("订单销售id", URLEncoder.encode(shangPinArray, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("订单详情", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_C_ORDERSINFO, getParams("shop_oInfo_orderInfoE", hashMap), onDataListener, -1);
    }

    //商品评价
    public final static int TAG_SHOP_C_COMMENT = 134;

    public RequestHandle FabuPingjia(OnDataListener onDataListener, JSONArray jsonArray, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));


        L.e("type", type);
        String FunStr;
        if ("追加评价".equals(type)) {
            //追加评价
            FunStr = "shop_oInfo_comment_addto";
        } else {
            //第一次评价
            FunStr = "shop_oInfo_comment_p";
            hashMap.put("version", "2.3.0"); //这个版本号用于区别评价时带start的情况
        }

        try {
            hashMap.put("comsend", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return HttpManager.doPost(TAG_SHOP_C_COMMENT, getParams(FunStr, hashMap), onDataListener, 0);
    }

    //收藏商品的删除
    public final static int TAG_WODESHOUCANG_SHANCHU = 135;

    public RequestHandle shop_wodeshoucang_delete(OnDataListener onDataListener, List<String> list) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (int i = 0; i < list.size(); i++) {
            jsonObject = new JSONObject();
            try {
                jsonObject.put("id", list.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        try {
            hashMap.put("id", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("我的收藏删除", e.getKey() + "=" + e.getValue());
        }
        return HttpManager.doPost(TAG_WODESHOUCANG_SHANCHU, getParams("shop_hpt_shoucanglist_d", hashMap), onDataListener, 0);
    }

//    //我的收藏
//    public final static int TAG_WODESHOUCANG_SHOUCANGLIEBIAO = 136;
//
//    public RequestHandle shop_wodeshoucang(OnDataListener onDataListener, String yeshu, PullToRefreshLayout pullToRefreshLayout, int requsetType) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
//
//        hashMap.put("页数", yeshu);
//
//        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
//        for (Map.Entry<String, Object> e : entry) {
//            Log.e("我的收藏", e.getKey() + "=" + e.getValue());
//        }
//        return HttpManager.doPost(TAG_WODESHOUCANG_SHOUCANGLIEBIAO, getParams("shop_hp_shoucanglistB", hashMap), onDataListener, -1, pullToRefreshLayout, requsetType);
//    }

    //我的收藏
    public final static int TAG_WODESHOUCANG_SHOUCANGLIEBIAO = 136;

    public RequestHandle shop_wodeshoucang(OnDataListener onDataListener, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("页数", yeshu);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("我的收藏", e.getKey() + "=" + e.getValue());
        }
        return HttpManager.doPost(TAG_WODESHOUCANG_SHOUCANGLIEBIAO, getParams("shop_hp_shoucanglistC", hashMap), onDataListener, 0);
    }

    //商品——用户评价列表  shop_sp_appraise-->shop_sh_recomments
    public static final int TAG_SHOP_SP_APPRAISE = 137;

    public RequestHandle shop_sp_appraise(OnDataListener onDataListener, String id, String yeshu, PullToRefreshLayout pullToRefreshLayout, int requsetType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("商品id", id);
        hashMap.put("页数", yeshu);

        return HttpManager.doPost(TAG_SHOP_SP_APPRAISE, getParams("shop_sh_recomments", hashMap), onDataListener, -1, pullToRefreshLayout, requsetType);
    }


    //一键同步数据
    public final static int TAG_ONECLICKSYNC = 39;

    public RequestHandle OneClickSync(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_ONECLICKSYNC, getParams("p_transferData", hashMap), onDataListener, 0);
    }


    //获取生活模块数据
    public final static int TAG_GETLIFEDATA = 40;

    public RequestHandle getLifeData(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("radio", "生活");
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETLIFEDATA, getParams("things_hp_lifeB", hashMap), onDataListener, -1, pullToRefreshLayout, requestType);
    }

    //手机动态登录p_quickLanding->p_quickLanding_two
    public final static int TAG_SHOUJIDONGTAIDENGLU = 137;

    public RequestHandle getDongTaiDengLu(OnDataListener onDataListener, String shoujihao, String yanzhengma) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", shoujihao);
        hashMap.put("验证码", yanzhengma);
        hashMap.put("随机码", MD5Utlis.Md5("p_quickLanding_Version2.0.0"));
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOUJIDONGTAIDENGLU, getParams("p_quickLanding_Version2.0.0", hashMap), onDataListener, 0);
    }

    //获取我的二维码
    public final static int TAG_GETMYQRCODE = 138;

    public RequestHandle GetMyQRcode(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETMYQRCODE, getParams("p_get_QR_Version2.0.0", hashMap), onDataListener, 0);
    }

    //我的钱包——商金币，积分宝
    public final static int TAG_SHOP_C_WALLETINFO = 139;

    public RequestHandle shop_wodejifen_zhanghu(OnDataListener onDataListener, String zhanghu, String yeshu, PullToRefreshLayout pullToRefreshLayout, int requestType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("账户", zhanghu);
        hashMap.put("页数", yeshu);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("我的钱包", e.getKey() + "=" + e.getValue());
        }
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_C_WALLETINFO, getParams("shop_t_walletinfo_three", hashMap), onDataListener, -1, pullToRefreshLayout, requestType);
    }

    //核对支付密码是否正确p_verifyPassword->p_verifyPassword_two
    public final static int TAG_VERIFYPAYPWD = 140;

    public RequestHandle VerifyPaypwd(OnDataListener onDataListener, String weiyiID, String paypwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("唯一id", weiyiID);
        hashMap.put("支付密码", paypwd);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_VERIFYPAYPWD, getParams("p_verifyPassword_Version2.0.0", hashMap), onDataListener, 0);
    }


    //更换关联手机号码p_updatePhone->p_updatePhone_two
    public final static int TAG_UPDATEPHONENUM = 141;

    public RequestHandle UpdatePhoneNum(OnDataListener onDataListener, String phonenum, String yzm) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", phonenum);
        hashMap.put("验证码", yzm);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_UPDATEPHONENUM, getParams("p_updatePhone_Version2.0.0", hashMap), onDataListener, 0);
    }

    //获取我的页面——订单数量
    public final static int TAG_SHOP_ORDER_PROMPT = 142;

    public RequestHandle Shop_order_prompt(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_SHOP_ORDER_PROMPT, getParams("shop_order_prompt", hashMap), onDataListener, -1);
    }

    //获取收款记录p_transfer_record->p_transfer_record_two
    public RequestHandle GetShoukuanJilu(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requestType,
                                         int page, int requesTag, int showLoad) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (page > 0) {
            hashMap.put("页数", page + "");
        } else {
            hashMap.put("页数", "");
        }
        hashMap.put("收款唯一id", sp.getString(ConstantUtlis.SP_ONLYID, ""));
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(requesTag, getParams("p_transfer_record_Version2.0.0", hashMap), onDataListener, showLoad, pullToRefreshLayout, requestType);
    }

    public final static int TAG_ZHIFUMIMAYANZHENG = 142;

    public RequestHandle GetZhiFuMiMaYanZheng(OnDataListener onDataListener, String pwd, String weiyiID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("支付密码", pwd);
        hashMap.put("唯一id", weiyiID);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_ZHIFUMIMAYANZHENG, getParams("p_verifyPassword_Version2.0.0", hashMap), onDataListener, 0);
    }


    //收货地址，type取值：add/del/edit/show.接口作者：叶华祥
    public final static int TAG_SHOUHUOADDRESS = 143;

    public RequestHandle ShouhuoAddress(OnDataListener onDataListener, ShouhuoAddress address, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", type);
        switch (type) {
            case "show":
                break;
            case "del":
                hashMap.put("id", address.getId());
                break;
            case "add":
                hashMap.put("省", address.getProvince());
                hashMap.put("市", address.getCity());
                hashMap.put("区", address.getDistrict());
                hashMap.put("收货人", address.getShouhuoren());
                hashMap.put("默认地址", address.getDefaultAddress());
                hashMap.put("详细地址", address.getDetailedAddress());
                hashMap.put("手机号", address.getPhoneNum());
                break;
            case "edit":
                hashMap.put("id", address.getId());
                hashMap.put("省", address.getProvince());
                hashMap.put("市", address.getCity());
                hashMap.put("区", address.getDistrict());
                hashMap.put("收货人", address.getShouhuoren());
                hashMap.put("默认地址", address.getDefaultAddress());
                hashMap.put("详细地址", address.getDetailedAddress());
                hashMap.put("手机号", address.getPhoneNum());
                break;
        }
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOUHUOADDRESS, getParams("shop_adr_send", hashMap), onDataListener, 0);
    }

    //获取快递费——单个商品
    public final static int TAG_SHOP_T_BUYSHOPB_EXFEE = 144;

    public RequestHandle shop_t_buyshopB_exfee(OnDataListener onDataListener, String id, String count,
                                               String ColorStr, String SizeStr, String guiGeStr) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        hashMap.put("id", id);
        hashMap.put("count", count);
        hashMap.put("version", "2.3.0");

        if (guiGeStr.length() == 0) {
            hashMap.put("颜色", ColorStr);
            hashMap.put("尺寸", SizeStr);
        } else {
            hashMap.put("规格", guiGeStr);
        }

        mLog(hashMap);

        return HttpManager.doPost(TAG_SHOP_T_BUYSHOPB_EXFEE, getParams("shop_t_buyshop_total", hashMap), onDataListener, -1);
    }

    //获取快递费——购物车
    public final static int TAG_SHOP_T_EXFREE = 145;

    public RequestHandle shop_t_exfree(OnDataListener onDataListener, JSONArray jsonArray, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        hashMap.put("type", type);

        hashMap.put("version", "2.3.0");
        try {
            hashMap.put("id", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mLog(hashMap);

        return HttpManager.doPost(TAG_SHOP_T_EXFREE, getParams("shop_new_exfreeB", hashMap), onDataListener, -1);
    }


    //我的钱包——油费账户，积分宝p_oilCostRecord->p_oilCostRecord_two
    public final static int TAG_SHOP_C_OILCOST = 146;

    public RequestHandle shop_youfei_zhanghu(OnDataListener onDataListener, String zhanghu, String yeshu, PullToRefreshLayout pullToRefreshLayout, int requestType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("帐号", zhanghu);
        hashMap.put("页数", yeshu);
        hashMap.put("账户", "油费");


        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("我的钱包", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_C_OILCOST, getParams("p_oilCostRecord_Version2.0.0", hashMap), onDataListener, -1, pullToRefreshLayout, requestType);
    }

    // 获取用户信息
    public final static int TAG_GETUSERINFO = 147;

    public RequestHandle GetUserInfo(OnDataListener onDataListener, String userId, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", userId);
        hashMap.put("type", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETUSERINFO, getParams("shop_hp_Info", hashMap), onDataListener, 0);
    }

    public final static int TAG_REMINDER = 148;

    // 物流信息
    public RequestHandle shop_oinfo_reminder(OnDataListener onDataListener, String orderId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderId", orderId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_REMINDER, getParams("shop_oinfo_reminder", hashMap), onDataListener, 0);
    }


    // 判断超级商家是否为空
    public final static int TAG_RETURNADDRESS = 149;

    public RequestHandle p_return_address(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RETURNADDRESS, getParams("p_return_address_Version2.0.0", hashMap), onDataListener, -1);
    }


    // 积分充值备注
    public final static int TAG_JIFENCHONGZHIBEIZHU = 150;

    public RequestHandle getJFNote(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_JIFENCHONGZHIBEIZHU, getParams("p_jifen_remark_Version2.0.0", hashMap), onDataListener, 0);
    }

    // 查询所有国家
    public final static int TAG_P_COUNTRYCODE_THREE = 151;

    public RequestHandle p_countryCode_three(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_P_COUNTRYCODE_THREE, getParams("p_countryCode_Version2.0.0", hashMap), onDataListener, 0);
    }

    // 海外注册
    public final static int TAG_OVERSEAS_REGISTER = 152;

    public RequestHandle Overseas_Register(OnDataListener onDataListener,
                                           String Phone, String Code, String ZhangHao, String psw, String psw_config) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", "");
//        hashMap.put("国家码", countryCode);
        hashMap.put("手机号", Phone);
        hashMap.put("验证码", Code);
        hashMap.put("账号", ZhangHao);
        hashMap.put("密码", psw);
        hashMap.put("确认密码", psw_config);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_OVERSEAS_REGISTER, getParams("p_overseas_register_NewVersion", hashMap), onDataListener, 0);
    }

    //获取全球购物
    public final static int TAG_SHOP_QUANQIU = 161;

    public RequestHandle GetQuanQiuGouInfo(OnDataListener onDataListener, String condition, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("condition", condition);
        hashMap.put("页数", yeshu);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取特价/精品，特卖/新品", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_QUANQIU, getParams("shop_hp_globalbuying", hashMap), onDataListener, -1);
    }

    //海外购生成订单(单个)
    public final static int TAG_SHOP_T_BUYSHOPQ = 151;

    public RequestHandle shop_t_buyshopQ(OnDataListener onDataListener, String shangpinid, String shangpinNum, String yanse, String chicun, String guige, String shouhuoren, String dizhi, String shoujihao, String shengfenID, String picture, String youhuiquanID, String youhuiquanJinE, String Shangjia,
                                         String zhanghuType, String note) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        hashMap.put("收货人", shouhuoren);
        hashMap.put("收货地址", dizhi);
        hashMap.put("收货人手机号", shoujihao);
        hashMap.put("商品id", shangpinid);
        hashMap.put("商品数量", shangpinNum);
        hashMap.put("idNumber", shengfenID);
        hashMap.put("抵扣账户", zhanghuType);
        if (guige.length() == 0) {
            hashMap.put("颜色", yanse);
            hashMap.put("尺寸", chicun);
        } else {
            hashMap.put("规格", guige);
        }

        if (youhuiquanID != null && youhuiquanJinE != null) {
            hashMap.put("优惠券id", youhuiquanID);
            hashMap.put("优惠金额", youhuiquanJinE);
        }
        if (Shangjia != null) {
            hashMap.put("商家账号", Shangjia);
        } else {
            hashMap.put("商家账号", "");
        }

        //账户抵用
        if (!"".equals(zhanghuType)) {
            hashMap.put("points", 1);
        } else {
            hashMap.put("points", 0);
        }
        //买家备注
        hashMap.put("note", note);

        hashMap.put("version", "2.3.0");

        hashMap.put("picture", picture);

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("全球购确认订单 单个", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_T_BUYSHOPQ, getParams("shop_t_buyshopQ", hashMap), onDataListener, 0);
    }

    //海外购确认订单(购物车)
    public final static int TAG_SHOP_HP_CARTPAYA_EARTH = 162;

    public RequestHandle shop_hp_cartPayA_earth(OnDataListener onDataListener, ArrayList<ShangpinBean> alldingdan, String shouhuoren, String dizhi, String shoujihao, String id, String picture,
                                                String youhuiquanID, String youhuiquanJinE, String Shangjia, String zhangHuDiYong, String node) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONArray jSONArrayShangpin = new JSONArray();
        hashMap.put("图片", picture);
        hashMap.put("身份证号码", id);
        hashMap.put("抵扣账户", zhangHuDiYong);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        for (int i = 0; i < alldingdan.size(); i++) {
            JSONObject jsonobj = new JSONObject();

            try {
                //描述jsonArray组建
                jsonobj.put("id", alldingdan.get(i).getId());
                jSONArrayShangpin.put(jsonobj);
                hashMap.put("id", URLEncoder.encode(jSONArrayShangpin.toString(), "UTF-8"));
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        hashMap.put("收货人", shouhuoren);
        hashMap.put("收货地址", dizhi);
        hashMap.put("收货人手机号", shoujihao);

        //账户抵用
        if (!"".equals(zhangHuDiYong)) {
            hashMap.put("积分抵扣", "是");
        } else {
            hashMap.put("积分抵扣", "否");
        }
        //买家留言
        hashMap.put("买家留言", node);

        hashMap.put("version", "2.4.0");

        if (youhuiquanID != null && youhuiquanJinE != null && Shangjia != null) {
            hashMap.put("优惠券id", youhuiquanID);
            hashMap.put("优惠金额", youhuiquanJinE);
        }
        if (Shangjia != null) {
            hashMap.put("商家账号", Shangjia);
        } else {
            hashMap.put("商家账号", "");
        }

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("确认订单", e.getKey() + "=" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_HP_CARTPAYA_EARTH, getParams("shop_hp_cartPayA_earthC", hashMap), onDataListener, 0);
    }


    //帐号密码登录 作者：丁文强
    public final static int TAG_NUMACCOUNT = 162;

    public RequestHandle numAccountLogin(OnDataListener onDataListener, String account, String mima, String phoneid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", account);
        hashMap.put("密码", mima);
        hashMap.put("phoneid", phoneid);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_NUMACCOUNT, getParams("p_worn_login_Version2.0.0", hashMap), onDataListener, 0);
    }

    //客户中心
    public final static int TAG_KEHU_ZHONGXIN = 163;

    public RequestHandle KeHuZhongXin(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_KEHU_ZHONGXIN, getParams("service_center", hashMap), onDataListener, 0);
    }

    //客服列表
    public final static int TAG_SELECT_KEFU = 164;

    public RequestHandle SelectKeFu(OnDataListener onDataListener, String str) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        hashMap.put("客服组", str);
        mLog(hashMap);

        return HttpManager.doPost(TAG_SELECT_KEFU, getParams("sel_kefu_2", hashMap), onDataListener, 0);
    }

    //获取分类目录二级列表
    public final static int TAG_SHOP_FENLEISONINFO = 165;

    public RequestHandle getFenLeiSonInfo(OnDataListener onDataListener, String condition, String yijiFenLei, String erjiFenLei, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("condition", condition);
        hashMap.put("一级分类", yijiFenLei);
        hashMap.put("二级分类", erjiFenLei);
        hashMap.put("页数", yeshu);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        return HttpManager.doPost(TAG_SHOP_FENLEISONINFO, getParams("shop_hp_classify", hashMap), onDataListener, -1);
    }

    //获取分类目录二级列表
    public final static int TAG_SHOP_FENLEISONINFOF1 = 166;

    public RequestHandle getFenLeiSonInfoF1(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        return HttpManager.doPost(TAG_SHOP_FENLEISONINFOF1, getParams("shop_hp_goods_fl", hashMap), onDataListener, -1);
    }

    // 积分钱包数据p_personal_center_two-> p_personal_center_three

    public final static int TAG_GETMONEYTHREE = 187;

    public RequestHandle getMoneyThree(OnDataListener onDataListener, int requestType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETMONEYTHREE, getParams("p_personal_center_Version2.5.0", hashMap), onDataListener, requestType);
    }

    //获取可提现红利
    public final static int TAG_HONGLITXTHREE = 180;

    public RequestHandle p_hongli_tx_three(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_HONGLITXTHREE, getParams("p_Exchange_show", hashMap), onDataListener, 0);
    }

    // 退款申请，退款原因字段为：7天无理由退货 时返回后台计算好的退款金额
    public final static int TAG_RFAPPLY_MONEY = 181;
    public final static int TAG_RFAPPLY_CONFIG = 182;

    public RequestHandle shop_rf_apply(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id, String rfCause,
                                       String rfStatu, String rfMoney, String rfReason, String rfPic, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.put("退款原因", rfCause);
        hashMap.put("物流状态", rfStatu);
        hashMap.put("退款金额", rfMoney);
        hashMap.put("退款说明", rfReason);
        hashMap.put("凭证图", rfPic);
        hashMap.put("接口功能", type);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        if ("0".equals(type)) {
            return HttpManager.doPost(TAG_RFAPPLY_MONEY, getParams("shop_rf_applyA", hashMap), onDataListener, 0);
        } else {
            return HttpManager.doPost(TAG_RFAPPLY_CONFIG, getParams("shop_rf_applyA", hashMap), onDataListener, 0);
        }
    }

    // 退货申请 type:0 为查询金额  1为提交申请"
    public final static int TAG_RFAPPLYGOODS_MONEY = 183;
    public final static int TAG_RFAPPLYGOODS_CONFIG = 184;

    public RequestHandle shop_rf_apply_goods(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id, String rfCause,
                                             String rfMoney, String rfReason, String rfPic, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.put("退款原因", rfCause);
        hashMap.put("退款金额", rfMoney);
        hashMap.put("退款说明", rfReason);
        hashMap.put("凭证图", rfPic);
        hashMap.put("接口功能", type);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        if ("0".equals(type)) {
            return HttpManager.doPost(TAG_RFAPPLYGOODS_MONEY, getParams("shop_rf_apply_goodsA", hashMap), onDataListener, 0);
        } else {
            return HttpManager.doPost(TAG_RFAPPLYGOODS_CONFIG, getParams("shop_rf_apply_goodsA", hashMap), onDataListener, 0);
        }
    }

    //退款详情
    public final static int TAG_SHOPRFDETAILS = 183;

    public RequestHandle shop_rf_details(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOPRFDETAILS, getParams("shop_rf_detailsA", hashMap), onDataListener, 0);
    }

    //退货详情
    public final static int TAG_SHOPRFGOODSDETAILS = 184;

    public RequestHandle shop_rf_goods_detail(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOPRFGOODSDETAILS, getParams("shop_rf_goods_detailA", hashMap), onDataListener, 0);
    }

    //提交申请时的退款原因列表 type:  0退款 1退货
    public final static int TAG_RFYUANYIN = 185;

    public RequestHandle shop_rf_reason(OnDataListener onDataListener, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("退货", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFYUANYIN, getParams("shop_rf_reason", hashMap), onDataListener, 0);
    }

    // 无用
    //退款重新申请或撤销申请 type:1代表重新申请 0代表撤销申请
    public final static int TAG_RFEDIT = 186;

    public RequestHandle shop_rf_edit(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id, String wuliuzhuagntai, String strTuikuanYuanyin, String strTuikuanJine,
                                      String strTuikuanShuoming, String strTuikuanTupian, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.put("物流状态", wuliuzhuagntai);
        hashMap.put("退款原因", strTuikuanYuanyin);
        hashMap.put("退款金额", strTuikuanJine);
        hashMap.put("退款说明", strTuikuanShuoming);
        hashMap.put("图片", strTuikuanTupian);
        hashMap.put("type", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFEDIT, getParams("shop_rf_editA", hashMap), onDataListener, 0);
    }

    // 售后  type:申请记录/售后申请
    public final static int TAG_SHOUHOU = 187;//售后申请
    public final static int TAG_SHOUHOU_JILU = 196; //申请记录

    public RequestHandle shop_oInfo_afterInfo(OnDataListener onDataListener, String type, String page, PullToRefreshLayout pullToRefreshLayout, int requsetType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("item", type);
        hashMap.put("page", page);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        int request;
        if ("售后申请".equals(type))
            request = TAG_SHOUHOU;
        else
            request = TAG_SHOUHOU_JILU;
        return HttpManager.doPost(request, getParams("shop_oInfo_afterInfoA", hashMap), onDataListener, 0, pullToRefreshLayout, requsetType);
    }

    // 无用
    //退货重新申请或撤销申请 type:1代表重新申请 0代表撤销申请
    public final static int TAG_RFGOODSEDIT = 188;

    public RequestHandle shop_rf_goods_edit(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id, String strTuikuanYuanyin, String strTuikuanJine,
                                            String strTuikuanShuoming, String strTuikuanTupian, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.put("退款原因", strTuikuanYuanyin);
        hashMap.put("退款金额", strTuikuanJine);
        hashMap.put("退款说明", strTuikuanShuoming);
        hashMap.put("图片", strTuikuanTupian);
        hashMap.put("type", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFGOODSEDIT, getParams("shop_rf_goods_editA", hashMap), onDataListener, 0);
    }

    //申请成功详情 type:0退款成功  1 为查询买家收货信息 2用户提交退货快递信息表单  3退货成功
    public final static int TAG_RFRESULTSUCESS = 189;

    public RequestHandle shop_rf_result_success(OnDataListener onDataListener, String dingdanId, String strShangpinId, String strId, String strKuaidiGongsi,
                                                String strKuaidiDanhao, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", strShangpinId);
        hashMap.put("id", strId);
        hashMap.put("快递公司", strKuaidiGongsi);
        hashMap.put("快递单号", strKuaidiDanhao);
        hashMap.put("接口功能", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFRESULTSUCESS, getParams("shop_rf_result_successA", hashMap), onDataListener, 0);
    }

    //申请成功详情 type:0退款成功  1 为查询买家收货信息 2用户提交退货快递信息表单  3退货成功
    public final static int TAG_RFRESULTSUCESS_TUIKUAN = 217;
    public final static int TAG_RFRESULTSUCESS_INFO = 218;
    public final static int TAG_RFRESULTSUCESS_TIJIAO = 219;
    public final static int TAG_RFRESULTSUCESS_TUIHUO = 220;

    public RequestHandle shop_result_success(OnDataListener onDataListener, String dingdanId, String strShangpinId, String strId, String strKuaidiGongsi,
                                             String strKuaidiDanhao, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", strShangpinId);
        hashMap.put("id", strId);
        hashMap.put("快递公司", strKuaidiGongsi);
        hashMap.put("快递单号", strKuaidiDanhao);
        hashMap.put("接口功能", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        int fun = 0;
        switch (type) {
            case "0":
                fun = TAG_RFRESULTSUCESS_TUIKUAN;
                break;
            case "1":
                fun = TAG_RFRESULTSUCESS_INFO;
                break;
            case "2":
                fun = TAG_RFRESULTSUCESS_TIJIAO;
                break;
            case "3":
                fun = TAG_RFRESULTSUCESS_TUIHUO;
                break;
        }
        mLog(hashMap);
        return HttpManager.doPost(fun, getParams("shop_rf_result_successA", hashMap), onDataListener, 0);
    }

    //用户填写退货快递信息提交成功后，查看等待退款详情
    public final static int TAG_RFWAIT = 190;

    public RequestHandle shop_rf_wait(OnDataListener onDataListener, String dingdanId, String strShangpinId, String strId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", strShangpinId);
        hashMap.put("id", strId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFWAIT, getParams("shop_rf_waitA", hashMap), onDataListener, 0);
    }

    //查看申请退款、退货失败详情信息  0:退款 1：退货
    public final static int TAG_RFRESULTFAIL = 191;

    public RequestHandle shop_rf_result_fail(OnDataListener onDataListener, String dingdanId, String strShangpinId, String strId, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", strShangpinId);
        hashMap.put("id", strId);
        hashMap.put("接口功能", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFRESULTFAIL, getParams("shop_rf_result_failA", hashMap), onDataListener, 0);
    }

    //查看商家三天未处理售后反馈详情页面
    public final static int TAG_SHOP_RF_OVERTIME = 215;

    public RequestHandle shop_rf_overtime(OnDataListener onDataListener, String dingdanId, String strShangpinId, String strId, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", strShangpinId);
        hashMap.put("id", strId);
        hashMap.put("接口功能", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_RF_OVERTIME, getParams("shop_rf_overtime", hashMap), onDataListener, 0);
    }

    //售后撤销详情页面
    //退款：0   退货：1
    public final static int TAG_SHOP_RF_REVOKEDETAILS = 216;

    public RequestHandle shop_rf_Revokedetails(OnDataListener onDataListener, String dingdanId, String strShangpinId, String strId, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", strShangpinId);
        hashMap.put("id", strId);
        hashMap.put("接口功能", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_RF_REVOKEDETAILS, getParams("shop_rf_Revokedetails", hashMap), onDataListener, 0);
    }

    //申请退货用户未处理
    public final static int TAG_SHOP_RF_USERTIMEOUT = 217;

    public RequestHandle shop_rf_usertimeout(OnDataListener onDataListener, String dingdanId, String strShangpinId, String strId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", strShangpinId);
        hashMap.put("id", strId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_RF_USERTIMEOUT, getParams("shop_rf_usertimeout", hashMap), onDataListener, 0);
    }

    //退款退货进度
    public final static int TAG_RFGOODSRECORD = 192;

    public RequestHandle shop_rf_goods_record(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFGOODSRECORD, getParams("shop_rf_goods_record", hashMap), onDataListener, 0);
    }

    //退款进度
    public final static int TAG_RFRECORD = 193;

    public RequestHandle shop_rf_record(OnDataListener onDataListener, String dingdanId, String shangpinId, String Id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("订单id", dingdanId);
        hashMap.put("商品id", shangpinId);
        hashMap.put("id", Id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_RFRECORD, getParams("shop_rf_record", hashMap), onDataListener, 0);
    }

    //获取可用/不可用优惠券
    public final static int TAG_SHOPCOUPONUSE = 193;

    /**
     * 立即下单下单获取
     */
    public RequestHandle shop_coupon_use(OnDataListener onDataListener, String jiage, String shuliang, String shangpinID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品单价", jiage);
        hashMap.put("商品数量", shuliang);
        hashMap.put("商品id", shangpinID);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOPCOUPONUSE, getParams("shop_coupon_use", hashMap), onDataListener, 0);
    }

    /**
     * 购物车下单下单获取
     */
    public RequestHandle shop_coupon_use(OnDataListener onDataListener, JSONArray jsonArray) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            hashMap.put("购物车id", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOPCOUPONUSE, getParams("shop_coupon_use", hashMap), onDataListener, 0);
    }

    //查看所有可领优惠券
    public final static int TAG_COUPONFINDALL = 194;

    public RequestHandle shop_coupon_findAll(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        mLog(hashMap);
        return HttpManager.doPost(TAG_COUPONFINDALL, getParams("shop_coupon_findAll", hashMap), onDataListener, 0);
    }

    //一键领取优惠券
    public final static int TAG_COUPONRECEIVE = 195;

    public RequestHandle shop_coupon_receive(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        mLog(hashMap);
        return HttpManager.doPost(TAG_COUPONRECEIVE, getParams("shop_coupon_receive", hashMap), onDataListener, 0);
    }


    // 绑定关联微信
    public final static int TAG_WXBINDING = 200;

    public RequestHandle WXBinding(OnDataListener onDataListener, String wxCode) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", wxCode);
        hashMap.put("唯一id", sp.getString(ConstantUtlis.SP_ONLYID, ""));
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_WXBINDING, getParams("p_binding_weixin_Version3.0.0", hashMap), onDataListener, 0);
    }

    // 解绑关联微信
    public final static int TAG_WXUNBIND = 201;

    public RequestHandle WXUnbind(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);


        return HttpManager.doPost(TAG_WXUNBIND, getParams("p_unbind_weixin_Version3.0.0", hashMap), onDataListener, 0);
    }

    // 获取超级商家信息
    public final static int TAG_CHAOJISHANGJIAINFO = 202;

    public RequestHandle ChaoJIShangJiaInfo(OnDataListener onDataListener, String onlyID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlyId", onlyID);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_CHAOJISHANGJIAINFO, getParams("p_super_merchant_collectionInfo_Version2.0.0", hashMap), onDataListener, 0);
    }

    // 向超级商家付款
    public final static int TAG_PAYCHAOJISHANGJIA = 203;

    public RequestHandle PayChaoJIShangJia(OnDataListener onDataListener, String onlyID, String zhanghu, String money, String orderNum, String pwd, String beizhu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlyId", onlyID);
        hashMap.put("付款账户", zhanghu);
        hashMap.put("转账金额", money);
        hashMap.put("转账订单号", orderNum);
        hashMap.put("支付密码", MD5Utlis.Md5(pwd));
        hashMap.put("备注", beizhu);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_PAYCHAOJISHANGJIA, getParams("p_super_merchant_collection_Version2.0.0", hashMap), onDataListener, 0);
    }

    public final static int TAG_DETAILS = 204;

    public RequestHandle p_billing_details_three(OnDataListener onDataListener, String id, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("交易类别", type);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_DETAILS, getParams("shop_Present_record", hashMap), onDataListener, 0);
    }

    //扫码充值
    public final static int TAG_SHOP_P_FINDINDENTNUM_THREE = 205;

    public RequestHandle findIndentNum(OnDataListener onDataListener, String jiaoyiNum, String onlyid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("交易单号", jiaoyiNum);
        hashMap.put("onlyId", onlyid);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_SHOP_P_FINDINDENTNUM_THREE, getParams("p_findIndentNum_Version2.0.0", hashMap), onDataListener, -1);
    }

    //扫码充值记录  p_recharge_three
    public final static int TAG_SHOP_P_RECHARGE_THREE = 206;

    public RequestHandle p_recharge_three(OnDataListener onDataListener, PullToRefreshLayout pullToRefreshLayout, int requsetType, int page, String onlyId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("唯一id", onlyId);
        hashMap.put("页数", page);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_P_RECHARGE_THREE, getParams("p_recharge_Version2.0.0", hashMap), onDataListener, -1, pullToRefreshLayout, requsetType);
    }

    //获取首页优惠券
    public final static int TAG_SHOP_HP_SPREE = 207;

    public RequestHandle shop_hp_spree(OnDataListener onDataListener, boolean flag) {
        HashMap<String, Object> hashMap = new HashMap<>();
        //flag为true为已登录， false为未登录
        if (flag) {
            hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        } else {
            hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
            //未登录传空过去
            hashMap.put("账号", "");
            hashMap.put("随机码", "");
        }

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取首页优惠券", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_SHOP_HP_SPREE, getParams("shop_hp_spree", hashMap), onDataListener, -1);
    }

    public final static int TAG_MY_BILL = 208;

    /**
     * 我的订单数据请求-若transactionAccount传空表示查询所有---
     */
    public RequestHandle p_sel_details_three(OnDataListener onDataListener, String transactionAccount, String page) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("交易账户", transactionAccount);
        hashMap.put("页数", page);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("获取我的账单数据", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_MY_BILL, getParams("p_sel_details_Version2.5.0", hashMap), onDataListener, -1);
    }

    public final static int TAG_COMMON_PHONE = 209;

    /**
     * 判断是否是常用机型的登录验证
     */
    public RequestHandle p_authentication_three(OnDataListener onDataListener, String phone, String code, String phoneid) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", phone);
        hashMap.put("验证码", code);
        hashMap.put("phoneid", phoneid);
        hashMap.put("随机码", MD5Utlis.Md5("p_authentication_Version2.0.0"));
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        Set<Map.Entry<String, Object>> entry = hashMap.entrySet();
        for (Map.Entry<String, Object> e : entry) {
            Log.e("登录是否同一机型验证", e.getKey() + "-" + e.getValue());
        }

        return HttpManager.doPost(TAG_COMMON_PHONE, getParams("p_authentication_Version2.0.0", hashMap), onDataListener, -1);
    }

    /**
     * 发现模块
     */
    public final static int TAG_RETURN_DISCOVER = 210;

    public RequestHandle p_return_discover(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_RETURN_DISCOVER, getParams("p_return_discover_Version2.0.0", hashMap), onDataListener, -1);
    }


    /**
     * 获取快递公司
     */
    public final static int TAG_KUAIDI = 211;

    public RequestHandle kuaidi(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_KUAIDI, getParams("shop_hp_kdgs", hashMap), onDataListener, 0);
    }

    /**
     * 获取店铺商品
     */
    public final static int TAG_HP_STORE = 212;

    public RequestHandle shop_hp_store(OnDataListener onDataListener, String shangpingid, String type, int yeshu, String dianpu_id, String sousuo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品id", shangpingid);
        hashMap.put("店铺id", dianpu_id);
        hashMap.put("搜索", sousuo);
        hashMap.put("condition", type);
        hashMap.put("页数", yeshu + "");
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_HP_STORE, getParams("shop_hp_store", hashMap), onDataListener, 0);
    }

    /**
     * 获取店铺信息
     */
    public final static int TAG_HP_STOREINFO = 213;

    public RequestHandle shop_hp_storeInfo(OnDataListener onDataListener, String shangping_id, String dianpu_id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品id", shangping_id);
        hashMap.put("店铺id", dianpu_id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_HP_STOREINFO, getParams("shop_hp_storeInfo", hashMap), onDataListener, 0);
    }

    /**
     * 获取店铺等级勋章
     */
    public final static int TAG_SH_HONNR = 216;

    public RequestHandle shop_sh_honnr(OnDataListener onDataListener, String shangping_id, String dianpu_id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商品id", shangping_id);
        hashMap.put("店铺id", dianpu_id);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SH_HONNR, getParams("shop_sh_honnr", hashMap), onDataListener, 0);
    }

    /**
     * 关注店铺点
     */
    public final static int TAG_HP_FOLLOW = 214;

    public RequestHandle shop_hp_follow(OnDataListener onDataListener, String id, String is, String no, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("店铺id", id);
        hashMap.put("关注", is);
        hashMap.put("取消关注", no);
        hashMap.put("页数", yeshu);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_HP_FOLLOW, getParams("shop_hp_follow", hashMap), onDataListener, 0);
    }

    /**
     * 转让积分类型获取
     */
    public final static int TAG_P_TRANSFERINTEGRAL = 217;

    public RequestHandle p_transferIntegral(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_P_TRANSFERINTEGRAL, getParams("p_transferIntegral_trueOrfalse", hashMap), onDataListener, 0);
    }


    /**
     * 线下交易提交
     */
    public final static int TAG_SHOP_LINECODE = 215;

    public RequestHandle shop_lineCode(OnDataListener onDataListener, String orderId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderId", orderId);

        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_LINECODE, getParams("shop_lineCode", hashMap), onDataListener, 0);
    }

    /**
     * 线下交易说明
     */
    public final static int TAG_SHOP_USEREXPLAIN = 216;

    public RequestHandle shop_userExplain(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_USEREXPLAIN, getParams("shop_userExplain", hashMap), onDataListener, 0);
    }


    /**
     * 获取商家优惠券列表
     */
    public final static int TAG_SHOP_CREATE_COUPON = 218;

    public RequestHandle shop_Create_coupon(OnDataListener onDataListener, String dianpuid, String shangpinId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("店铺id", dianpuid);
        hashMap.put("商品id", shangpinId);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_CREATE_COUPON, getParams("shop_Create_coupon", hashMap), onDataListener, 0);
    }

    /**
     * 领取商家优惠券
     */
    public final static int TAG_SHOP_WL_GET_COUPONS = 219;

    public RequestHandle shop_wl_Get_coupons(OnDataListener onDataListener, String youhuiquanid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("优惠券id", youhuiquanid);
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_SHOP_WL_GET_COUPONS, getParams("shop_wl_Get_coupons", hashMap), onDataListener, 0);
    }

    //首页发现模块获取列表数据
    public static final int TAG_P_SEL_FIND = 220;

    public RequestHandle p_sel_find(OnDataListener onDataListener, String onlyID, String page, PullToRefreshLayout pullToRefreshLayout, int requsetType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        hashMap.put("onlyID", onlyID);
        hashMap.put("page", page);

        mLog(hashMap);
        return HttpManager.doPost(ConstantUtlis.SHOUYE_FAXIAN_URL, TAG_P_SEL_FIND, getParams("p_sel_find", hashMap), onDataListener, -1, pullToRefreshLayout, requsetType);
    }

    //首页发现模块点赞/取消点赞
    public static final int TAG_P_CLK_PRAISE = 221;

    public RequestHandle p_clk_praise(OnDataListener onDataListener, String onlyID, String message_id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));

        hashMap.put("onlyID", onlyID);
        hashMap.put("message_id", message_id);

        mLog(hashMap);

        return HttpManager.doPost(ConstantUtlis.SHOUYE_FAXIAN_URL, TAG_P_CLK_PRAISE, getParams("p_clk_praise", hashMap), onDataListener, 0, 10);
    }

    //历史反馈记录    历史反馈记录详情
    public static final int TAG_HISTORY_FEEDBACK = 222;

    public RequestHandle history_feedback(OnDataListener onDataListener, String page, PullToRefreshLayout pullToRefreshLayout, int requsetType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        //查询列表不需要  反馈id；
        //查询反馈详情不需要   页数
        hashMap.put("页数", page);

        mLog(hashMap);

        return HttpManager.doPost(TAG_HISTORY_FEEDBACK, getParams("history_feedback", hashMap), onDataListener, 0, pullToRefreshLayout, requsetType);
    }

    public RequestHandle history_feedback(OnDataListener onDataListener, String fankui_id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));

        //查询列表不需要  反馈id；
        //查询反馈详情不需要   页数
        hashMap.put("反馈id", fankui_id);

        mLog(hashMap);

        return HttpManager.doPost(TAG_HISTORY_FEEDBACK, getParams("history_feedback", hashMap), onDataListener, 0);
    }

    //人工客服
    public final static int TAG_SERVICE_TYPE = 223;

    public RequestHandle RenGongKeFu(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);

        return HttpManager.doPost(TAG_SERVICE_TYPE, getParams("service_type", hashMap), onDataListener, 0);
    }

    //我的支付宝列表
    public final static int TAG_SHOP_ALIACC_LIST = 224;

    public RequestHandle shop_aliacc_list(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        hashMap.put("version", "10100");
        mLog(hashMap);

        return HttpManager.doPost(TAG_SHOP_ALIACC_LIST, getParams("shop_aliacc_list", hashMap), onDataListener, 0);
    }

    //我的支付宝天添加/删除
    public final static int TAG_SHOP_ALIACC_EDIT = 225;

    public RequestHandle shop_aliacc_edit(OnDataListener onDataListener, String addOrDel,
                                          String id, String aliaccout, String name, String paypwd, String onlyId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        hashMap.put("version", "10100");
        hashMap.put("edit", addOrDel);
        hashMap.put("id", id);
        hashMap.put("支付密码", MD5Utlis.Md5(paypwd));
        hashMap.put("唯一id", onlyId);
        hashMap.put("aliaccout", aliaccout);
        hashMap.put("name", name);
        mLog(hashMap);

        return HttpManager.doPost(TAG_SHOP_ALIACC_EDIT, getParams("shop_aliacc_edit", hashMap), onDataListener, 0);
    }

    // 获取提现手续费
    public final static int TAG_GETTIXIANSHOUXUFEI = 226;

    public RequestHandle getTiXianShouXuFei(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_REQUESTINFO_JSON));
        mLog(hashMap);
        return HttpManager.doPost(TAG_GETTIXIANSHOUXUFEI, getParams("shop_tx_showList", hashMap), onDataListener, 0);
    }
}
