package com.gloiot.hygo.ui.activity.web;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseFragment;
import com.gloiot.hygo.ui.activity.MainActivity;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.ui.activity.shopping.ShangPinXiangQingActivity;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.unionpay.UPPayAssistEx;
import com.zyd.wlwsdk.server.AliOss.MPhoto;
import com.zyd.wlwsdk.thirdpay.AliPay;
import com.zyd.wlwsdk.thirdpay.UnionPay;
import com.zyd.wlwsdk.thirdpay.WXPay;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MD5Utlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.StatusBarUtil;
import com.zyd.wlwsdk.webview.InnerWebView;
import com.zyd.wlwsdk.webview.WebFunction;
import com.zyd.wlwsdk.webview.WebManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.zyd.wlwsdk.webview.WebManage.TAG_WEBMANAGE_FAIL;
import static com.zyd.wlwsdk.webview.WebManage.TAG_WEBMANAGE_SUCCEESS;

public class WebFragment extends BaseFragment implements View.OnClickListener, WebManage.WebManageCall {

    @Bind(R.id.rl_title)
    RelativeLayout webTitle;    // 网页标题
    @Bind(R.id.img_title_back)
    ImageView imgTitleBack;     // 网页返回按钮
    @Bind(R.id.img_title_close)
    ImageView imgTitleClose;    // 网页关闭按钮
    @Bind(R.id.tv_title)
    TextView tvTitle;           // 标题
    @Bind(R.id.pb)
    ProgressBar pb;             // 进度条
    @Bind(R.id.tv_time)
    TextView tvTime;            // 倒计时
    @Bind(R.id.web)
    InnerWebView innerWebView;  // webview
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;

    private WebManage webManage;                        // web管理类
    private WebFunction webFunction;                    // web操作类
    private WebFunction.MyTimerTask myTimerTask;        // 倒计时时间
    private View view;

    public static final int TAG_ADDRESSBOOK_BACK = 100; // 获取通讯录联系人返回
    public static final int TAG_LOGIN_BACK = 101;       // 登录界面是否登录返回
    public static final int TAG_PAY_BACK = 102;         // 支付页面返回

    private String url;                     // 页面地址
    private String prepay_id, appid;        // 收银台所用
    private boolean unableGoBack = false;   // 页面不可返回
    private boolean isSearch = false;       // 是否展示搜索框

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, view);

        prepay_id = getArguments().getString("prepay_id", "");
        appid = getArguments().getString("appid", "");
        boolean showTitle = getArguments().getBoolean("showTitle", true);
        url = getArguments().getString("url", "");
        url = !url.contains("http") ? "http://" + url : url;
        Log.e("web-prepay_id", "" + prepay_id);
        Log.e("web-appid", "" + appid);
        Log.e("web-url", "" + url);
        if (!showTitle) {
            webTitle.setVisibility(View.GONE);  // 隐藏标题拦
        }
//        url = "file:///android_asset/webDemo.html";
        webFunction = new WebFunction(mContext);
        webManage = new WebManage();
        webManage.setWebManageCall(this);
        innerWebView.init(getActivity(), view, preferences.getString(ConstantUtlis.WEB_JS, ""), webManage, url);
        webManage.setWebView(innerWebView);

        return view;
    }

    @OnClick({R.id.img_title_back, R.id.img_title_close})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_title_back:
                finishPay();
                stopTimer();
                // 网页如果可以返回上一页则返回，否则关闭网页浏览器
                if (innerWebView.canGoBack() && !unableGoBack) {
                    innerWebView.goBack();
                } else {
                    getActivity().finish();
                }
                break;
            case R.id.img_title_close:
                finishPay();
                stopTimer();
                // 关闭网页浏览器
                getActivity().finish();
                // closeWebGoIntent不为空跳转指定页面
                if (closeWebGoIntent != null) {
                    startActivity(closeWebGoIntent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_PAYWEBURL:
                editor.putString(ConstantUtlis.WEB_JS, JSONUtlis.getString(response, "内容"));
                editor.putString(ConstantUtlis.WEBPAY_URL, JSONUtlis.getString(response, "webpay_url"));
                editor.putString(ConstantUtlis.WEBCASHIER_URL, JSONUtlis.getString(response, "webcashier_url"));
                editor.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResult(requestCode, resultCode, data);
    }

    public void ActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAG_ADDRESSBOOK_BACK:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        webFunction.contacts(data, new WebFunction.ContactsCallBack() {
                            @Override
                            public void callback(String content) {
                                webManage.callBack(content);
                            }
                        });
                    }
                }, R.string.perm_contacts, Manifest.permission.READ_CONTACTS);
                break;
            case TAG_LOGIN_BACK:
                try {
                    // 登录成功
                    webManage.callBack("{\"状态\":\"成功\",\"onlyid\":\"" + data.getStringExtra("onlyid") + "\"}");
                } catch (Exception e) {
                    // 解析异常登录失败
                    webManage.callBack("fail");
                }
                break;
            case TAG_PAY_BACK:
                if (data == null) return;
                if (data.getBooleanExtra("finshActivity", false)) {
                    stopTimer();
                    getActivity().finish();
                }
                break;
            default:
                try {
                    UnionPay.getInstance().UnionPayResult(data);
                } catch (Exception e) {

                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, boolean b) {
        finishPay();
        stopTimer();
        // closeWebGoIntent不为空跳转指定页面
        if (closeWebGoIntent != null) {
            startActivity(closeWebGoIntent);
        }
        // 返回键监听 点击返回如果有上一页面不会马上退出
        if (keyCode == KeyEvent.KEYCODE_BACK && !unableGoBack && innerWebView.canGoBack()) {
            innerWebView.goBack();
            return false;
        }
        if (unableGoBack) {
            unableGoBack = false;
        }
        return b;
    }

    /**
     * 判断是否支付页（用于通知上一层页面结束Activity）
     */
    public void finishPay() {
        try {
            if (!TextUtils.isEmpty(prepay_id) && !innerWebView.getUrl().contains(url)) {
                Intent intent = new Intent();
                intent.putExtra("finshActivity", true);
                getActivity().setResult(WebFragment.TAG_PAY_BACK, intent);  // 网页跳进登录页返回唯一id
            }
        } catch (Exception e) {
            L.e("finishPay_Exception");
        }
    }

    /**
     * 关闭timer倒计时
     */
    public void stopTimer() {
        if (myTimerTask == null)
            return;
        myTimerTask.finishWeb();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void web_wxPay(JSONObject jsonObject) {
        WXPay.getInstance().startPay(mContext, jsonObject, ConstantUtlis.WX_APP_ID2, new WXPay.WXPayCallBack() {
            @Override
            public void paySuccess() {
                webManage.callBack(TAG_WEBMANAGE_SUCCEESS);
            }

            @Override
            public void payError() {
                webManage.callBack(TAG_WEBMANAGE_FAIL);
            }
        });
    }

    @Override
    public void web_aliPay(JSONObject jsonObject) {
        String retrunInfo = JSONUtlis.getString(jsonObject, "return_info");
        new AliPay(mContext, retrunInfo) {
            @Override
            public void paySuccess() {
                webManage.callBack(TAG_WEBMANAGE_SUCCEESS);
            }

            @Override
            public void payError(boolean flag) {
                MyToast.getInstance().showToast(mContext, flag ? "支付取消" : "支付失败");
            }
        };
    }

    @Override
    public void web_pwdBox(JSONObject jsonObject) {
        String money = JSONUtlis.getString(jsonObject, "money");
        DialogUtlis.customPwd(getmDialog(), money, true, new MDialogInterface.PasswordInter() {
            @Override
            public void callback(String data) {
                webManage.callBack("{\"密码\":\"" + MD5Utlis.Md5(data) + "\"}");
            }
        });
    }

    @Override
    public void web_closeWeb(JSONObject jsonObject) {
        stopTimer();
        getActivity().finish();
    }

    Intent closeWebGoIntent = null;

    @Override
    public void web_closeWebGo(JSONObject jsonObject) {
        unableGoBack = true;
        imgTitleClose.setVisibility(View.VISIBLE);
        imgTitleBack.setVisibility(View.GONE);
        stopTimer();

        String page = JSONUtlis.getString(jsonObject, "page");
        closeWebGoIntent = new Intent(getActivity(), MainActivity.class);
        switch (page) {
            case "main_01":
                closeWebGoIntent.putExtra("chatSelectFlag", 1);
                break;
            case "main_02":
                closeWebGoIntent.putExtra("chatSelectFlag", 2);
                break;
            case "main_03":
                closeWebGoIntent.putExtra("chatSelectFlag", 3);
                break;
            case "main_04":
                closeWebGoIntent.putExtra("chatSelectFlag", 4);
                break;
            case "main_05":
                closeWebGoIntent.putExtra("chatSelectFlag", 5);
                break;
            default:
        }
    }

    @Override
    public void web_unableGoBack(JSONObject jsonObject) {
        unableGoBack = true;
        imgTitleClose.setVisibility(View.VISIBLE);
        imgTitleBack.setVisibility(View.GONE);
        stopTimer();
    }

    @Override
    public void web_addressbook(JSONObject jsonObject) {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), TAG_ADDRESSBOOK_BACK);
            }
        }, R.string.perm_contacts, Manifest.permission.READ_CONTACTS);
    }

    @Override
    public void web_login(JSONObject jsonObject) {
        if ("成功".equals(preferences.getString(ConstantUtlis.SP_LOGINTYPE, ""))) {
            webManage.callBack(TAG_WEBMANAGE_FAIL);
        } else {
            startActivityForResult(new Intent(mContext, LoginActivity.class), TAG_LOGIN_BACK);
        }
    }

    @Override
    public void web_video(JSONObject jsonObject) {
        JCVideoPlayerStandard.startFullscreen(getActivity(), JCVideoPlayerStandard.class, JSONUtlis.getString(jsonObject, "url"));
    }

    @Override
    public void web_webview(JSONObject jsonObject) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra("url", JSONUtlis.getString(jsonObject, "url"));
        startActivity(intent);
    }

    @Override
    public void web_shopInfo(JSONObject jsonObject) {
        String shopid = JSONUtlis.getString(jsonObject, "shopid");
        Intent intent = new Intent(mContext, ShangPinXiangQingActivity.class);
        intent.putExtra("id", shopid);
        startActivity(intent);
    }

    @Override
    public void web_getPhoto(JSONObject jsonObject) {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                MPhoto.Builder builder = new MPhoto.Builder()
                        .init(mContext)
                        .setTitle("选择图片")
                        .setResultCallback(new MPhoto.OnResultCallback() {
                            @Override
                            public void onSuccess(final String data) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        webManage.callBack("{\"状态\":\"成功\",\"地址\":\"" + data + "\"}");
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMsg) {
                                Log.e("上传图片失败", errorMsg);
                            }
                        });
                MPhoto.init(builder);
            }
        }, R.string.perm_camera, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void web_appType(JSONObject jsonObject) {
        webManage.callBack("{\"状态\":\"成功\",\"apptype\":\"" + "" + "\",\"版本号\":\"" + ConstantUtlis.VERSION + "\"}");
    }

    @Override
    public void web_appVersion(JSONObject jsonObject) {
        webManage.callBack("{\"状态\":\"成功\",\"版本号\":\"" + ConstantUtlis.VERSION + "\"}");
    }

    @Override
    public void web_webTitle(JSONObject jsonObject) {
        tvTitle.setText(JSONUtlis.getString(jsonObject, "title"));
    }

    @Override
    public void web_unionPay(JSONObject jsonObject) {
        UPPayAssistEx.startPay(mContext, null, null, JSONUtlis.getString(jsonObject, "tn"), ConstantUtlis.UnionPayMode);
    }

    @Override
    public void web_pageType(JSONObject jsonObject) {
        String page = JSONUtlis.getString(jsonObject, "page");
        if ("pay".equals(page)) {
            myTimerTask = new WebFunction.MyTimerTask(mContext, innerWebView, tvTime, Integer.parseInt(JSONUtlis.getString(jsonObject, "time")));
            myTimerTask.startSchedule();
        }
    }

    @Override
    public void web_toCashier(JSONObject jsonObject) {
        if ("".equals(preferences.getString(ConstantUtlis.WEBCASHIER_URL, ""))) {
            requestHandleArrayList.add(requestAction.getPayWebUrl(this, 0, 10));
            return;
        }
        // 跳转收银台
        startActivityForResult(WebToPayManager.toCashier(preferences, mContext, jsonObject), TAG_PAY_BACK);
    }


    @Override
    public void web_toPay(final JSONObject jsonObject) {
        String toPayUrl = preferences.getString(ConstantUtlis.WEBPAY_URL, "");
        String onlyID = preferences.getString(ConstantUtlis.SP_ONLYID, "");
        String randomCode = preferences.getString(ConstantUtlis.SP_RANDOMCODE, "");
        if ("".equals(toPayUrl)) {
            requestHandleArrayList.add(requestAction.getPayWebUrl(this, 0, 10));
            return;
        }
        new WebToPayManager(getActivity(), this, webManage, jsonObject).toPay(toPayUrl, onlyID, randomCode, prepay_id, appid);
    }

    @Override
    public void web_search(JSONObject jsonObject) {
        rlSearch.setVisibility(View.VISIBLE);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 缩小键盘
                    ((InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    innerWebView.loadUrl("javascript:activity.searchResult('" + v.getText().toString() + "')");
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void web_hideTitleBar(JSONObject jsonObject) {
        webTitle.setVisibility(View.GONE);
        switch (JSONUtlis.getString(jsonObject, "state")) {
            case "0":

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StatusBarUtil.transparencyBar(getActivity());
                                timer.cancel();
                            }
                        });
                    }
                }, 50, 1);
                break;
            default:
        }
    }

    @Override
    public void web_goBack(JSONObject jsonObject) {
        finishPay();
        stopTimer();
        // 网页如果可以返回上一页则返回，否则关闭网页浏览器
        if (innerWebView.canGoBack() && !unableGoBack) {
            innerWebView.goBack();
        } else {
            getActivity().finish();
        }
    }

}
