package com.gloiot.hygo.utlis.dialog.customview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.widge.MDialog;

/**
 * @author ljz.
 * @date 2017/11/7
 * 我的二维码
 */

public class CustomQRCode {

    /**
     * @param mDialog     dialog
     * @param qrCodeInter 显示回调
     */
    public void builder(final MDialog mDialog, final MDialogInterface.QrCodeInter qrCodeInter) {
        mDialog.setCancelable(true);
        mDialog.initDialog().setDismissClickEveryWhere(true)
                .setCustomView(R.layout.dialog_son_myerweima, new MDialog.CustomInter() {
                    @Override
                    public void custom(View customView) {
                        TextView tv_name = (TextView) customView.findViewById(R.id.tv_name);
                        TextView tv_num = (TextView) customView.findViewById(R.id.tv_num);
                        ImageView iv_touxiang = (ImageView) customView.findViewById(R.id.iv_touxiang);
                        ImageView iv_ewm = (ImageView) customView.findViewById(R.id.iv_ewm);
                        ImageView iv_sex = (ImageView) customView.findViewById(R.id.iv_sex);

                        qrCodeInter.callback(tv_name, tv_num, iv_touxiang, iv_ewm, iv_sex);
                    }
                }).show();
    }
}
