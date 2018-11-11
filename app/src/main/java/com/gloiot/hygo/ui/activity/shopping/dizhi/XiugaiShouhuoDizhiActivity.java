package com.gloiot.hygo.ui.activity.shopping.dizhi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.DizhiBean;
import com.gloiot.hygo.ui.activity.shopping.gouwuche.QuerenDingdanActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class XiugaiShouhuoDizhiActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.et_xiugai_shouhuo_dizhi_name)
    EditText et_xiugai_shouhuo_dizhi_name;
    @Bind(R.id.et_xiugai_shouhuo_dizhi_phone)
    EditText et_xiugai_shouhuo_dizhi_phone;
    @Bind(R.id.et_xiugai_shouhuo_dizhi_dizhi)
    EditText et_xiugai_shouhuo_dizhi_dizhi;

    private Context mContext;
    private DizhiBean dizhi;
    private String changeType;

    @Override
    public int initResource() {
        return R.layout.activity_xiugai_shouhuo_dizhi;
    }

    @Override
    public void initData() {
        mContext = this;
        Intent intent = getIntent();
        CommonUtlis.getTitleMore((Activity) mContext).setOnClickListener(this);

        if (intent.getStringExtra("添加修改类型").equals("添加")) { //添加发货地址
            CommonUtlis.setTitleBar((Activity) mContext, true, "添加收货地址", "保存");
            changeType = "添加";
        } else if (intent.getStringExtra("添加修改类型").equals("确认订单_添加")) {
            Log.e("确认订单_添加", "确认订单_添加---");
            CommonUtlis.setTitleBar((Activity) mContext, true, "添加收货地址", "保存");
            changeType = "确认订单_添加";
        } else { //修改发货地址
            changeType = "修改";
            CommonUtlis.setTitleBar((Activity) mContext, true, "修改收货地址", "保存");
            dizhi = (DizhiBean) intent.getSerializableExtra("dizhi");
            Log.e("dizhi", dizhi.toString());
            et_xiugai_shouhuo_dizhi_name.setText(dizhi.getShouhuoren());
            et_xiugai_shouhuo_dizhi_phone.setText(dizhi.getShoujihao());
            et_xiugai_shouhuo_dizhi_dizhi.setText(dizhi.getDizhi());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toptitle_right:
                if (check_dizhi()) {
                    addup_dizhi();
                }
                break;
        }
    }

    private boolean check_dizhi() {
        if (et_xiugai_shouhuo_dizhi_name.getText().toString().isEmpty() && et_xiugai_shouhuo_dizhi_phone.getText().toString().isEmpty() && et_xiugai_shouhuo_dizhi_dizhi.getText().toString().isEmpty()) {
            DialogUtlis.oneBtnNormal(getmDialog(), "请输入收货地址信息");
            return false;
        } else if (et_xiugai_shouhuo_dizhi_name.getText().toString().isEmpty()) {
            DialogUtlis.oneBtnNormal(getmDialog(), "请输入收货人");
            return false;
        } else if (et_xiugai_shouhuo_dizhi_phone.getText().toString().isEmpty()) {
            DialogUtlis.oneBtnNormal(getmDialog(), "请输入手机号");
            return false;
        } else if (et_xiugai_shouhuo_dizhi_phone.getText().length() < 11) {
            DialogUtlis.oneBtnNormal(getmDialog(), "请输入正确手机号");
            return false;
        } else if (et_xiugai_shouhuo_dizhi_dizhi.getText().toString().isEmpty()) {
            DialogUtlis.oneBtnNormal(getmDialog(), "请输入详细地址");
            return false;
        } else {
            return true;
        }

    }

    private void addup_dizhi() {
        if (changeType.equals("修改")) {
            dizhi = new DizhiBean(dizhi.getId(),
                    et_xiugai_shouhuo_dizhi_dizhi.getText().toString(),
                    et_xiugai_shouhuo_dizhi_phone.getText().toString(),
                    et_xiugai_shouhuo_dizhi_name.getText().toString(),
                    dizhi.getZhuangtai());
        } else {
            Log.e("addaddress", "addaddress");
            dizhi = new DizhiBean("",
                    et_xiugai_shouhuo_dizhi_dizhi.getText().toString(),
                    et_xiugai_shouhuo_dizhi_phone.getText().toString(),
                    et_xiugai_shouhuo_dizhi_name.getText().toString(),
                    "显示");
        }
        requestHandleArrayList.add(requestAction.shop_kf_addup(this, dizhi));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case RequestAction.TAG_SHOP_KF_ADDUP:
                Log.e("添加修改地址成功", response.toString());
                switch (changeType) {
                    case "确认订单_添加":
                        Intent intent2 = new Intent(XiugaiShouhuoDizhiActivity.this, QuerenDingdanActivity.class);
                        Log.e("dizhi", dizhi.toString());
                        intent2.putExtra("dizhi", dizhi);
                        setResult(RESULT_OK, intent2);
                        this.finish();
                        break;
                    case "添加":
                        Log.e("+++", "添加地址");
                        this.finish();
                        break;
                    case "修改":
                        Log.e("+++", "修改地址");
                        this.finish();
                        break;
                }
                break;
        }

    }
}

