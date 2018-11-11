package com.gloiot.hygo.utlis.dialog.customview;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.widge.MDialog;

/**
 * @author ljz.
 * @date 2017/11/7
 * 性别选择
 */

public class CustomSex implements View.OnClickListener {
    private final int TAG_BOY = 0;      // 男
    private final int TAG_GIRL = 1;     // 女
    private int newPosition;            // 当前选择位置
    private TextView tvBoy, tvGirl;

    /**
     * @param mDialog        dialog
     * @param position       默认位置
     * @param sexChoiceInter 选择回调
     */
    public void builder(final MDialog mDialog, final int position, final MDialogInterface.SexChoiceInter sexChoiceInter) {
        mDialog.initDialog().withTitie("性别")
                .setCustomView(R.layout.dialog_son_sex, new MDialog.CustomInter() {
                    @Override
                    public void custom(View customView) {
                        tvBoy = (TextView) customView.findViewById(R.id.dialog_nan);
                        tvGirl = (TextView) customView.findViewById(R.id.dialog_nv);
                        tvBoy.setOnClickListener(CustomSex.this);
                        tvGirl.setOnClickListener(CustomSex.this);
                        // 设置默认位置
                        switch (position) {
                            case TAG_BOY:
                                setBoy();
                                break;
                            case TAG_GIRL:
                                setGirl();
                                break;
                            default:
                        }
                    }
                })
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sexChoiceInter.onClick(newPosition);
                        mDialog.dismiss();
                    }
                }).show();
    }

    private void setBoy() {
        tvBoy.setTextColor(Color.parseColor("#965aff"));
        tvGirl.setTextColor(Color.parseColor("#333333"));
        tvBoy.setBackgroundResource(R.drawable.sytle_dialog_sex2);
        tvGirl.setBackgroundResource(R.drawable.sytle_dialog_sex1);
        newPosition = TAG_BOY;
    }

    private void setGirl() {
        tvBoy.setTextColor(Color.parseColor("#333333"));
        tvGirl.setTextColor(Color.parseColor("#965aff"));
        tvBoy.setBackgroundResource(R.drawable.sytle_dialog_sex1);
        tvGirl.setBackgroundResource(R.drawable.sytle_dialog_sex2);
        newPosition = TAG_GIRL;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_nan:
                setBoy();
                break;
            case R.id.dialog_nv:
                setGirl();
                break;
        }

    }
}
