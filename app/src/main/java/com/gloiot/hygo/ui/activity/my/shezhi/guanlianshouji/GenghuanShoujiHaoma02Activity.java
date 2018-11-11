package com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.MyToast;

import butterknife.Bind;

/**
 * 更换手机号码第二步
 */
public class GenghuanShoujiHaoma02Activity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_current_phonenum)
    TextView mTvCurrentPhoneNum;
    @Bind(R.id.et_new_phonenum)
    EditText mEtNewPhoneNum;
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.tv_quhao)
    TextView tv_quhao;


    private String originalPhoneNum;
    private String quhao;

    @Override
    public int initResource() {
        return R.layout.activity_genghuan_shouji_haoma02;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "更换手机号码");
        originalPhoneNum = getIntent().getStringExtra("originalPhoneNum");
        mTvCurrentPhoneNum.setText(originalPhoneNum);
        if (preferences.getString(ConstantUtlis.SP_HAIWAIZHUCE, "").equals("是")) { //是否海外注册
            tv_quhao.setVisibility(View.VISIBLE);
            quhao = preferences.getString(ConstantUtlis.SP_BANGDINGPHONE, "").split("\\+")[0];
            tv_quhao.setText(quhao + "+");
        } else {
            quhao = "";
            tv_quhao.setVisibility(View.GONE);
            mEtNewPhoneNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//最大输入长度
        }
        setListener();
    }

    private void setListener() {
        mEtNewPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (preferences.getString(ConstantUtlis.SP_HAIWAIZHUCE, "").equals("是")) { //是否海外注册
                    mTvNext.setEnabled(true);
                    mTvNext.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                    mTvNext.setTextColor(getResources().getColor(R.color.white));
                    mTvNext.setOnClickListener(GenghuanShoujiHaoma02Activity.this);
                } else {
                    if (s.toString().length() == 11) {
                        mTvNext.setEnabled(true);
                        mTvNext.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                        mTvNext.setTextColor(getResources().getColor(R.color.white));
                        mTvNext.setOnClickListener(GenghuanShoujiHaoma02Activity.this);
                    } else {
                        mTvNext.setEnabled(false);
                        mTvNext.setBackgroundResource(R.drawable.bg_no_but_fillet);
                        mTvNext.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                String phone = quhao.equals("") ? mEtNewPhoneNum.getText().toString() : quhao + "+" + mEtNewPhoneNum.getText().toString();
                if (phone.equals(originalPhoneNum)) {
                    MyToast.getInstance().showToast(mContext, "手机号不能和之前一致");
                } else {
                    Intent intent = new Intent(GenghuanShoujiHaoma02Activity.this, TianxieYanzhengmaActivity.class);
                    if (preferences.getString(ConstantUtlis.SP_HAIWAIZHUCE, "").equals("是")) {
                        intent.putExtra("newPhoneNum", quhao + "+" + mEtNewPhoneNum.getText().toString());
                    } else
                        intent.putExtra("newPhoneNum", mEtNewPhoneNum.getText().toString());
                    intent.putExtra("type","change_phone");
                    startActivity(intent);
                }

                break;
        }
    }
}
