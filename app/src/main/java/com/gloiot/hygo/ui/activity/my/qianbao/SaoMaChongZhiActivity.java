package com.gloiot.hygo.ui.activity.my.qianbao;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.web.WebActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.ImgUtils;
import com.zyd.wlwsdk.utils.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hygo03 on 2017/7/4.
 * 扫码充值
 */

public class SaoMaChongZhiActivity extends BaseActivity {

    @Bind(R.id.iv_saoma_erweima)
    ImageView ivSaomaErweima;
    @Bind(R.id.et_saoma_dingdanhao)
    EditText etSaomaDingdanhao;
    @Bind(R.id.tv_saoma_chongzhijilu)
    TextView tvSaomaChongzhijilu;
    @Bind(R.id.tv_saoma_caozoushuoming)
    TextView tvSaomaCaozoushuoming;
    @Bind(R.id.rl_saoma_activity)
    RelativeLayout rlSaomaActivity;




    @Override
    public int initResource() {
        return R.layout.activity_saoma_chongzhi;
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, "扫码充值");
    }

    @OnClick({R.id.iv_saoma_erweima, R.id.tv_saoma_chongzhijilu, R.id.tv_saoma_caozoushuoming, R.id.tv_saoma_tijiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //二维码
            case R.id.iv_saoma_erweima:
                new saoMaPop(mContext).showAtLocation(rlSaomaActivity, Gravity.CENTER, 0, 0);
                break;

            case R.id.tv_saoma_tijiao:

                String onlyId = preferences.getString(ConstantUtlis.SP_ONLYID,"");
                if(TextUtils.isEmpty(etSaomaDingdanhao.getText().toString())){
                    MyToast.getInstance().showToast(mContext,"请输入支付宝订单号");
                }else {
                    requestHandleArrayList.add(requestAction.findIndentNum(this,etSaomaDingdanhao.getText().toString(),onlyId));
                }
                break;

            //充值记录
            case R.id.tv_saoma_chongzhijilu:
                startActivity(new Intent(mContext,SaoMaChongZhiJiLuActivity.class));
                break;

            //操作说明
            case R.id.tv_saoma_caozoushuoming:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", ConstantUtlis.CAOZOUSHUOMING_URL);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag){
            case RequestAction.TAG_SHOP_P_FINDINDENTNUM_THREE:
                    etSaomaDingdanhao.setText("");
                    MyToast.getInstance().showToast(mContext,"提交成功");

                break;
        }
    }

    /**
     * 二维码popwindow
     * */
    private class saoMaPop extends PopupWindow {
        private Bitmap bitmap;

        private saoMaPop(Context context) {

            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popView = inflater.inflate(R.layout.pop_saoma_erweima, null);

            final ImageView imageView = (ImageView) popView.findViewById(R.id.iv_saoma_erweima);
            TextView textView = (TextView) popView.findViewById(R.id.tv_saoma_baocun);


            //保存二维码到相册
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermission();
                    dismiss();
                }
            });

            // 设置SelectPicPopupWindow的View
            this.setContentView(popView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            // 设置popupWindow以外可以触摸
            this.setOutsideTouchable(true);
            // 以下两个设置点击空白处时，隐藏掉pop窗口
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(0.3f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());
        }


    }


    /**
     * 请求读取权限,并保存图片到相册
     */
    private void requestPermission() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                //保存图片
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_zhifu_erweima);
                boolean isSaveSuccess = ImgUtils.saveImageToGallery(mContext, bitmap);
                if (isSaveSuccess) {
                    MyToast.getInstance().showToast(mContext, "保存图片成功");
                } else {
                    MyToast.getInstance().showToast(mContext, "保存图片失败，请稍后重试");
                }
            }
        }, R.string.perm_readstorage, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    private class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

}
