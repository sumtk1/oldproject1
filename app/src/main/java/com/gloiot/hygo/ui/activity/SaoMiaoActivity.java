package com.gloiot.hygo.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.zyd.wlwsdk.zxing.activity.CaptureFragment;
import com.zyd.wlwsdk.zxing.activity.CodeUtils;

/**
 * 定制化显示扫描界面
 */
public class SaoMiaoActivity extends BaseActivity {

    private CaptureFragment captureFragment;


    @Override
    public int initResource() {
        return R.layout.activity_saomiao;
    }

    @Override
    public void initData() {
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        CommonUtlis.setTitleBar(this, "扫一扫");
    }



    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            SaoMiaoActivity.this.setResult(RESULT_OK, resultIntent);
            SaoMiaoActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            SaoMiaoActivity.this.setResult(RESULT_OK, resultIntent);
            SaoMiaoActivity.this.finish();
        }
    };
}
