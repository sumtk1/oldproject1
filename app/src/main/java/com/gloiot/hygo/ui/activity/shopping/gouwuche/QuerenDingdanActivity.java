package com.gloiot.hygo.ui.activity.shopping.gouwuche;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.DiYongBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.DiYongYouHuiBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.GroupInfoBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerenDingdanYouHuiQuanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.QuerendingdanBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangpinBean;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShouhuoAddress;
import com.gloiot.hygo.ui.activity.shopping.dizhi.BianjiShouhuoDizhiActivity;
import com.gloiot.hygo.ui.activity.web.WebToPayManager;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


public class QuerenDingdanActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {
    @Bind(R.id.tv_queren_dingdan_mingzi)
    TextView tv_queren_dingdan_mingzi;
    @Bind(R.id.tv_queren_dingdan_phone)
    TextView tv_queren_dingdan_phone;
    @Bind(R.id.tv_queren_dingdan_dizhi)
    TextView tv_queren_dingdan_dizhi;
    @Bind(R.id.rl_queren_dingdan_youdizhi)
    RelativeLayout rl_queren_dingdan_youdizhi;
    @Bind(R.id.rl_queren_dingdan_wudizhi)
    RelativeLayout rl_queren_dingdan_wudizhi;
    @Bind(R.id.rv_queren_dingdan)
    RecyclerView rv_queren_dingdan;
    @Bind(R.id.tv_queren_dingdan_price)
    TextView tv_queren_dingdan_price;
    @Bind(R.id.tv_queren_dingdan_yunfei)
    TextView tv_queren_dingdan_yunfei;
    @Bind(R.id.btn_queren_dingdan_queren)
    Button btn_queren_dingdan_queren;
    @Bind(R.id.et_queren_dingdan_beizhu)
    EditText et_queren_dingdan_beizhu;

    ArrayList<QuerendingdanBean> Querendingdanlist = new ArrayList<>();
    private ShouhuoAddress shouhuoAddress;
    private GroupInfoBean groupInfoBean;
    private SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    private String price, dingdan_type, yanse, chicun, guige, dianpu_ming, jiesuanjia;

    private String last_price;//用于保留最初值
    private String allprice;//最后总的价格
    private String diYongJine = "";//转换抵用金额
    private String youHuiQuanJine = "";//优惠券金额

    private int shangpinshuliang = 0;
    private Context mContext;
    private ArrayList<ShangpinBean> allShangpin = new ArrayList<>();
    private ShangpinBean dange_shangpin;
    private DecimalFormat df = new DecimalFormat("######0.00");

    private double zhangHuDiYong = 0.00;
    //商品类型（自营/非自营）
    private String shangPin_Type = "自营";
    private String node = "";  //备注

    //用于判断获取快递费、抵扣的接口是否请求成功。
    // 请求中：0；
    // 请求成功：1；
    // 请求失败：2    （默认请求失败）
    private static int isSuccess = 2;

    private String ColorStr = "";
    private String SizeStr = "";
    private String guiGeStr = "";

    private JSONArray jsonArray;

    //优惠券
    private QuerenDingdanYouHuiQuanBean querenDingdanYouHuiQuanBean = new QuerenDingdanYouHuiQuanBean("");
    //抵用券
    private ArrayList<DiYongBean> diYong_list = new ArrayList<>();
    private DiYongYouHuiBean diYongYouHuiBean = new DiYongYouHuiBean();
    private String zhanghuType = "";

    @Override
    public int initResource() {
        return R.layout.activity_queren_dingdan;
    }

    @Override
    public void initData() {

        //收货地址接口请求
        requestHandleArrayList.add(requestAction.ShouhuoAddress(this, null, "show"));

        //设置请求状态不为“成功”时的接口回调
        setRequestErrorCallback(this);

        mContext = this;
        CommonUtlis.setTitleBar(this, "确认订单");

        Querendingdanlist = (ArrayList<QuerendingdanBean>) getIntent().getSerializableExtra("querendingdanList");

        ColorStr = getIntent().getStringExtra("颜色");
        SizeStr = getIntent().getStringExtra("尺寸");
        guiGeStr = getIntent().getStringExtra("规格");

        rv_queren_dingdan.setNestedScrollingEnabled(false);

        //确认订单类型
        dingdan_type = getIntent().getStringExtra("类型");
        if ("单个".equals(dingdan_type)) {
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

        } else {
            jsonArray = new JSONArray();
            for (int i = 0; i < Querendingdanlist.size(); i++) {
                for (int j = 0; j < Querendingdanlist.get(i).getToBeZhifuDingdan().size(); j++) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", Querendingdanlist.get(i).getToBeZhifuDingdan().get(j).getId() + "");

                        jsonArray.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                    if (!"自营".equals(Querendingdanlist.get(i).getToBeZhifuDingdan().get(j).getShangpin_leixing()))
                        shangPin_Type = "非自营";

                }
            }
            requestHandleArrayList.add(requestAction.shop_coupon_use(this, jsonArray1));

            isSuccess = 0;
            //获取快递费、抵用的那些接口——购物车下单
            requestHandleArrayList.add(requestAction.shop_t_exfree(this, jsonArray, shangPin_Type));
        }

        if (dingdan_type.equals("单个")) {
            yanse = getIntent().getStringExtra("颜色");
            chicun = getIntent().getStringExtra("尺寸");
            guige = getIntent().getStringExtra("规格");
            if (yanse.isEmpty() || chicun.isEmpty()) {
                Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).setShangpin_guige("规格:" + guige);
            } else {
                Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).setShangpin_guige("款式:" + yanse + ";尺寸:" + chicun);
            }
            shangpinshuliang = Querendingdanlist.get(0).getToBeZhifuDingdan().get(0).getShangpin_shuliang();
            dange_shangpin = Querendingdanlist.get(0).getToBeZhifuDingdan().get(0);
            dianpu_ming = Querendingdanlist.get(0).getGroupInfoBean().getDianpuming();
        }

        price = getIntent().getStringExtra("totalPrice");
        jiesuanjia = getIntent().getStringExtra("totalPrice");

        if (price != null) {
            //截取小数点后两位以前的值
            price = String.valueOf(df.format(Double.parseDouble(price)));
            jiesuanjia = String.valueOf(df.format(Double.parseDouble(jiesuanjia)));
        }

        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this,
                rv_queren_dingdan, 1);
        queren_dingdan();
        allprice = String.valueOf(df.format(Double.parseDouble(price)));
        last_price = allprice;
        tv_queren_dingdan_price.setText(allprice);
        requestHandleArrayList.add(requestAction.shop_kf_address(this));
    }


    private void queren_dingdan() {
        for (int i = 0; i < Querendingdanlist.size(); i++) {
            ArrayList<ShangpinBean> MyDaizhifu_Dingdanlist = (ArrayList<ShangpinBean>) Querendingdanlist.get(i).getToBeZhifuDingdan();
            ArrayList<ShangpinBean> ToZhifulist = new ArrayList<>();

            groupInfoBean = Querendingdanlist.get(i).getGroupInfoBean();
            for (int j = 0; j < MyDaizhifu_Dingdanlist.size(); j++) {
                if (groupInfoBean.getId().equals(String.valueOf(MyDaizhifu_Dingdanlist.get(j).getP_position()))) {
                    ToZhifulist.add(MyDaizhifu_Dingdanlist.get(j));
                    allShangpin.add(MyDaizhifu_Dingdanlist.get(j));
                }
            }
            sectionedExpandableLayoutHelper.addGroup(Querendingdanlist.get(i).getGroupInfoBean(), ToZhifulist);
            sectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
            sectionedExpandableLayoutHelper.addZhangHuDiYong(diYongYouHuiBean);
            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }


    public void ZhanghuDiyong(String zhangHuType, String zhangHuDiYong) {
        zhanghuType = zhangHuType;
        diYongJine = zhangHuDiYong;
        if ("".equals(youHuiQuanJine) || youHuiQuanJine == null) {
            allprice = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(zhangHuDiYong)));
        } else {
            allprice = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(zhangHuDiYong) - Double.parseDouble(youHuiQuanJine)));
        }

        tv_queren_dingdan_price.setText(allprice);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_SHOUHUOADDRESS:
                L.e("管理收货地址请求成功", ",返回数据=" + response.toString());
                JSONArray jsonArray = response.getJSONArray("列表");
                int num = jsonArray.length();
                if (num != 0) {
                    rl_queren_dingdan_youdizhi.setVisibility(View.VISIBLE);
                    rl_queren_dingdan_wudizhi.setVisibility(View.GONE);

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
                            tv_queren_dingdan_mingzi.setText(shouhuoAddress.getShouhuoren());
                            tv_queren_dingdan_phone.setText(shouhuoAddress.getPhoneNum());
                            tv_queren_dingdan_dizhi.setText(shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress());
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
                        tv_queren_dingdan_mingzi.setText(shouhuoAddress.getShouhuoren());
                        tv_queren_dingdan_phone.setText(shouhuoAddress.getPhoneNum());
                        tv_queren_dingdan_dizhi.setText(shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress());
                    }
                } else {
                    rl_queren_dingdan_youdizhi.setVisibility(View.GONE);
                    rl_queren_dingdan_wudizhi.setVisibility(View.VISIBLE);
                }
                break;
            case RequestAction.TAG_SHOP_KF_ADD:
                Log.e("单个商品", response.toString());
                if (response.getString("状态").equals("成功")) {
                    requestHandleArrayList.add(requestAction.shop_pay(this, response.getString("orderId")));
                }
                break;
            case RequestAction.TAG_SHOP_C_CARTPAY:
                if (response.getString("状态").equals("成功")) {
                    L.e("QuerenDingdanActivity", "购物车下单");
                    requestHandleArrayList.add(requestAction.shop_pay(this, response.getString("订单编号")));
                }
                break;

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

            case RequestAction.TAG_SHOP_T_BUYSHOPB_EXFEE:
                Log.e("单个商品运费", response.toString());
                tv_queren_dingdan_yunfei.setText("(运费：¥" + response.getString("express") + ")");
                tv_queren_dingdan_yunfei.setVisibility(View.VISIBLE);

                //总价
                allprice = response.getString("total");
                tv_queren_dingdan_price.setText(allprice);
                last_price = allprice;

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
                    sectionedExpandableLayoutHelper.addZhangHuDiYong(diYongYouHuiBean);
                }
                sectionedExpandableLayoutHelper.notifyDataSetChanged();

                isSuccess = 1;
                break;
            case RequestAction.TAG_SHOP_T_EXFREE:
                Log.e("购物车运费", response.toString());
                tv_queren_dingdan_yunfei.setText("(运费：¥" + response.getString("运费") + ")");
                tv_queren_dingdan_yunfei.setVisibility(View.VISIBLE);

                //总价
                allprice = response.getString("合计");
                last_price = allprice;
                tv_queren_dingdan_price.setText(allprice);
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
                    sectionedExpandableLayoutHelper.addZhangHuDiYong(diYongYouHuiBean);
                }
                sectionedExpandableLayoutHelper.notifyDataSetChanged();

                isSuccess = 1;
                break;

            case RequestAction.TAG_SHOPCOUPONUSE:
                JSONObject jsonObject = response.getJSONObject("可用优惠券");
                String info = response.toString();
                //可用优惠券数量
                if (jsonObject.getInt("条数") > 0) {
                    querenDingdanYouHuiQuanBean = new QuerenDingdanYouHuiQuanBean(jsonObject.getInt("条数") + "张可用", info);
                    sectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
                } else {
                    querenDingdanYouHuiQuanBean = new QuerenDingdanYouHuiQuanBean("", info);
                    sectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
                }
                sectionedExpandableLayoutHelper.notifyDataSetChanged();

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

    @OnClick({R.id.rl_queren_dingdan_youdizhi, R.id.rl_queren_dingdan_wudizhi, R.id.btn_queren_dingdan_queren})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_queren_dingdan_youdizhi:
                Intent intent = new Intent(mContext, SelectShouhuoAddressActivity.class);
                intent.putExtra("id", shouhuoAddress.getId());
                startActivityForResult(intent, 0);
                break;

            case R.id.rl_queren_dingdan_wudizhi:
                Intent intent2 = new Intent(mContext, BianjiShouhuoDizhiActivity.class);
                intent2.putExtra("type", "添加");
                startActivityForResult(intent2, 1);
                break;

            case R.id.btn_queren_dingdan_queren:
                //备注
                node = et_queren_dingdan_beizhu.getText().toString().trim();

                if (shouhuoAddress == null) {
                    DialogUtlis.oneBtnNormal(getmDialog(), "请填写收货地址");
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
                    if ("是".equals(preferences.getString(ConstantUtlis.SP_MYPWD, ""))) {
                        if (!"".equals(zhanghuType)) {
                            final String onlyID = preferences.getString(ConstantUtlis.SP_ONLYID, "");
                            DialogUtlis.customPwd(getmDialog(), "身份验证", false, new MDialogInterface.PasswordInter() {
                                @Override
                                public void callback(String data) {
                                    requestHandleArrayList.add(requestAction.VerifyPaypwd(QuerenDingdanActivity.this, onlyID, MD5Utlis.Md5(data)));
                                }
                            });
                        } else {
                            ShangPinXiaDan();
                        }

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

    private void ShangPinXiaDan() {

        if (dingdan_type.equals("单个")) {
            if (querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean() != null)
                requestHandleArrayList.add(requestAction.shop_kf_add(this, zhanghuType, dange_shangpin, yanse, chicun, guige, shouhuoAddress.getShouhuoren(), shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress(), shouhuoAddress.getPhoneNum(), jiesuanjia,
                        querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getYouhuiquanID(), querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getJine(), node));
            else
                requestHandleArrayList.add(requestAction.shop_kf_add(this, zhanghuType, dange_shangpin, yanse, chicun, guige, shouhuoAddress.getShouhuoren(), shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress(), shouhuoAddress.getPhoneNum(), jiesuanjia,
                        null, null, node));
        } else {
            ConstantUtlis.CHECK_ADD_GOUWUCHE = true;
            if (querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean() != null)
                requestHandleArrayList.add(requestAction.shop_c_cartPay(this, allShangpin, shouhuoAddress.getShouhuoren(), shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress(), shouhuoAddress.getPhoneNum(),
                        querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getYouhuiquanID(),
                        querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getJine(),
                        querenDingdanYouHuiQuanBean.getYouHuiQuanShiYongBean().getShanjiaZhanghao(), zhanghuType, node));
            else
                requestHandleArrayList.add(requestAction.shop_c_cartPay(this, allShangpin, shouhuoAddress.getShouhuoren(), shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress(), shouhuoAddress.getPhoneNum(),
                        null, null, null, zhanghuType, node));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode", "requestCode" + requestCode + "---resultCode" + resultCode + "--data" + data);
        switch (requestCode) {
            case 0://从选择收货地址返回
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.getExtras() != null) {
                            shouhuoAddress = (ShouhuoAddress) data.getSerializableExtra("addressInfo");
                            tv_queren_dingdan_mingzi.setText(shouhuoAddress.getShouhuoren());
                            tv_queren_dingdan_phone.setText(shouhuoAddress.getPhoneNum());
                            tv_queren_dingdan_dizhi.setText(shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress());
                        }
                    } else {
                        shouhuoAddress = null;
                        rl_queren_dingdan_youdizhi.setVisibility(View.GONE);
                        rl_queren_dingdan_wudizhi.setVisibility(View.VISIBLE);
                        Log.e("data", "null   case0");
                    }
                }

                break;
            case 1://没有收货地址时，从添加收货地址页面返回
                Log.e("data", data + "=======");
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Log.e("data01", data.getExtras() + "=======");
                        if (data.getExtras() != null) {
                            rl_queren_dingdan_wudizhi.setVisibility(View.GONE);
                            rl_queren_dingdan_youdizhi.setVisibility(View.VISIBLE);

                            shouhuoAddress = (ShouhuoAddress) data.getSerializableExtra("addressInfo");

                            tv_queren_dingdan_mingzi.setText(shouhuoAddress.getShouhuoren());
                            tv_queren_dingdan_phone.setText(shouhuoAddress.getPhoneNum());
                            tv_queren_dingdan_dizhi.setText(shouhuoAddress.getShouhuoArea() + "\n" + shouhuoAddress.getDetailedAddress());
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
                        allprice = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(youHuiQuanJine)));
                    } else {
                        allprice = String.valueOf(df.format(Double.parseDouble(last_price) - Double.parseDouble(diYongJine) - Double.parseDouble(youHuiQuanJine)));
                    }
                    tv_queren_dingdan_price.setText(allprice);
                }

                sectionedExpandableLayoutHelper.addYouHuiQuan(querenDingdanYouHuiQuanBean);
                sectionedExpandableLayoutHelper.notifyDataSetChanged();


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
                    rl_queren_dingdan_youdizhi.setVisibility(View.GONE);
                    rl_queren_dingdan_wudizhi.setVisibility(View.VISIBLE);
                    rl_queren_dingdan_wudizhi.setOnClickListener(this);
                    MyToast.getInstance().showToast(mContext, response.getString("状态"));
                } else {
                    Log.e("状态", response.getString("状态"));
                    DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                }
                break;
            case RequestAction.TAG_SHOP_C_CARTPAY:
                DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                break;
            case RequestAction.TAG_SHOP_KF_ADD:
                DialogUtlis.oneBtnNormal(getmDialog(), response.getString("状态"));
                break;

            case RequestAction.TAG_SHOP_PAY:
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

