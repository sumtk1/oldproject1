package com.gloiot.hygo.ui.activity.my.yinhangka;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.MyToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class BangDingYinHangKaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_bank_02)
    TextView tvMydata02;
    @Bind(R.id.tv_getYZM)
    TextView tv_getYZM;
    @Bind(R.id.tv_bank_05)
    EditText tvMydata05;
    @Bind(R.id.tv_bank_06)
    TextView tvMydata06;
    @Bind(R.id.tv_bank_07)
    TextView tvMydata07;
    @Bind(R.id.tv_bank_10)
    TextView tvMydata10;
    @Bind(R.id.tv_bank_11)
    EditText tvMydata11;
    @Bind(R.id.btn_bindbankCard_next)
    Button btnBindbankCardNext;
    private ArrayList<String> bank = new ArrayList<>();
    private int i = 60;
    private String num, truename;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tv_getYZM.setText(i + "秒后重试");
            } else if (msg.what == -8) {
                tv_getYZM.setText("获取验证码");
                tv_getYZM.setClickable(true);
                i = 60;
            }
        }
    };

    @Override
    public int initResource() {
        return R.layout.activity_my_bangdingyinhangka;
    }


    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "添加银行卡");
        num = preferences.getString(ConstantUtlis.SP_BANGDINGPHONE, "");
        tvMydata10.setText(num);
        requestHandleArrayList.add(requestAction.XuanZeYinHangKaLeiXing(BangDingYinHangKaActivity.this));
        CommonUtlis.setTitleBar(this, "添加银行卡");
        truename = preferences.getString(ConstantUtlis.SP_TRUENAME, "");
        tvMydata07.setText(truename);
        requestHandleArrayList.add(requestAction.XuanZeYinHangKaLeiXing(BangDingYinHangKaActivity.this));
    }

    int place, place1;

    @OnClick({R.id.btn_bindbankCard_next, R.id.rl_bankname, R.id.tv_getYZM,   R.id.rl_bank_tv_cardtype})
    @Override
    public void onClick(View view) {
        if (onMoreClick(view)) return;
        switch (view.getId()) {
            case R.id.rl_bankname://选择银行
                if (bank.size() > 0) {
                    DialogUtlis.customLoopView(getmDialog(), "选择银行卡类型", bank, place, new MDialogInterface.LoopViewInter() {
                        @Override
                        public void getPostition(int postition) {
                            place = postition;
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tvMydata02.setText(bank.get(place));
                            dismissDialog();
                        }
                    });
                }
                break;
            case R.id.btn_bindbankCard_next:
                String name = tvMydata02.getText().toString();
                String kahao = tvMydata05.getText().toString();
                String type = tvMydata06.getText().toString();
                String xingming = tvMydata07.getText().toString();
                String shoujihao = tvMydata10.getText().toString();
                String yanzhengma = tvMydata11.getText().toString();
                String regx = "[0-9]{17}([0-9]|x|X)";
                if (TextUtils.isEmpty(name)) {
                    MyToast.getInstance().showToast(mContext, "请选择银行种类");
                } else if (TextUtils.isEmpty(kahao)) {
                    MyToast.getInstance().showToast(mContext, "请输入银行卡号");
                } else if (kahao.length() < 15 || kahao.length() > 21) {
                    MyToast.getInstance().showToast(mContext, "银行卡号有误，请重新输入");
                } else if (TextUtils.isEmpty(xingming)) {
                    MyToast.getInstance().showToast(mContext, "请填写持卡人姓名");
                }
                else if (TextUtils.isEmpty(yanzhengma)) {
                    MyToast.getInstance().showToast(mContext, "请输入验证码");
                } else {
                    requestHandleArrayList.add(requestAction.TianJiaYinHangKa(BangDingYinHangKaActivity.this, name, kahao, type, xingming,  shoujihao, yanzhengma));
                }
                break;
            case R.id.tv_getYZM:
                shoujihao = tvMydata10.getText().toString();
                requestHandleArrayList.add(requestAction.YZM(BangDingYinHangKaActivity.this, shoujihao, "银行卡验证码"));
                break;
            default:
                break;

        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_TIANJAYINHANGKA:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                finish();
                break;
            case RequestAction.TAG_YZM:
                MyToast.getInstance().showToast(this, "验证码已发送");
                tv_getYZM.setClickable(false);
                tv_getYZM.setText(i + "秒后重试");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            SystemClock.sleep(1000);
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case RequestAction.TAG_XUANZEYINHANGKALEIXING:
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("数据");
                    bank.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        bank.add(jsonObject.getString("银行名"));
                    }
                }
                break;
            default:
                break;

        }
    }

}
