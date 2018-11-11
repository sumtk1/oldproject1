package com.gloiot.hygo.ui.activity.payment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 结算界面
 */
public class SettlementActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {

    // 收款方的用户图片
    @Bind(R.id.iv_payee_img1)
    ImageView iv_payee_img1;
    // 收款方的用户名字
    @Bind(R.id.tv_payee_name)
    TextView tv_payee_name;
    // 转款金额
    @Bind(R.id.et_amount_collected)
    EditText et_amount_collected;
    // 备注内容
    @Bind(R.id.edit_remarks)
    EditText edit_remarks;
    // 确定按钮
    @Bind(R.id.btn_bg_click1)
    Button btn_bg_click1;
    // 积分账户金额
    @Bind(R.id.tv_settlement_zhanhu)
    TextView tv_settlement_zhanhu;

    private String orderNum=""; //转款订单号
    private String onlyID = ""; // 收款人onlyID
    private String zhanghuMoney = ""; // 积分账户金额
    private String zhanghu = "";


    @Override
    public int initResource() {
        return R.layout.activity_settlement;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "转账金額");
        onlyID = getIntent().getStringExtra("onlyID");
        requestHandleArrayList.add(requestAction.ChaoJIShangJiaInfo(SettlementActivity.this, onlyID)); // 获取收款方信息
        setRequestErrorCallback(this);
        CommonUtlis.setNumPoint(et_amount_collected, 2);
    }
    Double m1, m2;
    @OnClick({R.id.btn_bg_click1})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bg_click1:
                // 判断如果为空重新请求获取收款方信息
                if (TextUtils.isEmpty(zhanghuMoney) && TextUtils.isEmpty(orderNum)) {
                    requestHandleArrayList.add(requestAction.ChaoJIShangJiaInfo(SettlementActivity.this, onlyID)); // 获取收款方信息
                } else if (TextUtils.isEmpty(et_amount_collected.getText().toString())){
                    MyToast.getInstance().showToast(mContext, "请输入转账金额");
                }else if (Double.parseDouble(et_amount_collected.getText().toString())<=0){
                    MyToast.getInstance().showToast(mContext, "请填写正确金额");
                } else {
                    try{
                        m1 = Double.parseDouble(zhanghuMoney);  // 积分账户余额
                        m2 = Double.parseDouble(et_amount_collected.getText().toString()); // 转账积分
                    } catch (Exception e){
                        m1 = 0.0;
                        m2 = 0.0;
                    }

                    if (m1 < m2) {
                        MyToast.getInstance().showToast(mContext, "账户余额不足");
                    } else {
                        DialogUtlis.customPwd(getmDialog(), m2 + "", true, new MDialogInterface.PasswordInter() {
                            @Override
                            public void callback(String data) {
                                requestHandleArrayList.add(requestAction.PayChaoJIShangJia(SettlementActivity.this, onlyID, zhanghu, m2 + "",orderNum, data, edit_remarks.getText().toString()));
                            }
                        });
                    }
                }
                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("Success请求回调",response.toString());
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_CHAOJISHANGJIAINFO:
                String name = response.getString("名称");
                String pic = response.getString("头像");
                zhanghuMoney = response.getString("金额");
                zhanghu = response.getString("付款账户");
                orderNum = response.getString("转账订单号");

                tv_settlement_zhanhu.setText(zhanghu + "(" + zhanghuMoney + ")");
                PictureUtlis.loadImageViewHolder(mContext, pic, R.mipmap.ic_portrait_default, iv_payee_img1);
                tv_payee_name.setText(name);
                break;
            case RequestAction.TAG_PAYCHAOJISHANGJIA:
                Intent it = new Intent(SettlementActivity.this, PaymentResultsActivity.class);
                it.putExtra("money", et_amount_collected.getText().toString());
                startActivity(it);
                finish();
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        Log.e("Error请求回调",response.toString());
        switch (requestTag) {
            case RequestAction.TAG_PAYCHAOJISHANGJIA:
                String status=response.getString("状态");
                if(status.equals("您输入的密码不正确!")){
                    DialogUtlis.oneBtnNormal(getmDialog(), "提示", status, "确定", false, null);
                }else{
                    MyToast.getInstance().showToast(mContext, status);
                    Intent it = new Intent(SettlementActivity.this, PaymentResultsActivity.class);
                    it.putExtra("money", "-1");
                    startActivity(it);
                    finish();
                }
                break;
            default:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                break;
        }
    }
}
