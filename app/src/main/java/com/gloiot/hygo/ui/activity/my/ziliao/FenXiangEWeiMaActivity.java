package com.gloiot.hygo.ui.activity.my.ziliao;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygo.ui.App;
import com.zyd.wlwsdk.utils.MyToast;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.zyd.wlwsdk.utils.PictureUtlis;
import com.zyd.wlwsdk.utils.ScreenShotUtils;
import com.zyd.wlwsdk.utils.WXUtil;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的二维码
 */
public class FenXiangEWeiMaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_ewm_touxiang)
    ImageView ivEwmTouxiang;
    @Bind(R.id.tv_ewm_name)
    TextView tvEwmName;
    @Bind(R.id.tv_ewm_num)
    TextView tvEwmNum;
    @Bind(R.id.iv_ewm)
    ImageView ivEwm;
    @Bind(R.id.iv_sex)
    ImageView ivSex;

    @Override
    public int initResource() {
        return R.layout.activity_fenxiangeweima;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "分享二维码");
        // 头像
        PictureUtlis.loadCircularImageViewHolder(mContext, preferences.getString(ConstantUtlis.SP_USERIMG, ""), R.mipmap.ic_head, ivEwmTouxiang);
        // 昵称
        tvEwmName.setText(preferences.getString(ConstantUtlis.SP_NICKNAME, ""));
        // 账号
        String my_phone = preferences.getString(ConstantUtlis.SP_USERPHONE, "");
        try {
            my_phone = my_phone.substring(0, 3) + "****" + my_phone.substring(my_phone.length() - 4, my_phone.length());
        } catch (Exception e) {

        }
        tvEwmNum.setText(my_phone);
        //性别
        String gender = preferences.getString(ConstantUtlis.SP_GENDER, "");
        if (gender.equals("男")) {
            PictureUtlis.loadImageViewHolder(mContext, "", R.mipmap.male_ic, ivSex);
        } else if (gender.equals("女")) {
            PictureUtlis.loadImageViewHolder(mContext, "", R.mipmap.female_ic, ivSex);
        } else {

        }

        requestHandleArrayList.add(requestAction.GetMyQRcode(FenXiangEWeiMaActivity.this));

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_GETMYQRCODE:
                String myQRcode = response.getString("二维码地址");
                if (TextUtils.isEmpty(myQRcode)) {
                    MyToast.getInstance().showToast(mContext, "返回二维码信息错误");
                    return;
                }
                //        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//                ivEwm.setImageBitmap(CodeUtils.createImage(myQRcode, 400, 400, null));

                PictureUtlis.loadImageViewHolder(mContext, myQRcode, R.mipmap.ic_launcher, ivEwm);
                break;
        }

    }

    @OnClick({R.id.rl_ewm_weixin, R.id.rl_ewm_pengyouquan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_ewm_weixin:
                if (CommonUtlis.isWXAppInstalledAndSupported(mContext)) {
                    checkPermission(new CheckPermListener() {
                        @Override
                        public void superPermission() {
                            wxShare(1);
                        }
                    }, R.string.perm_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.rl_ewm_pengyouquan:
                if (CommonUtlis.isWXAppInstalledAndSupported(mContext)) {
                    checkPermission(new CheckPermListener() {
                        @Override
                        public void superPermission() {
                            wxShare(0);
                        }
                    }, R.string.perm_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
        }
    }

    /**
     * 分享到微信
     */
    private void wxShare(int shareType) {
        LoadDialog.show(mContext);

        boolean result = ScreenShotUtils.shotBitmap(FenXiangEWeiMaActivity.this);
        if (result) {
            String path = "/sdcard/gh.png";
            File file = new File(path);
            if (!file.exists()) {
                String tip = "文件不存在";
                Toast.makeText(FenXiangEWeiMaActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
                return;
            }
            WXImageObject imgObj = new WXImageObject();
            imgObj.setImagePath(path);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            Bitmap bmp = BitmapFactory.decodeFile(path);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            if (shareType == 0) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            }
            App.getInstance().wxapi.sendReq(req);
//            api.sendReq(req);

        } else {
            MyToast.getInstance().showToast(mContext, "截图失败.");
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDialog.dismiss(mContext);
    }
}
