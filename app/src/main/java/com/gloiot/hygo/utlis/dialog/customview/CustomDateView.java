package com.gloiot.hygo.utlis.dialog.customview;

import android.view.View;
import android.widget.DatePicker;

import com.gloiot.hygo.R;
import com.gloiot.hygo.utlis.dialog.MDialogInterface;
import com.zyd.wlwsdk.utils.DatePrickerUtil;
import com.zyd.wlwsdk.widge.MDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author ljz.
 * @date 2017/11/7
 * 日期选择
 */

public class CustomDateView {

    private DatePicker datePicker;  // 日期选择控件

    /**
     * @param mDialog   dialog
     * @param dataInter 日期选择回调
     */
    public void builder(final MDialog mDialog, final MDialogInterface.DataInter dataInter) {
        mDialog.initDialog().withTitie("日期")
                .setCustomView(R.layout.dialog_son_datepricker, new MDialog.CustomInter() {
                    @Override
                    public void custom(View customView) {
                        datePicker = (DatePicker) customView.findViewById(R.id.datePicker);
                        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                        DatePrickerUtil.findNumberPicker(datePicker);
                        try {
                            datePicker.setMinDate(new SimpleDateFormat("yyyy-MM-dd").parse("1898-01-01").getTime());
                            datePicker.setMaxDate(new SimpleDateFormat("yyyy-MM-dd").parse(DatePrickerUtil.getDate()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DatePrickerUtil.setDatePickerDividerColor(datePicker);  // 自定义控件样式
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
                        dataInter.data(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                        mDialog.dismiss();
                    }
                }).show();
    }
}
