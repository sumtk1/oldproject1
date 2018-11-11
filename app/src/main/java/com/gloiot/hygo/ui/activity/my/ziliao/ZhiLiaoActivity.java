package com.gloiot.hygo.ui.activity.my.ziliao;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.App;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.SelectPCActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.SheZhiActivity;
import com.gloiot.hygo.ui.activity.my.ziliao.shimingrenzheng.RenZheng01Activity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.zyd.wlwsdk.server.AliOss.MPhoto;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

import static com.gloiot.hygo.R.id.img_mydata_tx;

public class ZhiLiaoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(img_mydata_tx)
    ImageView imgMydataTx;
    @Bind(R.id.tv_mydata_nicheng)
    TextView tvMydataNicheng;
    @Bind(R.id.tv_mydata_xingbie)
    TextView tvMydataXingbie;
    @Bind(R.id.tv_mydata_shengri)
    TextView tvMydataShengri;
    @Bind(R.id.tv_mydata_shoujihao)
    TextView tvMydataShoujihao;
    @Bind(R.id.tv_mydata_renzheng)
    TextView tvMydataRenzheng;
    @Bind(R.id.tv_mydata_weizhi)
    TextView tvMydataWeizhi;
    @Bind(R.id.tv_mydata_qianming)
    TextView tvMydataQianming;
    @Bind(R.id.rl_mydata_shoujihao)
    RelativeLayout rlMydataShoujihao;
    @Bind(R.id.rl_mydata_renzheng)
    RelativeLayout rlMydataRenzheng;
    @Bind(R.id.tv_zhanghao)
    TextView tvzhanghao;
    @Bind(R.id.rl_mydata_erweima)
    RelativeLayout rl_mydata_erweima;

    private String contents = "";
    private TextView mTextView;
    private ImageView mImageView;
    private String default_url_tx, shoujihao, myPic;
    private int sex;

    @Override
    public int initResource() {
        return R.layout.activity_my_ziliao;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "个人资料");
        // 头像
        PictureUtlis.loadCircularImageViewHolder(mContext, preferences.getString(ConstantUtlis.SP_USERIMG, ""), R.mipmap.ic_head, imgMydataTx);
        // 昵称
        tvMydataNicheng.setText(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
        // 账号(手机号)
        String my_phone = preferences.getString(ConstantUtlis.SP_BANGDINGPHONE, "");
        tvMydataShoujihao.setText(my_phone);
        //帐号
        tvzhanghao.setText(preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
        //性别
        tvMydataXingbie.setText(preferences.getString(ConstantUtlis.SP_GENDER, ""));
        //生日
        tvMydataShengri.setText(preferences.getString(ConstantUtlis.SP_BIRTHDAY, ""));
        //认证状态
        String renzhengState = preferences.getString(ConstantUtlis.SP_USERSMRZ, "");
        tvMydataRenzheng.setText(renzhengState);

        //如果已实名待审核不能点击
        if ("已认证".equals(renzhengState)) {
            rlMydataRenzheng.setClickable(false);
        } else {
            rlMydataRenzheng.setClickable(true);
        }
/*        if (preferences.getString(ConstantUtlis.SP_HAIWAIZHUCE, "").equals("是")) { //是否海外注册
            rl_mydata_erweima.setVisibility(View.GONE);
        } else {
            rl_mydata_erweima.setVisibility(View.VISIBLE);
        }
        if (preferences.getString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "").equals("否")) { //是否绑定微信
            rl_mydata_erweima.setVisibility(View.GONE);
        } else {
            rl_mydata_erweima.setVisibility(View.VISIBLE);
        }*/

        //位置
        tvMydataWeizhi.setText(preferences.getString(ConstantUtlis.SP_LOCATION, ""));
        //个性签名
        tvMydataQianming.setText(preferences.getString(ConstantUtlis.SP_PERSONALIZEDSIGNATURE, ""));

        requestData();//还是要求请求数据，不显示加载框，有更改就更新。

    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
        // 关联微信
        String wxCode = preferences.getString(ConstantUtlis.SP_WXCODE, null);
        if (!TextUtils.isEmpty(wxCode)) {
            requestHandleArrayList.add(requestAction.WXBinding(this, wxCode));
            editor.putString(ConstantUtlis.SP_WXCODE, null);
            editor.commit();
        } else {
            LoadDialog.dismiss(mContext);
        }
    }

    /**
     * 请求数据
     */
    private void requestData() {
        requestHandleArrayList.add(requestAction.GetMyData(this));
    }

    /**
     * 修改资料
     */
    private void updateMyData(final ImageView imageview, final TextView textview, final String content, HashMap<String, Object> hashMap) {
        requestHandleArrayList.add(requestAction.UpdateMyData(ZhiLiaoActivity.this, hashMap));
        contents = content;
        mTextView = textview;
        mImageView = imageview;
    }

    @OnClick({R.id.rl_mydata_touxiang, R.id.rl_mydata_nicheng, R.id.rl_mydata_xingbie
            , R.id.rl_mydata_shengri, R.id.rl_mydata_shoujihao, R.id.rl_mydata_erweima
            , R.id.rl_mydata_renzheng, R.id.rl_mydata_weizhi, R.id.rl_mydata_qianming})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mydata_touxiang:

                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {

                        MPhoto.Builder builder = new MPhoto.Builder()
                                .init(mContext)
                                .setTitle("头像选择")
                                .setResultCallback(new MPhoto.OnResultCallback() {
                                    @Override
                                    public void onSuccess(final String data) {
                                        ZhiLiaoActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                myPic = data;
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("头像", data);
                                                updateMyData(imgMydataTx, null, data, hashMap);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(String errorMsg) {
                                        Log.e("设置头像失败", errorMsg);
                                    }
                                });
                        MPhoto.init(builder);

                    }
                }, R.string.perm_camera, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.rl_mydata_nicheng:
                Intent intent = new Intent(ZhiLiaoActivity.this, SingleLineInputActivity.class);
                intent.putExtra("title", "昵称");
                intent.putExtra("currentString", tvMydataNicheng.getText().toString());
                startActivityForResult(intent, 1);

                break;
            case R.id.rl_mydata_xingbie:

                DialogUtlis.customSex(getmDialog(), sex, new MDialogInterface.SexChoiceInter() {
                    @Override
                    public void onClick(int position) {
                        sex = position;
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("性别", sex == 0 ? "男" : "女");
                        updateMyData(null, tvMydataXingbie, sex == 0 ? "男" : "女", hashMap);
                    }
                });

                break;
            case R.id.rl_mydata_shengri:
                DialogUtlis.customDateView(getmDialog(), new MDialogInterface.DataInter() {
                    @Override
                    public void data(int year, int month, int day) {
                        String birthday = year + "-" + month + "-" + day;
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("生日", birthday);
                        updateMyData(null, tvMydataShengri, birthday, hashMap);
                    }
                });
                break;
            case R.id.rl_mydata_shoujihao:

                break;
            case R.id.rl_mydata_erweima:
                if (check_login_tiaozhuang()) {
                    if (preferences.getString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "").equals("否")) {
                        DialogUtlis.twoBtnNormal(getmDialog(), "提示", "未绑定微信,请先绑定微信", true, "取消", "去绑定",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dismissDialog();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                        getWxInfo();
                                    }
                                });
                    } else {
                        startActivity(new Intent(ZhiLiaoActivity.this, FenXiangEWeiMaActivity.class));
                    }
                }

                break;
            case R.id.rl_mydata_renzheng:
                startActivity(new Intent(ZhiLiaoActivity.this, RenZheng01Activity.class));
                break;
            case R.id.rl_mydata_weizhi:
                Intent intent2 = new Intent(ZhiLiaoActivity.this, SelectPCActivity.class);
                startActivityForResult(intent2, 3);
                break;
            case R.id.rl_mydata_qianming:
                Intent intent1 = new Intent(ZhiLiaoActivity.this, SingleLineInputActivity.class);
                intent1.putExtra("title", "个性签名");
                intent1.putExtra("currentString", tvMydataQianming.getText().toString());
                startActivityForResult(intent1, 2);
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_WXBINDING:
                editor.putString(ConstantUtlis.SP_SHIFOUBANGDINGWX, "是");
                editor.commit();
                MyToast.getInstance().showToast(mContext, "关联成功");
                break;
            case RequestAction.TAG_GETMYDATA:
                SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_USERIMG, response.getString("头像"));
                PictureUtlis.loadCircularImageViewHolder(mContext, response.getString("头像"), R.mipmap.ic_head, imgMydataTx);
                if (!TextUtils.isEmpty(response.getString("昵称"))) {
                    tvMydataNicheng.setText(response.getString("昵称"));
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_NICKNAME, response.getString("昵称"));
                }
                if (!TextUtils.isEmpty(response.getString("性别"))) {
                    sex = response.getString("性别").equals("男") ? 0 : 1;
                    tvMydataXingbie.setText(response.getString("性别"));
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_GENDER, response.getString("性别"));
                    if (response.getString("性别").equals("无")) {
                        tvMydataXingbie.setText("");
                    }
                }
                if (!TextUtils.isEmpty(response.getString("生日"))) {
                    tvMydataShengri.setText(response.getString("生日"));
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_BIRTHDAY, response.getString("生日"));
                }
                if (!TextUtils.isEmpty(response.getString("个性签名"))) {
                    tvMydataQianming.setText(response.getString("个性签名"));
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_PERSONALIZEDSIGNATURE, response.getString("个性签名"));
                }
                if (!TextUtils.isEmpty(response.getString("位置"))) {
                    tvMydataWeizhi.setText(response.getString("位置"));
                    SharedPreferencesUtils.setString(mContext, ConstantUtlis.SP_LOCATION, response.getString("位置"));
                }
                if (!TextUtils.isEmpty(response.getString("手机号"))) {
                    tvMydataShoujihao.setText(response.getString("手机号"));
                    rlMydataShoujihao.setClickable(false);
                }
                default_url_tx = response.getString("头像");
                shoujihao = response.getString("手机号");

                if (!TextUtils.isEmpty(response.getString("认证状态"))) {
                    String smrz = response.getString("认证状态");
                    tvMydataRenzheng.setText(smrz);
                    editor.putString(ConstantUtlis.SP_USERSMRZ, smrz).commit();

                    //如果已实名待审核不能点击
                    if ("已认证".equals(smrz)) {
                        rlMydataRenzheng.setClickable(false);
                        tvMydataRenzheng.setTextColor(Color.parseColor("#965aff"));
                    } else {
                        rlMydataRenzheng.setClickable(true);
                        tvMydataRenzheng.setTextColor(Color.parseColor("#FF6D63"));
                    }

                }
                break;
            case RequestAction.TAG_UPDATEYMYDATA:
                if (mTextView != null) {
                    mTextView.setText(contents);
                    if (mTextView == tvMydataNicheng) {//昵称
                        editor.putString(ConstantUtlis.SP_NICKNAME, contents);
                        editor.commit();

//                        UserInfo info = new UserInfo();
//                        info.setName(ConstantUtlis.SP_NICKNAME);
//                        info.setId(ConstantUtlis.SP_USERPHONE);
//                        info.setUrl(ConstantUtlis.SP_USERIMG);
//                        Log.e("user--22", info.toString());
//                        UserInfoCache.getInstance(mContext).upData(info.getId(), info);
                    }
                    if (mTextView == tvMydataXingbie) {//性别
                        editor.putString(ConstantUtlis.SP_GENDER, contents);
                        editor.commit();
                        L.e("性别", contents);
                    }
                    if (mTextView == tvMydataShengri) {//生日
                        editor.putString(ConstantUtlis.SP_BIRTHDAY, contents);
                        editor.commit();
                        L.e("生日", contents);
                    }
                    if (mTextView == tvMydataWeizhi) {//位置
                        editor.putString(ConstantUtlis.SP_LOCATION, contents);
                        editor.commit();
                        L.e("位置", contents);
                    }
                    if (mTextView == tvMydataQianming) {//个性签名
                        editor.putString(ConstantUtlis.SP_PERSONALIZEDSIGNATURE, contents);
                        editor.commit();
                        L.e("个性签名", contents);
                    }
                }
                if (mImageView != null && mImageView == imgMydataTx) {
                    editor.putString(ConstantUtlis.SP_USERIMG, myPic);
                    editor.commit();
                    PictureUtlis.loadCircularImageViewHolder(mContext, myPic, R.mipmap.ic_head, imgMydataTx);
                }
                if (!TextUtils.isEmpty(shoujihao)) {
                    rlMydataShoujihao.setClickable(false);
                }
                UserInfo info = new UserInfo();
                info.setName(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
                info.setId(preferences.getString(ConstantUtlis.SP_USERPHONE, ""));
                info.setUrl(preferences.getString(ConstantUtlis.SP_USERIMG, ""));
                UserInfoCache.getInstance(mContext).upData(info.getId(), info, preferences.getString(ConstantUtlis.SP_USERPHONE, ""));

                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String updateNickName = data.getStringExtra("update");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("昵称", updateNickName);
                    updateMyData(null, tvMydataNicheng, updateNickName, hashMap);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    String updateSignature = data.getStringExtra("update");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("个性签名", updateSignature);
                    updateMyData(null, tvMydataQianming, updateSignature, hashMap);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    String updateLocation = data.getStringExtra("city");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("位置", updateLocation);
                    updateMyData(null, tvMydataWeizhi, updateLocation, hashMap);
                }
                break;
        }
    }


    /**
     * 获取微信信息
     */
    public void getWxInfo() {
        LoadDialog.show(mContext);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_authorization";
        App.getInstance().wxapi.sendReq(req);
    }
}
