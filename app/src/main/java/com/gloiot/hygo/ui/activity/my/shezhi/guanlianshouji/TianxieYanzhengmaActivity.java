package com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.InfoRequestUtils;
import com.zyd.wlwsdk.utils.MyToast;
import com.gloiot.hygo.widget.TimeButton;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;
import com.zyd.wlwsdk.widge.YzmInputView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;

import static com.gloiot.hygo.ui.activity.login.LoginActivity.TAG_NUMLOGIN;

/**
 * 填写验证码
 */
public class TianxieYanzhengmaActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {

    @Bind(R.id.tv_phonenum)
    TextView mPhoneNum;
    @Bind(R.id.yzmv_yzm)
    YzmInputView mYzmvYzm;
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.tb_prompt_message)
    TimeButton mTbPromptMessage;
    @Bind(R.id.tv_send_again)
    TextView mTvSendAgain;

    private String mNewPhoneNum;

    private boolean isFirstRequestYZM = true;//是否是第一次请求验证码

    //切换账号功能需要的数据
    private boolean storage;
    private String Str_zhanghao;
    private String Str_mima;

    /**
     * 表示是从进来干嘛的-- 更改手机 & 验证是否常用手机
     */
    private String type = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mTbPromptMessage.onCreate(savedInstanceState);//TimeButton要和Activity的生命周期同步
    }

    @Override
    public int initResource() {
        return R.layout.activity_tianxie_yanzhengma;
    }

    @Override
    public void initData() {
        //切换账号功能需要的数据
        storage = getIntent().getBooleanExtra("storage", false);
        if (storage) {
            Str_zhanghao = getIntent().getStringExtra("zhanghao");
            Str_mima = getIntent().getStringExtra("mima");
        }


        mNewPhoneNum = getIntent().getStringExtra("newPhoneNum");
        Log.e("mNewPhoneNum000", mNewPhoneNum);
        String phone = mNewPhoneNum;
        type = getIntent().getStringExtra("type");
        try {
            if (phone.length() != 11) {
                phone = "****" + phone.substring(phone.length() - 4, phone.length());
            } else {
                phone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        mPhoneNum.setText(phone);
        if (type.equals("change_phone")) {
            CommonUtlis.setTitleBar(this, "填写验证码");
            Log.e("mNewPhoneNum11", mNewPhoneNum);
            requestHandleArrayList.add(requestAction.YZM(this, mNewPhoneNum, "短信验证码"));
        } else if (type.equals("common_phone")) {
            CommonUtlis.setTitleBar(this, "身份验证");
            Log.e("mNewPhoneNum22", mNewPhoneNum);
            requestHandleArrayList.add(requestAction.YZM(this, mNewPhoneNum, "登录验证码"));
        }
        mTbPromptMessage.setTextAfter("s后重新获取验证码").setTextBefore("收不到验证码？重发短信").setLenght(60 * 1000);
        mTbPromptMessage.setOnClickListener(this);

        setListener();
    }

    /**
     * 验证码输入监听
     */
    private void setListener() {
        mYzmvYzm.setDisplayPasswords(true);
        mYzmvYzm.setPwdInputViewType(YzmInputView.ViewType.UNDERLINE);
        mYzmvYzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 4) {
                    mTvNext.setEnabled(true);
                    mTvNext.setBackgroundResource(R.drawable.bg_btn_fillet_violet);
                    mTvNext.setTextColor(getResources().getColor(R.color.white));
                    mTvNext.setOnClickListener(TianxieYanzhengmaActivity.this);
                } else {
                    mTvNext.setEnabled(false);
                    mTvNext.setBackgroundResource(R.drawable.bg_no_but_fillet);
                    mTvNext.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                String yzm = mYzmvYzm.getText().toString();
                if (type.equals("change_phone")) {
                    requestHandleArrayList.add(requestAction.UpdatePhoneNum(TianxieYanzhengmaActivity.this, mNewPhoneNum, yzm));
                } else if (type.equals("common_phone")) {
                    App.getInstance().mActivityStack.customAddActivity(this);
                    String phone = mNewPhoneNum;
                    String phoneid = preferences.getString(ConstantUtlis.SP_PHENEID, "");
                    requestHandleArrayList.add(requestAction.p_authentication_three(TianxieYanzhengmaActivity.this, phone, yzm, phoneid));
                }

                break;
            case R.id.tb_prompt_message:
                mTbPromptMessage.setCondition(true);
                if (type.equals("change_phone")) {
                    requestHandleArrayList.add(requestAction.YZM(this, mNewPhoneNum, "短信验证码"));
                } else if (type.equals("common_phone")) {
                    requestHandleArrayList.add(requestAction.YZM(this, mNewPhoneNum, "登录验证码"));
                }
                // requestHandleArrayList.add(requestAction.YZM(this, mNewPhoneNum, "短信验证码"));
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_YZM:
                if (isFirstRequestYZM) {//第一次进来时直接请求成功要求主动触发倒计时，但是之后就是用户点击事件触发倒计时。
                    mTbPromptMessage.startTime();//开始倒计时
                    isFirstRequestYZM = false;
                }
                L.e("更换关联手机号验证码", response.toString());
                break;
            case RequestAction.TAG_UPDATEPHONENUM:
                SharedPreferencesUtils.setString(ConstantUtlis.SP_BANGDINGPHONE, mNewPhoneNum);//存储新的关联手机号
                startActivity(new Intent(TianxieYanzhengmaActivity.this, XiugaiChenggongActivity.class));
                break;
            case RequestAction.TAG_COMMON_PHONE:
                if (storage) {
                    CommonUtlis.clearPersonalData();//清除个人数据，重置登录状态等
                    SocketListener.getInstance().signoutRenZheng();
                    IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).ClearnData();

                    //SQLite
                    SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("shang_cheng.db", Context.MODE_PRIVATE, null);
                    sqLiteDatabase.execSQL("CREATE TABLE if not exists qiehuan_zhanghao (zhanghao VARCHAR PRIMARY KEY, mima VARCHAR, imgUrl VARCHAR)");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("zhanghao", Str_zhanghao);
                    contentValues.put("mima", Str_mima);
                    contentValues.put("imgUrl", JSONUtlis.getString(response, "头像"));
                    sqLiteDatabase.replace("qiehuan_zhanghao", null, contentValues);
                }

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
//                InfoRequestUtils.getInstance().getPictureList();

                Intent intent = new Intent();
                intent.putExtra("type", "成功");
                intent.putExtra("onlyid", JSONUtlis.getString(response, "唯一id"));
                setResult(TAG_NUMLOGIN, intent);
                finish();

               /* Intent intent = new Intent(TianxieYanzhengmaActivity.this, MainActivity.class);
                startActivity(intent);
                App.getInstance().mActivityStack.customRemoveActivity();*/
                break;
        }

    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case RequestAction.TAG_YZM:
                mTbPromptMessage.setText("");
//                mTbPromptMessage.setCondition(false);
                L.e("更换关联手机号验证码状态不成功", response.toString());
                break;
            case RequestAction.TAG_COMMON_PHONE:
                MyToast.getInstance().showToast(mContext, response.getString("状态"));
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


    @Override
    protected void onDestroy() {
        mTbPromptMessage.onDestroy();
        super.onDestroy();
    }
}
