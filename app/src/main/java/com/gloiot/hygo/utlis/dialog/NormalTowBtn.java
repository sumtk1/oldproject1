package com.gloiot.hygo.utlis.dialog;

import android.view.View;

import com.zyd.wlwsdk.widge.MDialog;

/**
 * @author ljz.
 * @date 2017/11/7
 * 两个按钮Dialog
 */

class NormalTowBtn {

    /**
     * @param mDialog          dialog
     * @param title            标题
     * @param content          内容
     * @param gravity          内容位置(Gravity.)
     * @param cancelable       能否取消
     * @param btn1             按钮1文字
     * @param btn2             按钮2文字
     * @param onClickListener1 按钮1点击监听
     * @param onClickListener2 按钮2点击监听
     */
    void builder(final MDialog mDialog, String title, String content, int gravity, boolean cancelable, String btn1, String btn2, View.OnClickListener onClickListener1, View.OnClickListener onClickListener2) {
        mDialog.setCancelable(cancelable);

        mDialog.initDialog().withTitie(title);
        if (gravity != -100) {
            mDialog.withContene(content, gravity);
        } else {
            mDialog.withContene(content);
        }
        if (btn1 != null && btn2 != null) {
            mDialog.setBtn2Text(btn1, btn2);
        }
        if (onClickListener1 != null && onClickListener2 != null) {
            mDialog.setBtn2(onClickListener1, onClickListener2);
        }
        if (onClickListener1 != null && onClickListener2 == null) {
            mDialog.setBtn2(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            }, onClickListener1);
        }
        mDialog.show();
    }
}
