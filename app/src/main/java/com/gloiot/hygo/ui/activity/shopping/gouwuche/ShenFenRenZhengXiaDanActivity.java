package com.gloiot.hygo.ui.activity.shopping.gouwuche;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerenDingdanYouHuiQuanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShouhuoAddress;
import com.gloiot.hygo.ui.activity.shopping.Bean.SingleShangPinXiaDan;
import com.gloiot.hygo.ui.activity.web.WebToPayManager;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.server.AliOss.MPhoto;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 实名认证第二步
 */
public class ShenFenRenZhengXiaDanActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {

    @Bind(R.id.iv_sfz_photo)
    ImageView mIvShouchi;
    @Bind(R.id.iv_sf_photo_fan)
    ImageView ivSfPhotoFan;

    private String pictrue;
    private String mPicZhengmian, mPicFanmian;
    private SingleShangPinXiaDan singleShangPinXiaDan;
    private String shenFenid;
    private ShouhuoAddress shouhuoAddress;
    private ArrayList<ShangpinBean> allShangpin = new ArrayList<>();

    private QuerenDingdanYouHuiQuanBean querenDingdanYouHuiQuanBean;
    private String  zhangHuDiYong ="";
    private String node = "";

    @Override
    public int initResource() {
        return R.layout.activity_renzheng02;
    }

    @Override
    public void initData() {

        CommonUtlis.setTitleBar(this, "上传身份证");

        querenDingdanYouHuiQuanBean = (QuerenDingdanYouHuiQuanBean) getIntent().getSerializableExtra("优惠券对象");
        zhangHuDiYong = getIntent().getStringExtra("ZhangHuDiYong");
        Log.e("ZhangHuDiYong",zhangHuDiYong);
        node = getIntent().getStringExtra("node");
        if (getIntent().getStringExtra("类型").equals("单个")) {
            //订单对象
            singleShangPinXiaDan = (SingleShangPinXiaDan) getIntent().getSerializableExtra("订单对象");
        } else if (getIntent().getStringExtra("类型").equals("多个")) {
            shenFenid = getIntent().getStringExtra("id");
            shouhuoAddress = (ShouhuoAddress) getIntent().getSerializableExtra("shouhuoAddress");
            allShangpin = (ArrayList<ShangpinBean>) getIntent().getSerializableExtra("allShangpin");
        }
    }

    @OnClick({R.id.iv_sfz_photo, R.id.btn_next, R.id.iv_sf_photo_fan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //正面照
            case R.id.iv_sfz_photo:
                photo("正");
                break;
            //反面照
            case R.id.iv_sf_photo_fan:
                photo("反");
                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(mPicZhengmian)) {
                    MyToast.getInstance().showToast(mContext, "请上传身份证正面照");
                } else if (TextUtils.isEmpty(mPicFanmian)) {
                    MyToast.getInstance().showToast(mContext, "请上传身份证反面照");
                } else {
                    if ("是".equals(preferences.getString(ConstantUtlis.SP_MYPWD, ""))) {

                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray.put(new JSONObject().put("front", mPicZhengmian));
                            jsonArray.put(new JSONObject().put("opposite", mPicFanmian));
                            //身份证照片加密
                            pictrue = URLEncoder.encode(jsonArray.toString(), "UTF-8");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (getIntent().getStringExtra("类型").equals("单个")) {
                            if (querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean() != null) {
                                requestHandleArrayList.add(requestAction.shop_t_buyshopQ(this, singleShangPinXiaDan.getShangPin_ID(), singleShangPinXiaDan.getShangPin_Num(),
                                        singleShangPinXiaDan.getColor(), singleShangPinXiaDan.getSize(), singleShangPinXiaDan.getSpecification(), singleShangPinXiaDan.getConsignee(),
                                        singleShangPinXiaDan.getAddress(), singleShangPinXiaDan.getConsignee_phone(), singleShangPinXiaDan.getShengFen_ID(), pictrue,
                                        querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getYouhuiquanID(), querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getJine(),
                                        querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getShanjiaZhanghao(), zhangHuDiYong, node));
                            } else {
                                requestHandleArrayList.add(requestAction.shop_t_buyshopQ(this, singleShangPinXiaDan.getShangPin_ID(), singleShangPinXiaDan.getShangPin_Num(),
                                        singleShangPinXiaDan.getColor(), singleShangPinXiaDan.getSize(), singleShangPinXiaDan.getSpecification(), singleShangPinXiaDan.getConsignee(),
                                        singleShangPinXiaDan.getAddress(), singleShangPinXiaDan.getConsignee_phone(), singleShangPinXiaDan.getShengFen_ID(), pictrue, null, null, null,
                                        zhangHuDiYong, node));
                            }
                        } else if (getIntent().getStringExtra("类型").equals("多个")) {
                            if (querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean() != null) {
                                String shouhuodizhi = shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress();
                                requestHandleArrayList.add(requestAction.shop_hp_cartPayA_earth(this, allShangpin, shouhuoAddress.getShouhuoren(), shouhuodizhi, shouhuoAddress.getPhoneNum(),
                                        shenFenid, pictrue, querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getYouhuiquanID(),
                                        querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getJine(), querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getShanjiaZhanghao(),
                                        zhangHuDiYong, node));
                            } else {
                                String shouhuodizhi = shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress();
                                requestHandleArrayList.add(requestAction.shop_hp_cartPayA_earth(this, allShangpin, shouhuoAddress.getShouhuoren(), shouhuodizhi, shouhuoAddress.getPhoneNum(),
                                        shenFenid, pictrue, null, null, null, zhangHuDiYong, node));

                            }
                        }
                    } else {
                        DialogUtlis.oneBtnNormal(getmDialog(), "您还未设置支付密码！", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳设置支付密码
                                Intent intent = new Intent(mContext, WangjimimaActivity.class);
                                intent.putExtra("forgetpwd", "设置支付密码");
                                startActivity(intent);
                                dismissDialog();
                            }
                        });
                    }
                }

                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            //立即购买---获取订单id
            case RequestAction.TAG_SHOP_T_BUYSHOPQ:
                requestHandleArrayList.add(requestAction.shop_pay(this, response.getString("orderId")));
                break;
            //购物车多个购买---获取订单id
            case RequestAction.TAG_SHOP_HP_CARTPAYA_EARTH:
                Log.e("hhhhh-----", response.toString());
                ConstantUtlis.CHECK_ADD_GOUWUCHE = true;
                requestHandleArrayList.add(requestAction.shop_pay(this, response.getString("订单编号")));
                break;
            //跳转网页收银台
            case RequestAction.TAG_SHOP_PAY:
                if ("成功".equals(response.getString("状态"))) {
                    if ("是".equals(preferences.getString(ConstantUtlis.SP_MYPWD, ""))) {
                        // 跳转收银台
                        startActivity(WebToPayManager.toCashier(preferences, mContext, response));
                        this.finish();
                    } else {
                        DialogUtlis.oneBtnNormal(getmDialog(), "您还未设置支付密码！", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, WangjimimaActivity.class);
                                intent.putExtra("forgetpwd", "设置支付密码");
                                startActivity(intent);
                                dismissDialog();
                            }
                        });
                    }
                }
                break;

        }
    }

    //选择图片
    public void photo(final String type) {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                MPhoto.Builder builder = new MPhoto.Builder()
                        .init(mContext)
                        .setTitle("选择照片")
                        .setResultCallback(new MPhoto.OnResultCallback() {
                            @Override
                            public void onSuccess(final String data) {
                                ShenFenRenZhengXiaDanActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (type.equals("正")) {
                                            mPicZhengmian = data;
                                            PictureUtlis.loadImageViewHolder(mContext, data, R.mipmap.ic_shenfenzheng, mIvShouchi);
                                        } else {
                                            mPicFanmian = data;
                                            PictureUtlis.loadImageViewHolder(mContext, data, R.mipmap.ic_shenfenzheng, ivSfPhotoFan);
                                        }
                                    }
                                });
                                Log.e("设置身份证成功", data);
                            }

                            @Override
                            public void onFailure(String errorMsg) {
                                Log.e("上传图片失败", errorMsg);
                            }
                        });
                MPhoto.init(builder);

            }
        }, R.string.perm_camera, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_SHOP_PAY:
                DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                break;
        }
    }
}
