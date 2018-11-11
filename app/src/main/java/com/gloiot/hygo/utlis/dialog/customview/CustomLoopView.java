package com.gloiot.hygo.utlis.dialog.customview;

import android.view.View;

import com.gloiot.hygo.R;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.widge.MDialog;
import com.zyd.wlwsdk.widge.loopview.LoopView;
import com.zyd.wlwsdk.widge.loopview.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * @author ljz.
 * @date 2017/11/7
 *
 * 滚轮单选
 */

public class CustomLoopView {

    public void builder(final MDialog mDialog, String title, final ArrayList<String> datas, final int currentPostition, final MDialogInterface.LoopViewInter loopViewInter, View.OnClickListener onClickListener) {

        mDialog.initDialog().withTitie(title)
                .setCustomView(R.layout.dialog_son_loopview, new MDialog.CustomInter() {
                    @Override
                    public void custom(View customView) {

                        LoopView loopView = (LoopView) customView.findViewById(R.id.lp_loopView);

                        loopView.setListener(new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(int index) {
                                loopViewInter.getPostition(index);
                            }
                        });
                        loopView.setItemsVisibleCount(6);
                        loopView.setNotLoop();
                        loopView.setItems(datas);
                        loopView.setCurrentPosition(currentPostition);
                    }
                })
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                }, onClickListener).show();
    }
}
