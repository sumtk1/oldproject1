package com.gloiot.hygo.utlis.dialog;

import android.view.View;

import com.gloiot.hygo.utlis.dialog.customview.CustomDateView;
import com.gloiot.hygo.utlis.dialog.customview.CustomListView;
import com.gloiot.hygo.utlis.dialog.customview.CustomLoopView;
import com.gloiot.hygo.utlis.dialog.customview.CustomPwd;
import com.gloiot.hygo.utlis.dialog.customview.CustomQRCode;
import com.gloiot.hygo.utlis.dialog.customview.CustomSex;
import com.zyd.wlwsdk.widge.MDialog;

import java.util.ArrayList;

/**
 * @author ljz.
 * @date 2017/11/7
 */

public class DialogUtlis {

    public static void oneBtnNormal(MDialog mDialog, String content) {
        oneBtnNormal(mDialog, "提示", content, null, true, null);
    }

    public static void oneBtnNormal(MDialog mDialog, String content, String btntext) {
        oneBtnNormal(mDialog, "提示", content, btntext, true, null);
    }

    public static void oneBtnNormal(MDialog mDialog, String content, String btntext, View.OnClickListener onClickListener) {
        oneBtnNormal(mDialog, content, btntext, true, onClickListener);
    }

    public static void oneBtnNormal(MDialog mDialog, String content, View.OnClickListener onClickListener) {
        oneBtnNormal(mDialog, content, null, false, onClickListener);
    }

    public static void oneBtnNormal(MDialog mDialog, String content, boolean cancelable, View.OnClickListener onClickListener) {
        oneBtnNormal(mDialog, content, null, cancelable, onClickListener);
    }

    public static void oneBtnNormal(MDialog mDialog, String content, String btnText, boolean cancelable, View.OnClickListener onClickListener) {
        oneBtnNormal(mDialog, "提示", content, -100, btnText, cancelable, onClickListener);
    }

    public static void oneBtnNormal(MDialog mDialog, String title, String content, String btnText, boolean cancelable, View.OnClickListener onClickListener) {
        oneBtnNormal(mDialog, title, content, -100, btnText, cancelable, onClickListener);
    }

    public static void twoBtnNormal(MDialog mDialog, String content, View.OnClickListener onClickListener) {
        twoBtnNormal(mDialog, "提示", content, -100, true, null, null, onClickListener, null);
    }

    public static void twoBtnNormal(MDialog mDialog, String content, int gravity, View.OnClickListener onClickListener) {
        twoBtnNormal(mDialog, "提示", content, gravity, true, null, null, onClickListener, null);
    }

    public static void twoBtnNormal(MDialog mDialog, String content, String btn1, String btn2, View.OnClickListener onClickListener) {
        twoBtnNormal(mDialog, "提示", content, -100, true, btn1, btn2, onClickListener, null);
    }

    public static void twoBtnNormal(MDialog mDialog, String title, String content, boolean cancelable, String btn1, String btn2, View.OnClickListener onClickListener1, View.OnClickListener onClickListener2) {
        twoBtnNormal(mDialog, title, content, -100, cancelable, btn1, btn2, onClickListener1, onClickListener2);
    }


    public static void oneBtnNormal(MDialog mDialog, String title, String content, int gravity, String btnText, boolean cancelable, View.OnClickListener onClickListener) {
        new NormalOneBtn().builder(mDialog, title, content, gravity, btnText, cancelable, onClickListener);
    }

    public static void twoBtnNormal(MDialog mDialog, String title, String content, int gravity, boolean cancelable, String btn1, String btn2, View.OnClickListener onClickListener1, View.OnClickListener onClickListener2) {
        new NormalTowBtn().builder(mDialog, title, content, gravity, cancelable, btn1, btn2, onClickListener1, onClickListener2);
    }


    public static void customDateView(MDialog mDialog, MDialogInterface.DataInter dataInter) {
        new CustomDateView().builder(mDialog, dataInter);
    }

    public static void customLoopView(final MDialog mDialog, String title, final ArrayList<String> datas, final int currentPostition, final MDialogInterface.LoopViewInter loopViewInter, View.OnClickListener onClickListener) {
        new CustomLoopView().builder(mDialog, title, datas, currentPostition, loopViewInter, onClickListener);
    }


    public static void customListView(final MDialog mDialog, String title, final ArrayList<String> datas, final MDialogInterface.ListViewOnClickInter listViewDataInter) {
        new CustomListView().builder(mDialog, title, datas, listViewDataInter);
    }


    public static void customSex(final MDialog mDialog, final int position, final MDialogInterface.SexChoiceInter sexChoiceInter) {
        new CustomSex().builder(mDialog, position, sexChoiceInter);
    }


    public static void customPwd(final MDialog mDialog, final String message, final boolean isPay, final MDialogInterface.PasswordInter passwordInter) {
        new CustomPwd().builder(mDialog, message, isPay, passwordInter);
    }

    public static void customQRCode(final MDialog mDialog, final MDialogInterface.QrCodeInter qrCodeInter) {
        new CustomQRCode().builder(mDialog, qrCodeInter);
    }

}
