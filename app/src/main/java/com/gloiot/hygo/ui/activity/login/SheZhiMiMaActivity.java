package com.gloiot.hygo.ui.activity.login;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.InfoRequestUtils;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.MD5Utlis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

public class SheZhiMiMaActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.et_forgetpwd_newpwd)
    EditText etForgetpwdNewpwd;
    @Bind(R.id.et_forgetpwd_confirmpwd)
    EditText etForgetpwdConfirmpwd;
    private String shoujihao;

    @Override
    public int initResource() {
        return R.layout.activity_shezhimima;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "设置密码");
        Intent intent = getIntent();
        shoujihao = intent.getStringExtra("shoujihao");

    }

    @OnClick({R.id.bnt_confirm})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bnt_confirm:
                String pwd = etForgetpwdNewpwd.getText().toString();
                String Confirmpwd = etForgetpwdConfirmpwd.getText().toString();
                if (TextUtils.isEmpty(pwd) && pwd.length() < 6 || pwd.length() > 15) {
                    MyToast.getInstance().showToast(mContext, "请设置新密码6-15位");
                } else if (TextUtils.isEmpty(Confirmpwd)) {
                    MyToast.getInstance().showToast(mContext, "请输入确认密码");
                } else {
                    requestHandleArrayList.add(requestAction.WeChatPwd(SheZhiMiMaActivity.this, shoujihao, MD5Utlis.Md5(pwd)));
                }
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_WECHATPWD:
                editor.putString(ConstantUtlis.SP_LOGINTYPE, "成功");
                editor.putString(ConstantUtlis.SP_USERPHONE, shoujihao);
                editor.putString(ConstantUtlis.SP_USERSMRZ, JSONUtlis.getString(response, "实名认证"));
                editor.putString(ConstantUtlis.SP_RANDOMCODE, JSONUtlis.getString(response, "随机码"));
                editor.putString(ConstantUtlis.SP_USERIMG, JSONUtlis.getString(response, "头像"));
                editor.putString(ConstantUtlis.SP_USERNAME, JSONUtlis.getString(response, "姓名"));
                editor.putString(ConstantUtlis.SP_NICKNAME, JSONUtlis.getString(response, "昵称"));
                editor.putString(ConstantUtlis.SP_TOKEN, JSONUtlis.getString(response, "token"));

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("账号", shoujihao);
                hashMap.put("随机码", JSONUtlis.getString(response, "随机码"));
                hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
                CommonUtlis.saveMap(ConstantUtlis.SP_REQUESTINFO_JSON, hashMap);
                editor.commit();

                // 获取图片清单
//                InfoRequestUtils.getInstance().getPictureList();

                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();


                break;
        }
    }
}
