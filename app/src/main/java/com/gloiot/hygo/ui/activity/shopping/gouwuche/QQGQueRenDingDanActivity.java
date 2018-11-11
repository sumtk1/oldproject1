package com.gloiot.hygo.ui.activity.shopping.gouwuche;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.DiYongBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.DiYongYouHuiBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.GroupInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerenDingdanYouHuiQuanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerendingdanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShouhuoAddress;
import com.gloiot.hygo.ui.activity.shopping.Bean.SingleShangPinXiaDan;
import com.gloiot.hygo.ui.activity.shopping.dizhi.BianjiShouhuoDizhiActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hygo03 on 2017/6/16.
 */

public class QQGQueRenDingDanActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {


    @Bind(R.id.tv_queren_dingdan_mingzi)
    TextView tvQuerenDingdanMingzi;
    @Bind(R.id.tv_queren_dingdan_phone)
    TextView tvQuerenDingdanPhone;
    @Bind(R.id.iv_weizhi)
    ImageView ivWeizhi;
    @Bind(R.id.tv_queren_dingdan_dizhi)
    TextView tvQuerenDingdanDizhi;
    @Bind(R.id.iv_dingdan_qianwang)
    ImageView ivDingdanQianwang;
    @Bind(R.id.rl_queren_dingdan_youdizhi)
    RelativeLayout rlQuerenDingdanYoudizhi;
    @Bind(R.id.iv_dizhi)
    ImageView ivDizhi;
    @Bind(R.id.rl_queren_dingdan_wudizhi)
    RelativeLayout rlQuerenDingdanWudizhi;
    @Bind(R.id.et_dingdan_shengFenId)
    EditText etDingdanShengFenId;
    @Bind(R.id.rv_queren_dingdan)
    RecyclerView rvQuerenDingdan;
    @Bind(R.id.tv_queren_dingdan_price)
    TextView tvQuerenDingdanPrice;
    @Bind(R.id.btn_queren_dingdan_queren)
    Button btnQuerenDingdanQueren;
    @Bind(R.id.rl_button)
    RelativeLayout rlButton;
    @Bind(R.id.btn_dingdan_baocun)
    Button btnDingdanBaocun;
    @Bind(R.id.cb_dingdan_xieyixuanze)
    CheckBox cbDingdanXieyixuanze;
    @Bind(R.id.tv_queren_dingdan_yunfei)
    TextView tvQuerenDingdanYunfei;
    @Bind(R.id.tv_dingdan_xieyi)
    TextView tvDingdanXieyi;
    @Bind(R.id.et_queren_dingdan_beizhu)
    EditText et_queren_dingdan_beizhu;

    private ShouhuoAddress shouhuoAddress;
    private ArrayList<QuerendingdanBean> Querendingdanlist = new ArrayList<>();
    private String ColorStr, SizeStr, guiGeStr, dingdan_type, iD = "";
    private QQGSectionedExpandableLayoutHelper qqgsectionedExpandableLayoutHelper;
    private GroupInfoBean groupInfoBean;
    private ArrayList<ShangpinBean> allShangpin = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("######0.00");
    private boolean checkBox_flag = false;

    private double zhangHuDiYong = 0.00;
    //商品类型（自营/非自营）
    private String shangPin_Type = "自营";
    private String node = "";  //备注

    //用于判断获取快递费、抵扣的接口是否请求成功。
    // 请求中：0；
    // 请求成功：1；
    // 请求失败：2    （默认请求失败）
    private static int isSuccess = 2;

    private JSONArray jsonArray;
    private String diYongType = "";//抵用类型
    //抵用券
    private ArrayList<DiYongBean> diYong_list = new ArrayList<>();
    private DiYongYouHuiBean diYongYouHuiBean = new DiYongYouHuiBean();

    private String last_price;//用于保留最初值
    private String price;//最后总的价格
    private String diYongJine = "";//转换抵用金额
    private String youHuiQuanJine = "";//优惠券金额

    //优惠券
    private QuerenDingdanYouHuiQuanBean querenDingdanYouHuiQuanBean = new QuerenDingdanYouHuiQuanBean("");


    @Override
    public int initResource() {
        return R.layout.activity_qqg_querendingdan;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "确认订单");

        //收货地址接口请求
        requestHandleArrayList.add(requestAction.ShouhuoAddress(this, null, "show"));
        //获取商金币,跟我的钱包接口一样
        requestHandleArrayList.add(requestAction.shop_c_wallet(this));

        //商品集合
        Querendingdanlist = (ArrayList<QuerendingdanBean>) getIntent().getSerializableExtra("querendingdanList");

        ColorStr = getIntent().getStringExtra("颜色");
        SizeStr = getIntent().getStringExtra("尺寸");
        guiGeStr = getIntent().getStringExtra("规格");
        price = getIntent().getStringExtra("totalPrice");

        rvQuerenDingdan.setNestedScrollingEnabled(false);

        if (price != null) {
            //截取小数点后两位以前的值
            price = String.valueOf(df.format(Double.parseDouble(price)));
            tvQuerenDingdanPrice.setText("￥" + price);
        }

        last_price = price;

        //确认订单类型
        dingdan_type = getIntent().getStringExtra("类型");
        if (dingdan_type.equals("单个")) {
            isSuccess = 0;
            //获取快递费、抵用的那些接口——立即下单
            requestHandleArrayList.add(requestAction.shop_t_buyshopB_exfee(this,
                    Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_id(),
                    Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_shuliang() + "",
                    ColorStr, SizeStr, guiGeStr));

            //获取优惠券信息
            requestHandleArrayList.add(requestAction.shop_coupon_use(this,
                    Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getDanjia() + "",
                    Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_shuliang() + "",
                    Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_id()));
        } else if (dingdan_type.equals("多个")) {
            jsonArray = new JSONArray();
            for (int i = 0; i < Querendingdanlist.size(); i++) {
                for (int j = 0; j < Querendingdanlist.get(i).getToBeZhifuDingdan().size(); j++) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", Querendingdanlist.get(i).getToBeZhifuDingdan().get(j).getId() + "");
                        L.e("多个", Querendingdanlist.get(i).getToBeZhifuDingdan().get(j).getId());
                        jsonArray.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!"全球购-自营".equals(Querendingdanlist.get(i).getToBeZhifuDingdan().get(j).getShangpin_leixing()))
                        shangPin_Type = "非自营";
                }
            }

            //获取优惠券信息
            JSONArray jsonArray1 = new JSONArray();
            for (int i = 0; i < Querendingdanlist.size(); i++) {
                for (int j = 0; j < Querendingdanlist.get(i).getToBeZhifuDingdan().size(); j++) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("购物车id", Querendingdanlist.get(i).getToBeZhifuDingdan().get(j).getId() + "");

                        jsonArray1.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            requestHandleArrayList.add(requestAction.shop_coupon_use(this, jsonArray1));

            isSuccess = 0;
            //运费请求
            requestHandleArrayList.add(requestAction.shop_t_exfree(this, jsonArray, shangPin_Type));
        }

        if (dingdan_type.equals("单个")) {
            if (ColorStr.isEmpty() || SizeStr.isEmpty()) {
                Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).setShangpin_guige("规格:" + SizeStr);
            } else {
                Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).setShangpin_guige("款式:" + ColorStr + ";尺寸:" + SizeStr);
            }

        }


        //对RecyclerView添加数据
        qqgsectionedExpandableLayoutHelper = new QQGSectionedExpandableLayoutHelper(this,
                rvQuerenDingdan, 1);
        //确认下单界面
        queren_dingdan();

        //CheckBox的监听
        cbDingdanXieyixuanze.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox_flag = isChecked;
                if (isChecked) {
                    btnQuerenDingdanQueren.setEnabled(true);
                    btnQuerenDingdanQueren.setBackgroundResource(R.drawable.btn_dialog_right);
                } else {
                    btnQuerenDingdanQueren.setEnabled(false);
                    btnQuerenDingdanQueren.setBackgroundColor(Color.parseColor("#c3c3c3"));
                }
            }
        });

        etDingdanShengFenId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 18) {
                    btnDingdanBaocun.setText("保存");
                    btnDingdanBaocun.setBackgroundColor(getResources().getColor(R.color.main_color));
                } else {
                    btnDingdanBaocun.setText("保存");
                    btnDingdanBaocun.setBackgroundColor(Color.parseColor("#CFCFCF"));
                }
            }
        });


    }

    /**
     * 确认订单商品数据处理
     */
    private void queren_dingdan() {
        for (int i = 0; i < Querendingdanlist.size(); i++) {
            ArrayList<ShangpinBean> MyDaizhifu_Dingdanlist = (ArrayList<ShangpinBean>) Querendingdanlist.get(i).getToBeZhifuDingdan();
            ArrayList<ShangpinBean> ToZhifulist = new ArrayList<>();

            groupInfoBean = Querendingdanlist.get(i).getGroupInfoBean();
            double kdf = 0.00;
            for (int j = 0; j < MyDaizhifu_Dingdanlist.size(); j++) {
                if (groupInfoBean.getId().equals(String.valueOf(MyDaizhifu_Dingdanlist.get(j).getP_position()))) {
                    ToZhifulist.add(MyDaizhifu_Dingdanlist.get(j));
                    allShangpin.add(MyDaizhifu_Dingdanlist.get(j));
                }
            }

            qqgsectionedExpandableLayoutHelper.addGroup(Querendingdanlist.get(i).getGroupInfoBean(), ToZhifulist);
            qqgsectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
            qqgsectionedExpandableLayoutHelper.addZhangHuDiYong(diYongYouHuiBean);
            qqgsectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }

    private boolean flag_ZhangHuDiYong = false;

    public void ZhanghuDiyong(String diYongType, String zhangHuDiYong) {
        this.diYongType = diYongType;
        this.diYongJine = zhangHuDiYong;
        if ("".equals(youHuiQuanJine) || youHuiQuanJine == null) {
            price = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(zhangHuDiYong)));
        } else {
            price = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(zhangHuDiYong) - Double.parseDouble(youHuiQuanJine)));
        }
        tvQuerenDingdanPrice.setText(price);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            //管理收货地址
            case RequestAction.TAG_SHOUHUOADDRESS:
                JSONArray jsonArray = response.getJSONArray("列表");
                int num = jsonArray.length();
                if (num != 0) {
                    rlQuerenDingdanWudizhi.setVisibility(View.GONE);
                    rlQuerenDingdanYoudizhi.setVisibility(View.VISIBLE);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("状态").equals("默认地址")) {
                            shouhuoAddress = new ShouhuoAddress(jsonObject.getString("id"),
                                    jsonObject.getString("收货人"),
                                    jsonObject.getString("手机号"),
                                    jsonObject.getString("省"),
                                    jsonObject.getString("市"),
                                    jsonObject.getString("区"),
                                    jsonObject.getString("地址"),
                                    "是",
                                    jsonObject.getString("收货地区"));
                            tvQuerenDingdanMingzi.setText(jsonObject.getString("收货人"));
                            tvQuerenDingdanPhone.setText(jsonObject.getString("手机号"));
                            tvQuerenDingdanDizhi.setText(jsonObject.getString("收货地区") + "\n" + jsonObject.getString("地址"));
                        }
                    }

                    if (shouhuoAddress == null) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        shouhuoAddress = new ShouhuoAddress(jsonObject.getString("id"),
                                jsonObject.getString("收货人"),
                                jsonObject.getString("手机号"),
                                jsonObject.getString("省"),
                                jsonObject.getString("市"),
                                jsonObject.getString("区"),
                                jsonObject.getString("地址"),
                                "是",
                                jsonObject.getString("收货地区"));
                        tvQuerenDingdanMingzi.setText(jsonObject.getString("收货人"));
                        tvQuerenDingdanPhone.setText(jsonObject.getString("手机号"));
                        tvQuerenDingdanDizhi.setText(jsonObject.getString("收货地区") + "\n" + jsonObject.getString("地址"));
                    }
                } else {
                    rlQuerenDingdanWudizhi.setVisibility(View.VISIBLE);
                    rlQuerenDingdanYoudizhi.setVisibility(View.GONE);
                }
                break;

            case RequestAction.TAG_SHOP_T_BUYSHOPB_EXFEE:
                tvQuerenDingdanYunfei.setText("(运费：¥" + response.getString("express") + ")");
                tvQuerenDingdanYunfei.setVisibility(View.VISIBLE);

                //总价
                price = response.getString("total");
                tvQuerenDingdanPrice.setText(price);
                last_price = price;
                JSONArray jsonArray1 = response.getJSONArray("bank");
                if (jsonArray1.length() > 0) {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        DiYongBean diYongBean = new DiYongBean();
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        diYongBean.setType(jsonObject.getString("type"));
                        diYongBean.setAccount(jsonObject.getString("account"));
                        diYongBean.setOffset(jsonObject.getString("offset"));
                        diYong_list.add(diYongBean);
                    }
                    diYongYouHuiBean.setDiYong_list(diYong_list);
                    qqgsectionedExpandableLayoutHelper.addZhangHuDiYong(diYongYouHuiBean);
                }
                qqgsectionedExpandableLayoutHelper.notifyDataSetChanged();

                isSuccess = 1;
                break;
            case RequestAction.TAG_SHOP_T_EXFREE:
                //{"状态":"成功","运费":"0.01"}
                Log.e("购物车运费", response.toString());
                price = response.getString("合计");
                tvQuerenDingdanPrice.setText(price);
                last_price = price;
                tvQuerenDingdanYunfei.setText("(运费：¥" + response.getString("运费") + ")");
                tvQuerenDingdanYunfei.setVisibility(View.VISIBLE);

                JSONArray jsonArr = response.getJSONArray("bank");
                if (jsonArr.length() > 0) {
                    for (int i = 0; i < jsonArr.length(); i++) {
                        DiYongBean diYongBean = new DiYongBean();
                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        diYongBean.setType(jsonObject.getString("type"));
                        diYongBean.setAccount(jsonObject.getString("account"));
                        diYongBean.setOffset(jsonObject.getString("offset"));
                        diYong_list.add(diYongBean);
                    }
                    diYongYouHuiBean.setDiYong_list(diYong_list);
                    qqgsectionedExpandableLayoutHelper.addZhangHuDiYong(diYongYouHuiBean);
                }
                qqgsectionedExpandableLayoutHelper.notifyDataSetChanged();

                isSuccess = 1;
                break;
            case RequestAction.TAG_SHOPCOUPONUSE:
                JSONObject jsonObject = response.getJSONObject("可用优惠券");
                String info = response.toString();
                //可用优惠券数量
                if (jsonObject.getInt("条数") > 0) {
                    querenDingdanYouHuiQuanBean = new QuerenDingdanYouHuiQuanBean(jsonObject.getInt("条数") + "张可用", info);
                    qqgsectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
                } else {
                    querenDingdanYouHuiQuanBean = new QuerenDingdanYouHuiQuanBean("", info);
                    qqgsectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
                }
                qqgsectionedExpandableLayoutHelper.notifyDataSetChanged();

                break;

            case RequestAction.TAG_VERIFYPAYPWD: //身份认证成功下单
                ShangPinXiaDan();
                break;

        }
    }

    //判断快递费是否加载成功
    public static boolean getIsSuccess() {
        return isSuccess == 1;
    }

    @OnClick({R.id.rl_queren_dingdan_wudizhi, R.id.rl_queren_dingdan_youdizhi, R.id.btn_dingdan_baocun, R.id.btn_queren_dingdan_queren, R.id.tv_dingdan_xieyi})
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //选择收货地址
            case R.id.rl_queren_dingdan_youdizhi:
                if (shouhuoAddress != null) {
                    intent = new Intent(mContext, SelectShouhuoAddressActivity.class);
                    intent.putExtra("id", shouhuoAddress.getId());
                    startActivityForResult(intent, 0);
                }
                break;

            //无地址，添加地址
            case R.id.rl_queren_dingdan_wudizhi:
                Intent intent2 = new Intent(mContext, BianjiShouhuoDizhiActivity.class);
                intent2.putExtra("type", "添加");
                startActivityForResult(intent2, 1);
                break;

            //身份证号码保存按钮的状态变化
            case R.id.btn_dingdan_baocun:
                final String regx = "[0-9]{17}([0-9]|X)";
                String shengFenId = etDingdanShengFenId.getText().toString();
                if (btnDingdanBaocun.getText().equals("保存")) {
                    if (!"".equals(shengFenId)) {
                        if (shengFenId.length() == 18 && shengFenId.matches(regx)) {
                            iD = shengFenId;
                            btnDingdanBaocun.setText("编辑");
                            btnDingdanBaocun.setBackgroundColor(getResources().getColor(R.color.main_color));
                            etDingdanShengFenId.setEnabled(false);
                            MyToast.getInstance().showToast(mContext, "保存成功");
                        } else {
                            MyToast.getInstance().showToast(mContext, "格式有误");
                        }
                    } else {
                        MyToast.getInstance().showToast(mContext, "请输入身份证号码");
                    }
                } else {
                    btnDingdanBaocun.setText("保存");
                    if (etDingdanShengFenId.getText().toString().trim().length() == 18) {
                        btnDingdanBaocun.setBackgroundColor(getResources().getColor(R.color.main_color));
                        etDingdanShengFenId.setEnabled(true);
                    }
                    iD = "";
                }
                break;
            //确认按钮跳转上传身份证验证
            case R.id.btn_queren_dingdan_queren:
                node = et_queren_dingdan_beizhu.getText().toString().trim();    //备注

                if ("".equals(shouhuoAddress.getDetailedAddress())) {
                    MyToast.getInstance().showToast(mContext, "请添加收货地址");
                } else if ("".equals(iD)) {
                    MyToast.getInstance().showToast(mContext, "请输入并保存身份证号码");
                } else if (isSuccess == 0) {   //请求中
                    MyToast.getInstance().showToast(mContext, "数据请求中");
                } else if (isSuccess == 2) {  //请求失败重新请求
                    MyToast.getInstance().showToast(mContext, "数据请求中");
                    isSuccess = 0;
                    if ("单个".equals(dingdan_type)) {
                        //获取快递费、抵用的那些接口——立即下单
                        requestHandleArrayList.add(requestAction.shop_t_buyshopB_exfee(this,
                                Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_id(),
                                Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_shuliang() + "",
                                ColorStr, SizeStr, guiGeStr));
                    } else {
                        //获取快递费、抵用的那些接口——购物车下单
                        requestHandleArrayList.add(requestAction.shop_t_exfree(this, jsonArray, shangPin_Type));
                    }
                } else {
                    if (flag_ZhangHuDiYong) {
                        final String onlyID = preferences.getString(ConstantUtlis.SP_ONLYID, "");
                        DialogUtlis.customPwd(getmDialog(), "身份验证", false, new MDialogInterface.PasswordInter() {
                            @Override
                            public void callback(String data) {
                                requestHandleArrayList.add(requestAction.VerifyPaypwd(QQGQueRenDingDanActivity.this, onlyID, MD5Utlis.Md5(data)));
                            }
                        });
                    } else {
                        ShangPinXiaDan();
                    }
                }
                break;
            case R.id.tv_dingdan_xieyi:
                intent = new Intent(QQGQueRenDingDanActivity.this, WebActivity.class);
                intent.putExtra("url", ConstantUtlis.SHOP_XUZHI);
                startActivity(intent);
                break;

        }
    }

    private void ShangPinXiaDan() {
        Intent intent;
        Bundle bundle;
        if (dingdan_type.equals("单个")) {
            SingleShangPinXiaDan singleShangPinXiaDan = new SingleShangPinXiaDan();
            singleShangPinXiaDan.setAddress(shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress());
            singleShangPinXiaDan.setConsignee(shouhuoAddress.getShouhuoren());
            singleShangPinXiaDan.setConsignee_phone(shouhuoAddress.getPhoneNum());
            singleShangPinXiaDan.setColor(ColorStr);
            singleShangPinXiaDan.setSize(SizeStr);
            singleShangPinXiaDan.setSpecification(guiGeStr);
            singleShangPinXiaDan.setShengFen_ID(iD);
            singleShangPinXiaDan.setShangPin_ID(Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_id());
            singleShangPinXiaDan.setShangPin_Num(Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_shuliang() + "");
            intent = new Intent(this, ShenFenRenZhengXiaDanActivity.class);
            bundle = new Bundle();
            bundle.putString("类型", "单个");
            bundle.putSerializable("订单对象", (Serializable) singleShangPinXiaDan);
            intent.putExtra("优惠券对象", querenDingdanYouHuiQuanBean);
            intent.putExtra("ZhangHuDiYong", diYongType);
            intent.putExtra("node", node);
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
        } else if (dingdan_type.equals("多个")) {
            intent = new Intent(this, ShenFenRenZhengXiaDanActivity.class);
            bundle = new Bundle();
            bundle.putString("类型", "多个");
            bundle.putSerializable("allShangpin", (Serializable) allShangpin);
            bundle.putSerializable("shouhuoAddress", (Serializable) shouhuoAddress);
            bundle.putString("id", iD);
            intent.putExtras(bundle);
            intent.putExtra("优惠券对象", querenDingdanYouHuiQuanBean);
            intent.putExtra("ZhangHuDiYong", diYongType);
            intent.putExtra("node", node);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0://从选择收货地址返回
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.getExtras() != null) {
                            shouhuoAddress = (ShouhuoAddress) data.getSerializableExtra("addressInfo");
                            tvQuerenDingdanMingzi.setText(shouhuoAddress.getShouhuoren());
                            tvQuerenDingdanPhone.setText(shouhuoAddress.getPhoneNum());
                            tvQuerenDingdanDizhi.setText(shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress());
                        }
                    } else {
                        shouhuoAddress = null;
                        rlQuerenDingdanWudizhi.setVisibility(View.VISIBLE);
                        rlQuerenDingdanYoudizhi.setVisibility(View.GONE);
                        Log.e("data", "null   case0");
                    }
                }

                break;
            case 1://没有收货地址时，从添加收货地址页面返回
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.getExtras() != null) {
                            rlQuerenDingdanWudizhi.setVisibility(View.GONE);
                            rlQuerenDingdanYoudizhi.setVisibility(View.VISIBLE);

                            shouhuoAddress = (ShouhuoAddress) data.getSerializableExtra("addressInfo");

                            tvQuerenDingdanMingzi.setText(shouhuoAddress.getShouhuoren());
                            tvQuerenDingdanPhone.setText(shouhuoAddress.getPhoneNum());
                            tvQuerenDingdanDizhi.setText(shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress());
                        }
                    } else {
                        shouhuoAddress = null;
                        Log.e("data", "null  case1");
                    }
                }
                break;

            //我的优惠券
            case 2:
                if (data != null) {
                    querenDingdanYouHuiQuanBean = (QuerenDingdanYouHuiQuanBean) data.getSerializableExtra("Obj");
                    youHuiQuanJine = querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getJine();
                    if ("".equals(diYongJine) || diYongJine == null) {
                        price = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(youHuiQuanJine)));
                    } else {
                        price = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(diYongJine) - Double.parseDouble(youHuiQuanJine)));
                    }
                    tvQuerenDingdanPrice.setText(price);
                }

                qqgsectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
                qqgsectionedExpandableLayoutHelper.notifyDataSetChanged();

                break;

            default:
                break;

        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_SHOP_KF_ADDRESS:
                if (response.getString("状态").equals("请添加送货地址")) {
                    shouhuoAddress = null;
                    rlQuerenDingdanYoudizhi.setVisibility(View.GONE);
                    rlQuerenDingdanWudizhi.setVisibility(View.VISIBLE);
                    rlQuerenDingdanWudizhi.setOnClickListener(this);
                    MyToast.getInstance().showToast(mContext, response.getString("状态"));
                } else {
                    Log.e("状态", response.getString("状态"));
                    DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                }
                break;
//            case RequestAction.TAG_SHOP_C_CARTPAY:
//                DialogUtlis.oneBtnNormal(mContext, response.getString("状态"));
//                break;
            case RequestAction.TAG_SHOP_T_BUYSHOPQ:
                DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                break;
            //抵扣账户请求失败
            case RequestAction.TAG_SHOP_T_BUYSHOPB_EXFEE:
            case RequestAction.TAG_SHOP_T_EXFREE:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                isSuccess = 2;
                break;
            default:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                break;
        }
    }

}
