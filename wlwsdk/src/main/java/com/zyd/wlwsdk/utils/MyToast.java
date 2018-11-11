package com.zyd.wlwsdk.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyd.wlwsdk.R;

import java.lang.ref.WeakReference;

/**
 * Created by hygo01 on 2017/11/1.
 * Toast工具类
 */
public class MyToast {

    private Toast mToast;
    private Handler mHandler = new Handler();
    private Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null; // toast隐藏后，将其置为null
        }
    };
    private String toastContet = "";

    private static WeakReference<MyToast> WeakReferenceInstance;

    /**
     * 通过单利模式处理，采用弱引用解决上下文对象内存无法释放问题
     * 使用例子：
     * MyToast.getInstance().showToast(context, "提示文字")
     * MyToast.getInstance().showToast(context, "提示文字"，true)
     *
     * @return
     */
    public static synchronized MyToast getInstance() {
        if (WeakReferenceInstance == null || WeakReferenceInstance.get() == null) {
            synchronized (MyToast.class) {
                if (WeakReferenceInstance == null || WeakReferenceInstance.get() == null) {
                    WeakReferenceInstance = new WeakReference<MyToast>(new MyToast());
                }
            }
        }
        return WeakReferenceInstance.get();
    }

    private MyToast() {
    }

    /**
     * 显示Toast，默认没有图片
     *
     * @param context 上下文Context
     * @param content 吐司显示文字
     */
    public void showToast(final Context context, final String content) {
        if (toastContet.equals(content)) { // 判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) { // 只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            }
        } else {
            toastContet = content;
            if (mToast != null) { // mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 显示自定义Toast
     *
     * @param context 上下文Context
     * @param content 吐司显示文字
     * @param haveImg 是否有图片，true为有图片
     */
    public void showToast(final Context context, final String content, final boolean haveImg) {
        if (toastContet.equals(content)) { // 判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) { // 只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);

                mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);  //设置显示位置
                View view = View.inflate(context, R.layout.toast_buju, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_toast_ic);
                TextView textView = (TextView) view.findViewById(R.id.tv_toast_info);

                if (haveImg) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }

                textView.setText(content);
                mToast.setView(view);
            }
        } else {
            toastContet = content;
            if (mToast != null) { // mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);

            mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);  //设置显示位置
            View view = View.inflate(context, R.layout.toast_buju, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_toast_ic);
            TextView textView = (TextView) view.findViewById(R.id.tv_toast_info);

            if (haveImg) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }

            textView.setText(content);
            mToast.setView(view);
        }
        mToast.show();
    }
}
