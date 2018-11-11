package com.gloiot.hygo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.loopj.android.http.RequestHandle;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
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

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by hygo01 on 16/12/5.
 */

public abstract class BaseFragment extends Fragment implements OnDataListener, EasyPermissions.PermissionCallbacks {
    private static final String TAG = "BaseFragment";

    protected Context mContext;

    protected RequestAction requestAction;

    protected ArrayList<RequestHandle> requestHandleArrayList = new ArrayList<>();

    protected SharedPreferences preferences;

    protected SharedPreferences.Editor editor;

    protected MDialog mDialog;

    /**
     * 调用该方法 该Fragment的Activity必须是继承BaseActivity
     */
    public MDialog getmDialog() {
        if (mDialog != null){
            return mDialog;
        } else {
            mDialog = ((BaseActivity) getActivity()).getmDialog();
        }
        return mDialog;
    }

    public void dismissDialog(){
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        editor = preferences.edit();
        mContext = getActivity();
        requestAction = new RequestAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelRequestHandle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

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
        L.e(TAG, "onSuccess: " + response);
        if (showLoad == 0 || showLoad == 2) {
            LoadDialog.dismiss(mContext);
        }
        try {
            if (response.getString("状态").equals("成功")) {
                requestSuccess(requestTag, response, showLoad);
            } else if (response.getString("状态").equals("随机码不正确")) {
                CommonUtlis.clearPersonalData();//清除账号个人数据，重置登录状态等
                DialogUtlis.oneBtnNormal(getmDialog(), "该账号在其他设备登录\n请重新登录", "确定", false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                        ((Activity) mContext).overridePendingTransition(R.anim.activity_open, 0);
                    }
                });
            } else {
                MToast.showToast(response.getString("状态"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            DialogUtlis.oneBtnNormal(mContext, "数据异常-1\n请稍后再试", e);
        } catch (Exception e) {
            e.printStackTrace();
//            DialogUtlis.oneBtnNormal(mContext, "数据异常-2\n请稍后再试", e);
        }
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
        L.e(TAG, "onCancel: " + requestTag);
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
        L.e("跳转执行：" + this.getClass().getName());
        if (preferences.getString(ConstantUtlis.SP_LOGINTYPE, "").equals("成功")) {
            return true;
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_open, 0);
            return false;
        }
    }


    public boolean checkIsSetPwd() {
        if (preferences.getString(ConstantUtlis.SP_MYPWD, "否").equals("否")) {
            CommonUtlis.toSetPwd(this.mContext, getmDialog());
            return false;
        } else {
            return true;
        }
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
        if (requestCode == 123) {
            //设置返回
            if (EasyPermissions.hasPermissions(mContext, mPerms)) {
                if (mListener != null)
                    mListener.superPermission();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
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
