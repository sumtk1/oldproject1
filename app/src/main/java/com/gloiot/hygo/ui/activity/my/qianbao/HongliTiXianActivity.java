package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.zhifubao.AddAlipayActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MD5Utlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：Ljy on 2017/3/28.
 * 功能：我的——红利提现
 */

public class HongliTiXianActivity extends BaseActivity {

    @Bind(R.id.imv_tixian_dot)
    ImageView imvTixianDdot;
    @Bind(R.id.et_honglitixian_guding)
    EditText etHonglitixianGuding;
    @Bind(R.id.tv_zongjine)
    TextView tvZongjine;
    @Bind(R.id.tv_beizhu)
    TextView tvBeizhu;
    @Bind(R.id.rl_honglitixian_yinhangka)
    RelativeLayout rlHonglitixianYinhangka;
    @Bind(R.id.tv_honglitixian_yinhangka)
    TextView tvHonglitixianYinhangka;
    @Bind(R.id.tv_honglitixian_hongbao)
    TextView tvHhonglitixianHongbao;
    @Bind(R.id.tv_ch03)
    TextView tvTixianYinhangka;
    @Bind(R.id.btn_honglitixian_tixian)
    Button btTixian;
    @Bind(R.id.iv_cz)
    ImageView imvjiantou;
    @Bind(R.id.tv_honglitixian_guding)
    TextView tvHonglitixianGuding;

    private ArrayList<String> bankInfo = new ArrayList<>();
    private List<String> bankIdList;
    private String bank;
    private String bankId;
    private String pictureUrl = "";
    private int position;
    private boolean bankIsNoNull = false; // 存储是否请求过银行卡列表
    //用于数据请求成功标识
    private boolean successInfo = false;

    //支付宝提现
    private String alipay_tiXian_fanShi = "是";
    private String alipay_gudinghongli;
    private String alipay_beizhu;
    //银行卡提现
    private String bank_tiXian_fanShi = "是";
    private String bank_gudinghongli;
    private String bank_beizhu;
    //总金额
    private String zongjine = "";

//    private String gudinghongli;
//    private String beizhu = "";
//    private String tiXian_fanShi;

    // 判断是提现方式
    private String cardType;

    @Override
    public int initResource() {
        return R.layout.activity_my_qianbao_honglitixian_ka;
    }

    @Override
    public void initData() {
        cardType = getIntent().getStringExtra("type");
        Log.e("HongliTiXian：", "---" + cardType);
        CommonUtlis.setTitleBar(this, "市场奖励提现", "提现明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HongliTiXianActivity.this, HongliTixianMingxiActivity.class));
            }
        });

        imvTixianDdot.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.bg_honglitixian_dot_blue, null));
        bankIdList = new ArrayList<>();

        //红利提现信息（可兑换红利、固定金额）
        requestHandleArrayList.add(requestAction.p_hongli_tx_three(HongliTiXianActivity.this));

        etHonglitixianGuding.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    int jine = Integer.parseInt(s.toString().trim());
                    if (jine > 50000) {
                        MyToast.getInstance().showToast(mContext, "提现金额不得超过50000");
                        etHonglitixianGuding.setText("");
                    } else if (jine <= 0) {
                        MyToast.getInstance().showToast(mContext, "提现金额必须大于0");
                        etHonglitixianGuding.setText("");
                    }
                }
            }
        });
    }

    @OnClick({R.id.rl_honglitixian_yinhangka, R.id.btn_honglitixian_tixian})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.rl_honglitixian_yinhangka:  //选择银行卡
                // 如果请求银行卡列表失败 重新请求
                if (!bankIsNoNull || !successInfo) {
                    if (!bankIsNoNull) {
                        MyToast.getInstance().showToast(mContext, "获取账户列表");
                        requestHandleArrayList.add(requestAction.BankInfo(HongliTiXianActivity.this, cardType));
                    }
                    if (!successInfo) {
                        //红利提现信息（可兑换红利、固定金额）
                        requestHandleArrayList.add(requestAction.p_hongli_tx_three(HongliTiXianActivity.this));
                    }

                    return;
                }

                //可能存在银行卡重名的情况，这里需要通过选中的position来得到银行卡的id，所以重载了oneBtnSingleChoice()方法和MyDialogBuilder中的setSingleChoice()方法\
                if (bankInfo.size() > 0) {
                    DialogUtlis.customLoopView(getmDialog(), "请选择提现类型", bankInfo, position, new MDialogInterface.LoopViewInter() {
                        @Override
                        public void getPostition(int postition) {

                            position = postition;
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if ("支付宝".equals(bankInfo.get(position))) {
                                    dismissDialog();
                                    startActivity(new Intent(HongliTiXianActivity.this, AddAlipayActivity.class));
                                } else {
                                    bank = bankInfo.get(position);
                                    bankId = bankIdList.get(position);
                                    tvHonglitixianYinhangka.setText(bank);
                                    if (bank.contains("支付宝")) {
                                        L.e("onClick", "   支付宝");
                                        tvBeizhu.setText(alipay_beizhu);
                                        if ("是".equals(alipay_tiXian_fanShi)) {
                                            L.e("onClick", "   支付宝     是");
                                            tvHonglitixianGuding.setVisibility(View.VISIBLE);
                                            etHonglitixianGuding.setVisibility(View.GONE);
                                            tvHonglitixianGuding.setText(alipay_gudinghongli);
                                            if (Double.parseDouble(alipay_gudinghongli) >= Double.parseDouble(zongjine)) {
                                                btTixian.setEnabled(false);
                                            } else {
                                                btTixian.setEnabled(true);
                                            }
                                        } else {
                                            btTixian.setEnabled(true);
                                            tvHonglitixianGuding.setVisibility(View.GONE);
                                            etHonglitixianGuding.setVisibility(View.VISIBLE);
                                            etHonglitixianGuding.setText("");
                                        }
                                    } else {
                                        L.e("onClick", "银行卡");
                                        tvBeizhu.setText(bank_beizhu);
                                        if ("是".equals(bank_tiXian_fanShi)) {
                                            L.e("onClick", "银行卡    是");
                                            tvHonglitixianGuding.setVisibility(View.VISIBLE);
                                            etHonglitixianGuding.setVisibility(View.GONE);
                                            tvHonglitixianGuding.setText(bank_gudinghongli);
                                            if (Double.parseDouble(bank_gudinghongli) >= Double.parseDouble(zongjine)) {
                                                btTixian.setEnabled(false);
                                            } else {
                                                btTixian.setEnabled(true);
                                            }
                                        } else {
                                            btTixian.setEnabled(true);
                                            tvHonglitixianGuding.setVisibility(View.GONE);
                                            etHonglitixianGuding.setVisibility(View.VISIBLE);
                                            etHonglitixianGuding.setText("");
                                        }
                                    }
                                    dismissDialog();
                                }
                            } catch (Exception e) {
                                dismissDialog();
                            }
                        }
                    });
                }
                break;

            case R.id.btn_honglitixian_tixian:  //点击提现
                if (successInfo) {
                    check();
                } else {
                    //红利提现信息（可兑换红利、固定金额）
                    requestHandleArrayList.add(requestAction.p_hongli_tx_three(HongliTiXianActivity.this));
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess:" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_HONGLITXTHREE:
                try {
                    zongjine = response.getString("总金额");

                    JSONArray array = response.getJSONArray("列表");
                    JSONObject object;
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        if ("支付宝".equals(JSONUtlis.getString(object, "提现类别"))) {
                            //支付宝提现
                            alipay_tiXian_fanShi = JSONUtlis.getString(object, "固定提现", "是");
                            alipay_gudinghongli = JSONUtlis.getString(object, "固定提现额");
                            alipay_beizhu = JSONUtlis.getString(object, "备注").replace("/n", "\n");

                        } else if ("银行卡".equals(JSONUtlis.getString(object, "提现类别"))) {
                            //银行卡提现
                            bank_tiXian_fanShi = JSONUtlis.getString(object, "固定提现", "是");
                            bank_gudinghongli = JSONUtlis.getString(object, "固定提现额");
                            bank_beizhu = JSONUtlis.getString(object, "备注").replace("/n", "\n");
                        }
                    }

                    tvZongjine.setText(zongjine);
                    //请求成功
                    successInfo = true;

                } catch (Exception e) {

                }
                break;
            case RequestAction.TAG_BANKINFO:
                bankIsNoNull = true;
                JSONArray bankList = response.getJSONArray("列表");
                if (bankList.length() > 0) {
                    bankInfo.clear();
                    bankIdList.clear();

                    for (int i = 0; i < bankList.length(); i++) {
                        JSONObject object = bankList.getJSONObject(i);

                        String banknum = JSONUtlis.getString(object, "卡号");
                        String leibie = JSONUtlis.getString(object, "类别");
                        switch (leibie) {
                            case "支付宝":
                                if (banknum.length() > 0) {
                                    if (banknum.contains("@")) {
                                        bankInfo.add(JSONUtlis.getString(object, "账户名") + "(" +
                                                banknum.substring(banknum.lastIndexOf("@") - 3, banknum.length()) + ")");
                                    } else if (banknum.length() == 11) {
                                        bankInfo.add(JSONUtlis.getString(object, "账户名") + "(" + banknum.substring(0, 3) + "****" + banknum.substring(7, 11) + ")");
                                    } else {
                                        bankInfo.add(JSONUtlis.getString(object, "账户名") + "(" + banknum.substring(banknum.length() - 4, banknum.length()) + ")");
                                    }
                                } else {
                                    bankInfo.add(JSONUtlis.getString(object, "账户名"));
                                }
                                bankIdList.add(object.getString("id"));
                                break;
                            case "信用卡":
                            case "储蓄卡":
                                if (!TextUtils.isEmpty(banknum) && banknum.length() > 4) {
                                    bankInfo.add(JSONUtlis.getString(object, "账户名") + "(" + banknum.substring(banknum.length() - 4, banknum.length()) + ")");
                                    bankIdList.add(object.getString("id"));
                                }
                                break;
                        }
                    }
                }
                break;
            case RequestAction.TAG_DIVIDENDEXCHANGE:
                //红利提现信息（可兑换红利、固定金额）
                requestHandleArrayList.add(requestAction.p_hongli_tx_three(HongliTiXianActivity.this));
//                MyToast.getInstance().showToast(mContext, "已提交审核，三个工作日到账");
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取银行卡接口
        requestHandleArrayList.add(requestAction.BankInfo(HongliTiXianActivity.this, cardType));
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {
        super.onCancel(requestTag, showLoad);
        if (requestTag == RequestAction.TAG_DIVIDENDEXCHANGE || requestTag == RequestAction.TAG_HONGLITXTHREE) {
            LoadDialog.dismiss(mContext);
            finish();
        }
    }

    public void check() {
        try {
            if (bank.contains("支付宝") && "是".equals(alipay_tiXian_fanShi)
                    || !bank.contains("支付宝") && "是".equals(bank_tiXian_fanShi)) {
                if (tvHonglitixianYinhangka.getText().toString().equals("请选择提现账户")) {
                    DialogUtlis.oneBtnNormal(getmDialog(), "请选择提现账户");
                } else if (Double.parseDouble(tvHonglitixianGuding.getText().toString()) > Double.parseDouble(zongjine)) {
                    DialogUtlis.oneBtnNormal(getmDialog(), "可兑换红利不足，不能进行提取");
                } else if (preferences.getString(ConstantUtlis.SP_MYPWD, "否").equals("否")) {
                    CommonUtlis.toSetPwd(this, getmDialog());
                } else {
                    DialogUtlis.customPwd(getmDialog(), "¥" + tvHonglitixianGuding.getText().toString(), true, new MDialogInterface.PasswordInter() {
                        @Override
                        public void callback(String data) {
                            if (TextUtils.isEmpty(data) || data.length() < 6) {
                                MyToast.getInstance().showToast(mContext, "请输入支付密码");
                            } else {
                                String weiYiID = preferences.getString(ConstantUtlis.SP_ONLYID, "");
                                if (bank.contains("支付宝")) {
                                    requestHandleArrayList.add(requestAction.DividendExchange(HongliTiXianActivity.this, weiYiID, bankId, MD5Utlis.Md5(data), tvHonglitixianGuding.getText().toString(), true));
                                } else {
                                    requestHandleArrayList.add(requestAction.DividendExchange(HongliTiXianActivity.this, weiYiID, bankId, MD5Utlis.Md5(data), tvHonglitixianGuding.getText().toString()));
                                }
                            }
                        }
                    });
                }
            } else {
                if (tvHonglitixianYinhangka.getText().toString().equals("请选择提现账户")) {
                    DialogUtlis.oneBtnNormal(getmDialog(), "请选择提现账户");
                } else if ("".equals(etHonglitixianGuding.getText().toString())) {
                    MyToast.getInstance().showToast(mContext, "请输入提现金额");
                } else if (Double.parseDouble(etHonglitixianGuding.getText().toString()) > Double.parseDouble(zongjine)) {
                    DialogUtlis.oneBtnNormal(getmDialog(), "可兑换红利不足，不能进行提取");
                } else if (preferences.getString(ConstantUtlis.SP_MYPWD, "否").equals("否")) {
                    CommonUtlis.toSetPwd(this, getmDialog());
                } else {
                    DialogUtlis.customPwd(getmDialog(), "¥" + etHonglitixianGuding.getText().toString(), true, new MDialogInterface.PasswordInter() {
                        @Override
                        public void callback(String data) {
                            if (TextUtils.isEmpty(data) || data.length() < 6) {
                                MyToast.getInstance().showToast(mContext, "请输入支付密码");
                            } else {
                                String weiYiID = preferences.getString(ConstantUtlis.SP_ONLYID, "");
                                if (bank.contains("支付宝")) {
                                    requestHandleArrayList.add(requestAction.DividendExchange(HongliTiXianActivity.this, weiYiID, bankId, MD5Utlis.Md5(data), etHonglitixianGuding.getText().toString(), true));
                                } else {
                                    requestHandleArrayList.add(requestAction.DividendExchange(HongliTiXianActivity.this, weiYiID, bankId, MD5Utlis.Md5(data), etHonglitixianGuding.getText().toString()));
                                }
                            }
                        }
                    });
                }
            }

        } catch (NumberFormatException e) {
            DialogUtlis.oneBtnNormal(getmDialog(), "可兑换红利不足，不能进行提取");
        }
    }

}
