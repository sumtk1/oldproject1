package com.gloiot.hygo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.loopj.android.http.RequestHandle;
import com.zyd.wlwsdk.autolayout.AutoLayoutActivity;
import com.zyd.wlwsdk.server.network.OnDataListener;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MToast;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;
import com.zyd.wlwsdk.widge.LoadDialog;
import com.zyd.wlwsdk.widge.MDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by hygo01 on 16/10/19.
 * Activity基础类
 */

public abstract class BaseActivity extends AutoLayoutActivity implements OnDataListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "BaseActivity";

    protected Context mContext;

    protected RequestAction requestAction;

    protected ArrayList<RequestHandle> requestHandleArrayList = new ArrayList<>();

    protected SharedPreferences preferences;

    protected SharedPreferences.Editor editor;

    private static long lastTime; // 上一次点击时间

    private long delay = 1000; // 默认1秒

    public void setRequestErrorCallback(RequestErrorCallback requestErrorCallback) {
        this.requestErrorCallback = requestErrorCallback;
    }

    protected RequestErrorCallback requestErrorCallback;

    protected MDialog mDialog;

    public MDialog getmDialog() {
        if (mDialog != null){
            return mDialog;
        } else {
            mDialog = new MDialog(this);
        }
        return mDialog;
    }


    public void dismissDialog(){
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        editor = preferences.edit();
        mContext = this;
        requestAction = new RequestAction();
        App.getInstance().mActivityStack.addActivity(this);
        setContentView(initResource());
        ButterKnife.bind(this);

        String phone_type =preferences.getString(ConstantUtlis.SP_PHENENAME,"");

        if(phone_type.contains("vivo")||phone_type.contains("Vivo")){
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#B4B4B4"),true);
        }else {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"),true);
        }

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelRequestHandle();
        requestErrorCallback = null;
        App.getInstance().mActivityStack.removeActivity(this);

        this.mListener = null;
        this.mPerms = null;
        dismissDialog();
    }


    /**
     * 初始化布局资源文件
     */
    public abstract int initResource();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 请求开始
     *
     * @param requestTag 请求标志
     */
    @Override
    public void onStart(int requestTag, int showLoad) {
        L.d(TAG, "onStart: " + requestTag);
        if (showLoad == 0 || showLoad == 1) {
            LoadDialog.show(mContext, requestHandleArrayList);
        }
    }

    /**
     * 请求成功(过滤 状态=成功)
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     * @throws JSONException
     */
    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

    }

    /**
     * 请求成功
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     */
    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        L.d(TAG, "requestTag: " + requestTag + " onSuccess: " + response);
        if (showLoad == 0 || showLoad == 2) {
            LoadDialog.dismiss(mContext);
        }
        try {
            if (response.getString("状态").equals("成功")) {
                requestSuccess(requestTag, response, showLoad);
            } else if (response.getString("状态").equals("随机码不正确")) {
                if (!"成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))){
                    return;
                }
                CommonUtlis.clearPersonalData();//清除账号个人数据，重置登录状态等
                DialogUtlis.oneBtnNormal(getmDialog(), "该账号在其他设备登录\n请重新登录", "确定", false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
//                        App.getInstance().mActivityStack.getLastActivity().finish();
//                        System.exit(0);
                        SocketListener.getInstance().signoutRenZheng();
                        IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).ClearnData();
                        ((Activity) mContext).overridePendingTransition(R.anim.activity_open, 0);
                    }
                });
            } else {
                if (requestErrorCallback != null) {
                    requestErrorCallback.requestErrorcallback(requestTag, response);
                } else {
                    MToast.showToast(response.getString("状态"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            DialogUtlis.oneBtnNormal(mContext, "数据异常-1\n请稍后再试", e);
        } catch (Exception e) {
            e.printStackTrace();
//            DialogUtlis.oneBtnNormal(mContext, "数据异常-2\n请稍后再试", e);
        }
    }

    // 请求状态！=成功回调
    public interface RequestErrorCallback {
        void requestErrorcallback(int requestTag, JSONObject response) throws Exception;
    }

    /**
     * 请求失败
     *
     * @param requestTag    请求标志
     * @param errorResponse 错误请求返回
     */
    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        L.e(TAG, "onFailure: " + requestTag + " errorResponse: " + errorResponse);
        MToast.showToast("请求超时,请检查你的网络!");
        LoadDialog.dismiss(mContext);
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {

    }

    /**
     * 取消网络请求
     */
    public void cancelRequestHandle() {
        if (requestHandleArrayList.size() != 0) {
            for (int i = 0; i < requestHandleArrayList.size(); i++) {
                requestHandleArrayList.get(i).cancel(true);
            }
        }
    }


    //判断是否登录
    public boolean check_login() {
        if (preferences.getString(ConstantUtlis.SP_LOGINTYPE, "").equals("成功")) {
            return true;
        } else {
            return false;
        }
    }


    //判断是否登录再跳转
    public boolean check_login_tiaozhuang() {
        if (preferences.getString(ConstantUtlis.SP_LOGINTYPE, "").equals("成功")) {
            return true;
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.activity_open, 0);
            return false;
        }
    }



    /**
     * 判断是否超过限制时间
     *
     * @param v
     * @return
     */
    public boolean onMoreClick(View v) {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;
        if (time < delay) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * 权限回调接口
     */
    private CheckPermListener mListener;
    private String[] mPerms;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void checkPermission(CheckPermListener mListener, int resString, String... mPerms) {
        this.mListener = mListener;
        this.mPerms = mPerms;

        if (EasyPermissions.hasPermissions(mContext, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            EasyPermissions.requestPermissions(this, getString(resString), 123, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //设置返回
            if (mContext == null || mPerms == null || mPerms.length == 0) {
                return;
            }
            if (EasyPermissions.hasPermissions(mContext, mPerms)) {
                if (mListener != null)
                    mListener.superPermission();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (mContext == null || mPerms == null || mPerms.length == 0) {
            return;
        }
        //同意了某些权限可能不是全部
        if (EasyPermissions.hasPermissions(mContext, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("设置")
                    .setRationale(R.string.perm_tip)
                    .build().show();
        }
    }
}
