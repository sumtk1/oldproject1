package com.gloiot.hygo.ui.activity.my.qianbao;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.my.ziliao.shimingrenzheng.RenZheng01Activity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：Ljy on 2017/3/29.
 * 功能：我的——转让积分
 */


public class ZhuanrangJifenActivity extends BaseActivity implements BaseActivity.RequestErrorCallback {
    @Bind(R.id.et_zr_shouji)
    EditText etZrShouji;
    @Bind(R.id.tv_zr_name)
    TextView tvZrName;
    @Bind(R.id.tv_zr_jifenleixing)
    TextView tvZrJifenleixing;
    @Bind(R.id.tv_zr_keyongjifen)
    TextView tvZrKeyongjifen;
    @Bind(R.id.btn_jifen)
    Button btnJifen;
    @Bind(R.id.et_zr_jifen)
    EditText etZrJifen;
    @Bind(R.id.et_zr_beizhu)
    EditText etZrBeizhu;
    @Bind(R.id.activity_my_zhuanrangjifen)
    RelativeLayout activityMyZhuanrangjifen;
    @Bind(R.id.rl_jieshouren_xinming)
    RelativeLayout rlJieshourenXinming;
    @Bind(R.id.rl_zr_leixing)
    RelativeLayout rl_zr_leixing;
    @Bind(R.id.rl_keyong_jifen)
    RelativeLayout rlKeyongJifen;
    @Bind(R.id.tv_zr_zhuanrangleixing)
    TextView tvZrZhuanrangleixing;

    private String accountType;//账户类型
    private ArrayList<String> transferType = new ArrayList<>();//存储两种积分类型
    private String hongliIntegral;//红利积分
    private String baseIntegral;//基本积分
    private String name;
    private String phone;
    private String jifen;
    private String keyong;
    private String beizhu;
    private boolean request_flag = false;
    private int position;
    private String zhuanRang_leixin;

    @Override

    public int initResource() {
        return R.layout.activity_my_zhuanrangjifen;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.PersonalCenter(ZhuanrangJifenActivity.this));
        requestHandleArrayList.add(requestAction.p_transferIntegral(ZhuanrangJifenActivity.this));
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "积分转让", "转让明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ZhuanrangJifenActivity.this, ZhuanRangJiFenMingXiActivity.class));
            }
        });

        setRequestErrorCallback(this);

        etZrShouji.addTextChangedListener(new TextWatcher() {

            private int length = 0;
            private boolean flag = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = s.toString().trim();
                if (!flag) {
                    length = s.length();
                    flag = true;
                }
                if (s.length() - length > 0) {
                    name = "";
                    tvZrName.setText("");
                }

            }
        });

        etZrShouji.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                } else {
                    if (!request_flag) {
                        if (!"".equals(etZrShouji.getText().toString()))
                            requestHandleArrayList.add(requestAction.GetName(ZhuanrangJifenActivity.this, etZrShouji.getText().toString().trim()));
                    }
                }
            }
        });


        etZrJifen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //第一位为零，后面只能输入点
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etZrJifen.setText(s.subSequence(0, 1));
                        etZrJifen.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /***
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
//                v.setFocusable(true); //这里不需要是因为下面一句代码会同时实现这个功能
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }


    @OnClick({R.id.tv_zr_jifenleixing, R.id.btn_jifen, R.id.rl_jieshouren_xinming,
            R.id.rl_keyong_jifen, R.id.et_zr_shouji, R.id.rl_zr_leixing})
    public void onViewClicked(View view) {
        if (onMoreClick(view)) return;
        switch (view.getId()) {
            case R.id.tv_zr_jifenleixing:
                if (transferType.size()>0){
                    DialogUtlis.customListView(getmDialog(), "选择积分账户", transferType, new MDialogInterface.ListViewOnClickInter() {
                        @Override
                        public void onItemClick(String data, int position) {
                            try {
                                if (data.contains("积分")) {
                                    tvZrJifenleixing.setText("积分赠送账户");
                                    tvZrKeyongjifen.setText(baseIntegral);
                                    accountType = "积分";
                                } else {
                                    tvZrJifenleixing.setText("市场奖励账户");
                                    tvZrKeyongjifen.setText(hongliIntegral);
                                    accountType = "红利";
                                }
                            } catch (Exception e) {
                                Log.e("转让积分", "-" + e);
                            }
                        }
                    });
                }
                break;
            case R.id.btn_jifen:
                request_flag = true;
                if (TextUtils.isEmpty(phone)) {
                    MyToast.getInstance().showToast(this, "请输入接收人账号");
                } else if (TextUtils.isEmpty(tvZrName.getText().toString())) {
                    if (!"".equals(etZrShouji.getText().toString()))
                        requestHandleArrayList.add(requestAction.GetName(ZhuanrangJifenActivity.this, etZrShouji.getText().toString().trim()));
                } else {
                    check();
                }
                break;
            case R.id.rl_zr_leixing:

                final ArrayList<String> info = new ArrayList<>();
                info.add("个人转让");
                info.add("激活商城权限");
                DialogUtlis.customLoopView(getmDialog(), "选择转让类型", info, position, new MDialogInterface.LoopViewInter() {
                    @Override
                    public void getPostition(int postition) {
                        position = postition;
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            tvZrZhuanrangleixing.setText(info.get(position));
                            dismissDialog();
                        } catch (Exception e) {
                            dismissDialog();
                        }
                    }
                });

                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_PERSONALCENTER:
                transferType.clear();
                hongliIntegral = response.getString("红利账户");
                baseIntegral = response.getString("积分账户");
                transferType.add("市场奖励账户 （" + hongliIntegral + ")");
                transferType.add("积分赠送账户 （" + baseIntegral + ")");
                if ("红利".equals(accountType)) {
                    tvZrKeyongjifen.setText(hongliIntegral);
                } else if ("积分".equals(accountType)) {
                    tvZrKeyongjifen.setText(baseIntegral);
                }
                break;
            case RequestAction.TAG_GETNAME:
                name = response.getString("姓名");
                tvZrName.setText(name);
                break;
            case RequestAction.TAG_TRANSFERINTEGRAL:
                DialogUtlis.oneBtnNormal(getmDialog(), "转让成功", false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        finish();
                    }
                });
                break;

            case RequestAction.TAG_P_TRANSFERINTEGRAL:
                zhuanRang_leixin = response.getString("转让类型");

                if ("是".equals(response.getString("转让类型"))) {
                    rl_zr_leixing.setVisibility(View.VISIBLE);
                } else {
                    rl_zr_leixing.setVisibility(View.GONE);
                }

                break;
        }
    }

    public void check() {
        jifen = etZrJifen.getText().toString();
        keyong = tvZrKeyongjifen.getText().toString();
        beizhu = etZrBeizhu.getText().toString();

        if (TextUtils.isEmpty(tvZrJifenleixing.getText().toString())) {
            MyToast.getInstance().showToast(this, "请选择积分账户类型");
        } else if (TextUtils.isEmpty(jifen)) {
            MyToast.getInstance().showToast(this, "转让积分不能为空");
        } else if ("0".equals(jifen)) {
            MyToast.getInstance().showToast(this, "转让积分不能为零");
        } else if (Float.parseFloat(jifen) > Float.parseFloat(keyong)) {
            MyToast.getInstance().showToast(this, "账户余额不足");
        } else if (preferences.getString(ConstantUtlis.SP_MYPWD, "否").equals("否")) {
            CommonUtlis.toSetPwd(this, getmDialog());
        } else if ("是".equals(zhuanRang_leixin) && TextUtils.isEmpty(tvZrZhuanrangleixing.getText().toString())) {
            MyToast.getInstance().showToast(this, "请选择转让类型");
        } else {
            if (!preferences.getString(ConstantUtlis.SP_USERSMRZ, "").equals("已认证")) {
                DialogUtlis.twoBtnNormal(getmDialog(), "提示", "未实名认证不能转积分,请先进行实名认证!", true, "取消", "去认证",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                Intent intent = new Intent(mContext, RenZheng01Activity.class);
                                mContext.startActivity(intent);
                            }
                        });
            } else {
                DialogUtlis.customPwd(getmDialog(), jifen, true, new MDialogInterface.PasswordInter() {
                    @Override
                    public void callback(String data) {
                        if ("是".equals(zhuanRang_leixin)) {
                            requestHandleArrayList.add(requestAction.TransferIntegral(ZhuanrangJifenActivity.this, accountType, phone, MD5Utlis.Md5(data), jifen, beizhu, tvZrZhuanrangleixing.getText().toString()));
                        } else {
                            requestHandleArrayList.add(requestAction.TransferIntegral(ZhuanrangJifenActivity.this, accountType, phone, MD5Utlis.Md5(data), jifen, beizhu, ""));
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {
        super.onCancel(requestTag, showLoad);
        if (requestTag == RequestAction.TAG_PERSONALCENTER) {
            finish();
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_TRANSFERINTEGRAL:
                if (response.getString("状态").equals("支付密码输入错误")) {

                    DialogUtlis.twoBtnNormal(getmDialog(), "提示", "支付密码输入错误", true, "取消", "重试",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                    DialogUtlis.customPwd(getmDialog(), jifen, true, new MDialogInterface.PasswordInter() {
                                        @Override
                                        public void callback(String data) {
                                            requestHandleArrayList.add(requestAction.TransferIntegral(ZhuanrangJifenActivity.this, accountType, phone, MD5Utlis.Md5(data), jifen, beizhu, tvZrZhuanrangleixing.getText().toString()));
                                        }
                                    });
                                }
                            });
                } else {
                    MyToast.getInstance().showToast(mContext, response.getString("状态"));
                }
                break;
            default:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
                break;
        }
    }

}
