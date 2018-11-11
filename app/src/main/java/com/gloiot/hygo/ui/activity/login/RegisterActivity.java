package com.gloiot.hygo.ui.activity.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.ui.activity.listwithheard.SelectCountriesActivity;
import com.gloiot.hygo.ui.activity.listwithheard.User;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.InfoRequestUtils;
import com.zyd.wlwsdk.utils.MyToast;
import com.gloiot.hygo.utlis.StringUtils;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作用：海外注册
 * 詹志钦
 * 时间：2017-6-19
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_reg_overseas_code)
    TextView tv_reg_overseas_code;
    @Bind(R.id.et_overseas_area)
    TextView et_overseas_area;
    @Bind(R.id.et_reg_overseas_phone)
    EditText et_reg_overseas_phone;
    @Bind(R.id.et_reg_overseas_code)
    EditText et_reg_overseas_code;
    @Bind(R.id.et_reg_overseas_zhanghao)
    EditText et_reg_overseas_zhanghao;
    @Bind(R.id.et_reg_overseas_psw)
    EditText et_reg_overseas_psw;
    @Bind(R.id.et_reg_overseas_confirm_psw)
    EditText et_reg_overseas_confirm_psw;
    @Bind(R.id.im_reg_overseas_agreement)
    ImageView im_reg_overseas_agreement;
    @Bind(R.id.btn_register_overseas)
    Button btn_register_overseas;
    @Bind(R.id.tv_overseas_quhao)
    TextView tv_overseas_quhao;

    private String Str_Code, Str_Phone, Str_Psw, Str_Psw_config, phoneId, Str_Zhanghao;
    private boolean isAgree = true;
    private String Str_Guojiama = "86";  //默认中国

    private boolean isCountry = true, isPhone = false, isCode = false, isZhangHao = false, isPsw = false, isPswConfig = false;
    private ArrayList<User> list = null;

    @Override
    public int initResource() {
        return R.layout.activity_register;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "注册", "", true);

        MyTextWatch();

        requestHandleArrayList.add(requestAction.p_countryCode_three(this));  //查询所有国家
    }

    @OnClick({R.id.ll_overseas_area, R.id.rl_reg_overseas_agreement, R.id.btn_register_overseas,
            R.id.tv_reg_overseas_agreement, R.id.tv_reg_overseas_code})
    @Override
    public void onClick(View view) {
        Str_Phone = et_reg_overseas_phone.getText().toString().trim();
        switch (view.getId()) {
            //跳转地区
            case R.id.ll_overseas_area:

                if (list != null) {
                    Intent intent1 = new Intent(mContext, SelectCountriesActivity.class);
                    intent1.putExtra("type", "注册");
                    intent1.putExtra("list", list);
                    startActivityForResult(intent1, 666);
                } else {
                    MyToast.getInstance().showToast(mContext, "数据重新请求中...");
                    requestHandleArrayList.add(requestAction.p_countryCode_three(this));  //查询所有国家
                }
                break;

            //获取验证码
            case R.id.tv_reg_overseas_code:
                if (TextUtils.isEmpty(Str_Guojiama)) {
                    MyToast.getInstance().showToast(mContext, "请设置国家与地区");
                } else if (TextUtils.isEmpty(Str_Phone)) {
                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                } else {
                    if (et_overseas_area.getText().equals("中国")) {
                        if (Str_Phone.length() != 11) {
                            MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                        } else {
                            requestHandleArrayList.add(requestAction.YZM(RegisterActivity.this, Str_Phone, "注册验证码"));
                        }
                    } else {
                        requestHandleArrayList.add(requestAction.YZM(RegisterActivity.this, Str_Guojiama + "+" + Str_Phone, "海外注册验证码"));
                    }
                }
                break;

            //勾选同意协议
            case R.id.rl_reg_overseas_agreement:
                if (isAgree) {
                    im_reg_overseas_agreement.setImageResource(R.mipmap.ic_xuanzhong_no);
                    isAgree = false;
                } else {
                    im_reg_overseas_agreement.setImageResource(R.mipmap.ic_xuanzhong);
                    isAgree = true;
                }

                if (isCountry && isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                    btn_register_overseas.setEnabled(true);
                else
                    btn_register_overseas.setEnabled(false);
                break;

            //注册
            case R.id.btn_register_overseas:
                Str_Psw = et_reg_overseas_psw.getText().toString();
                Str_Psw_config = et_reg_overseas_confirm_psw.getText().toString();
                Str_Code = et_reg_overseas_code.getText().toString().trim().replace("", "");
                Str_Zhanghao = et_reg_overseas_zhanghao.getText().toString();

                if (TextUtils.isEmpty(Str_Phone)) {
                    MyToast.getInstance().showToast(mContext, "请输入手机号");
                } else if (TextUtils.isEmpty(Str_Code)) {
                    MyToast.getInstance().showToast(mContext, "请输入验证码");
                } else if (TextUtils.isEmpty(Str_Zhanghao)) {
                    MyToast.getInstance().showToast(mContext, "请设置账号");
                } else if (Str_Zhanghao.length() < 6) {
                    MyToast.getInstance().showToast(mContext, "请设置有效账号");
                } else if (TextUtils.isEmpty(Str_Psw)) {
                    MyToast.getInstance().showToast(mContext, "请输入新密码");
                } else if (Str_Psw.length() < 6 || Str_Psw.length() > 15) {
                    MyToast.getInstance().showToast(mContext, "请输入新密码6-15位");
                } else if (!StringUtils.judgePasswordFormat(Str_Psw)) {
                    MyToast.getInstance().showToast(mContext, "密码必须字母大小写与数字相结合");
                } else if (TextUtils.isEmpty(Str_Psw_config)) {
                    MyToast.getInstance().showToast(mContext, "请输入确认密码");
                } else if (!Str_Psw.equals(Str_Psw_config)) {
                    et_reg_overseas_confirm_psw.getText().clear();
                    MyToast.getInstance().showToast(mContext, "输入密码不一致，请重新输入");
                } else if (!isAgree) {
                    MyToast.getInstance().showToast(mContext, "请勾选《用户注册协议、用户隐私政策》");
                } else {
                    checkPermission(new CheckPermListener() {
                        @Override
                        public void superPermission() {
                            phoneId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            if (phoneId != null) {
                                editor.putString(ConstantUtlis.SP_PHENEID, phoneId);
                            } else {
                                phoneId = "";
                                editor.putString(ConstantUtlis.SP_PHENEID, "");
                            }
                            editor.commit();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("手机ID", phoneId);
                            CommonUtlis.updataMap(ConstantUtlis.SP_PHONEINFO_JSON, hashMap);

                            if (et_overseas_area.getText().equals("中国")) {
                                if (Str_Phone.length() != 11) {
                                    MyToast.getInstance().showToast(mContext, "请输入有效手机号");
                                } else {
                                    requestHandleArrayList.add(requestAction.Overseas_Register(RegisterActivity.this,
                                            Str_Phone, Str_Code, Str_Zhanghao, MD5Utlis.Md5(Str_Psw), MD5Utlis.Md5(Str_Psw_config)));
                                }
                            } else {
                                String phone = Str_Guojiama + "+" + Str_Phone;
                                requestHandleArrayList.add(requestAction.Overseas_Register(RegisterActivity.this,
                                        phone, Str_Code, Str_Zhanghao, MD5Utlis.Md5(Str_Psw), MD5Utlis.Md5(Str_Psw_config)));
                            }
                        }
                    }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);
                }
                break;

            //跳转协议
            case R.id.tv_reg_overseas_agreement:
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", ConstantUtlis.SHOP_OVERSEAS_REGISTER_URL);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x666) {
            Str_Guojiama = data.getStringExtra("国家码");
            et_overseas_area.setText(data.getStringExtra("国家与地区"));
            tv_overseas_quhao.setText("+" + data.getStringExtra("国家码"));

            if ("中国".equals(data.getStringExtra("国家与地区"))) {
                et_reg_overseas_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            } else {
                et_reg_overseas_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            }
            et_reg_overseas_phone.setText("");
        }
    }

    /**
     * 为edittext添加内容是否为空判断
     */
    private void MyTextWatch() {
        et_overseas_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Str_Guojiama.length() == 0)
                    isCountry = false;
                else isCountry = true;

                if (isCountry && isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                    btn_register_overseas.setEnabled(true);
                else
                    btn_register_overseas.setEnabled(false);
            }
        });
        //手机号
        et_reg_overseas_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isPhone = false;
                else isPhone = true;

                if (isCountry && isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                    btn_register_overseas.setEnabled(true);
                else
                    btn_register_overseas.setEnabled(false);
            }
        });
        //验证码
        et_reg_overseas_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isCode = false;
                else isCode = true;

                if (isCountry && isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                    btn_register_overseas.setEnabled(true);
                else
                    btn_register_overseas.setEnabled(false);
            }
        });
        //账号
        et_reg_overseas_zhanghao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isZhangHao = false;
                else
                    isZhangHao = true;

                if (isCountry && isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                    btn_register_overseas.setEnabled(true);
                else
                    btn_register_overseas.setEnabled(false);
            }
        });
        //密码
        et_reg_overseas_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isPsw = false;
                else isPsw = true;

                if (isCountry && isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                    btn_register_overseas.setEnabled(true);
                else
                    btn_register_overseas.setEnabled(false);
            }
        });
        //确认密码
        et_reg_overseas_confirm_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    isPswConfig = false;
                else isPswConfig = true;

                if (isCountry && isPhone && isCode && isZhangHao && isPsw && isPswConfig && isAgree)
                    btn_register_overseas.setEnabled(true);
                else
                    btn_register_overseas.setEnabled(false);
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, final JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess: " + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_P_COUNTRYCODE_THREE:
                list = new ArrayList<>();
                JSONArray jsonArray = response.getJSONArray("国家列表");
                User user;
                for (int i = 0; i < jsonArray.length(); i++) {
                    user = new User(jsonArray.getJSONObject(i).getString("中文名"), jsonArray.getJSONObject(i).getString("国家码"));
                    list.add(user);
                }
                break;

            case RequestAction.TAG_YZM:
                MyToast.getInstance().showToast(this, "验证码已发送");

                tv_reg_overseas_code.setClickable(false);
                tv_reg_overseas_code.setText(i + "秒后重试");
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

            case RequestAction.TAG_OVERSEAS_REGISTER:
                DialogUtlis.oneBtnNormal(getmDialog(), "提示", "注册成功", "确定", false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        L.e(response.toString());
                        editor.putString(ConstantUtlis.SP_BANGDINGPHONE, JSONUtlis.getString(response, "手机号"));
                        editor.putString(ConstantUtlis.SP_HAIWAIZHUCE, JSONUtlis.getString(response, "是否海外注册"));
                        editor.putString(ConstantUtlis.SP_LOGINTYPE, "成功");
                        editor.putString(ConstantUtlis.SP_USERPHONE, JSONUtlis.getString(response, "账号"));
                        editor.putString(ConstantUtlis.SP_USERSMRZ, JSONUtlis.getString(response, "实名认证"));
                        editor.putString(ConstantUtlis.SP_RANDOMCODE, JSONUtlis.getString(response, "随机码"));
                        editor.putString(ConstantUtlis.SP_USERIMG, JSONUtlis.getString(response, "头像"));
                        editor.putString(ConstantUtlis.SP_NICKNAME, JSONUtlis.getString(response, "昵称"));
                        editor.putString(ConstantUtlis.SP_GENDER, JSONUtlis.getString(response, "性别"));
                        editor.putString(ConstantUtlis.SP_LOCATION, JSONUtlis.getString(response, "地址"));
                        editor.putString(ConstantUtlis.SP_MYPWD, JSONUtlis.getString(response, "是否设置过支付密码"));
                        editor.putString(ConstantUtlis.SP_ONLYID, JSONUtlis.getString(response, "唯一id"));
                        editor.putString(ConstantUtlis.SP_SUPERMERCHANT, JSONUtlis.getString(response, "是否显示超级商家"));
                        editor.putString(ConstantUtlis.SP_KEFUCENTER, JSONUtlis.getString(response, "是否显示客服中心"));
                        editor.putString(ConstantUtlis.SP_TRUENAME, JSONUtlis.getString(response, "真实名"));
                        editor.putString(ConstantUtlis.SP_LOGINZHANHAO, JSONUtlis.getString(response, "账号"));
                        editor.putString(ConstantUtlis.SP_SHIFOUBANGDINGWX, JSONUtlis.getString(response, "是否绑定微信"));

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("账号", JSONUtlis.getString(response, "账号"));
                        hashMap.put("随机码", JSONUtlis.getString(response, "随机码"));
                        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
                        CommonUtlis.saveMap(ConstantUtlis.SP_REQUESTINFO_JSON, hashMap);
                        editor.commit();
                        imDB(JSONUtlis.getString(response, "账号"));
                        imSocket();
                        // 获取图片清单
//                        InfoRequestUtils.getInstance().getPictureList();

                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("selectFlag", 1);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

        }
    }

    private void imDB(String account) {
        IMDBManager.getInstance(mContext, account);
    }

    // socket认证
    private void imSocket() {
        if (!TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_USERPHONE, ""))) {
            SocketListener.getInstance().connectionRenZheng(preferences.getString(ConstantUtlis.SP_USERPHONE, ""), preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));
        }
    }

    private int i = 60;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tv_reg_overseas_code.setText(i + "秒后重试");
            } else if (msg.what == -8) {
                tv_reg_overseas_code.setText("获取验证码");
                tv_reg_overseas_code.setClickable(true);
                i = 60;
            }
        }
    };

}
