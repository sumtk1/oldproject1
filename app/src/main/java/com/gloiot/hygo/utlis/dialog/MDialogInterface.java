package com.gloiot.hygo.utlis.dialog;

import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author ljz.
 * @date 2017/11/6
 * MDialog接口类
 */

public interface MDialogInterface {

    interface DataInter {
        /**
         * 日期选择回调
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */
        void data(int year, int month, int day);
    }

    interface LoopViewInter {
        /**
         * 滚筒选择单选回调
         *
         * @param postition 选择的位置
         */
        void getPostition(int postition);
    }

    interface ListViewOnClickInter {
        /**
         * listview单选选择回调
         *
         * @param data     选择的数据
         * @param position 选择的位置
         */
        void onItemClick(String data, int position);
    }

    /**
     * 性别选择回调
     */
    interface SexChoiceInter {
        /**
         * 性别选择回调
         *
         * @param position 选择的位置
         */
        void onClick(int position);
    }

    interface PasswordInter {
        /**
         * 输入密码回调
         *
         * @param data 密码
         */
        void callback(String data);
    }

    interface QrCodeInter {
        /**
         * 我的二维码选择回调
         *
         * @param tvName   名字
         * @param tvNum    年龄
         * @param ivPic    头像
         * @param ivQRCode 二维码
         * @param ivSex    性别
         */
        void callback(TextView tvName, TextView tvNum, ImageView ivPic, ImageView ivQRCode, ImageView ivSex);
    }
}
