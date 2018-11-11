package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.bean.PayTypeBean;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.web.WebToPayManager;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：lvjiayu on 2017/3/31.
 * 功能：积分充值
 */

public class JiFenChongZhiActivity extends BaseActivity {

    @Bind(R.id.et_jifenchongzhi_jifen)
    EditText etJifenchongzhiJifen;
    @Bind(R.id.btn_jifenchongzhi_recharge)
    Button btnJifenchongzhiRecharge;
    @Bind(R.id.gift_points)
    TextView txGiftPoints;
    @Bind(R.id.gift_points_note)
    TextView txGiftPointsNote;
    private String jifen;
    //输入格式错误提示语
    private String errorMessage;
    private ArrayList<PayTypeBean> payTypeLocal;

    @Override
    public int initResource() {
        return R.layout.activity_my_jifenchongzhi;
    }

    @Override
    public void initData() {

        CommonUtlis.setTitleBar(this, "积分充值", "充值明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JiFenChongZhiActivity.this, JiFenChongZhiMingXiActivity.class));
            }
        });

        requestHandleArrayList.add(requestAction.getJFNote(JiFenChongZhiActivity.this));
        payTypeLocal = new ArrayList<>();
        etJifenchongzhiJifen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //第一位为零，后面只能输入点
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etJifenchongzhiJifen.setText(s.subSequence(0, 1));
                        etJifenchongzhiJifen.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.et_jifenchongzhi_jifen, R.id.btn_jifenchongzhi_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_jifenchongzhi_recharge:
                if (checkFormat()) {
                    if (preferences.getString(ConstantUtlis.SP_MYPWD, "否").equals("否")) {
                        CommonUtlis.toSetPwd(this, getmDialog());
                    } else {
                        requestHandleArrayList.add(requestAction.IntegralOrder(JiFenChongZhiActivity.this, jifen));
                    }
                } else {
                    MyToast.getInstance().showToast(this, errorMessage);
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_INTEGRALORDER:
                // 跳转收银台
                startActivity(WebToPayManager.toCashier(preferences, mContext, response));
                break;

            case RequestAction.TAG_JIFENCHONGZHIBEIZHU:
                txGiftPointsNote.setText(response.getString("备注").replace("\\n", "\n"));
                break;
        }
    }

    //检测格式是否正确
    public boolean checkFormat() {
        jifen = etJifenchongzhiJifen.getText().toString();
        if (TextUtils.isEmpty(jifen)) {
            errorMessage = "请输入要充值金额";
            return false;
        }
//        else if (Float.parseFloat(jifen) < 100) {
//            errorMessage = "积分充值不能低于100";
//            return false;
//        }
        else if (Float.parseFloat(jifen) > 100000000) {
            errorMessage = "积分充值不能高于100000000";
            return false;
        } else {
            return true;
        }
    }

}
