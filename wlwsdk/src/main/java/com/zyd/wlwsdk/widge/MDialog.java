package com.zyd.wlwsdk.widge;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyd.wlwsdk.R;

/**
 * @author ljz.
 * @date 2017/11/2
 * 自定义Dialog
 */

public class MDialog extends Dialog {

    private int mHeight, mWidth;                        // 屏幕高度宽度
    private RelativeLayout mDialog;                     // mDialog
    private TextView mDialogTitle, mDialogContent;      // 标题、内容
    private Button btnLeft, btnRight, btnAll;           // 左按钮、右按钮、一个按钮
    private FrameLayout mDialogCustomPanel;             // 自定义面板
    private View mDialogCustomPanelLine;                // 自定义面板线条
    private boolean isDismissClickEveryWhere;           // 用于判断点击是否dimss对话框
    private Context context;

    public MDialog(Context context) {
        super(context, R.style.MDialogStyle);
        this.context = context;
        this.setCancelable(false);
        init();
    }


    private void init() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        View view = View.inflate(context, R.layout.dialog_main, null);
        mDialog = (RelativeLayout) view.findViewById(R.id.mydialog);
        mDialogTitle = (TextView) view.findViewById(R.id.mydialog_title);
        mDialogContent = (TextView) view.findViewById(R.id.mydialog_content);
        mDialogCustomPanel = (FrameLayout) view.findViewById(R.id.mydialog_custompanel);
        mDialogCustomPanelLine = view.findViewById(R.id.mydialog_flview);
        btnLeft = (Button) view.findViewById(R.id.dialog_btn_left);
        btnRight = (Button) view.findViewById(R.id.dialog_btn_right);
        btnAll = (Button) view.findViewById(R.id.dialog_btn_all);
        setContentView(view);

        view.findViewById(R.id.mydialog_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDismissClickEveryWhere) {
                    dismiss();
                }
            }
        });

        mHeight = getScreenHeight();
        mWidth = getScreenWidth();
        setWidth(0.8);
    }

    /**
     * 必须调用该方法，用于清除上个dialog
     */
    public MDialog initDialog() {
        if (isShowing()) {
            dismiss();
        }
        return this;
    }


    /**
     * 用于判断点击是否dimss对话框
     */
    public MDialog setDismissClickEveryWhere(boolean dismissClickEveryWhere) {
        isDismissClickEveryWhere = dismissClickEveryWhere;
        return this;
    }

    /**
     * 显示自定义面板线条
     */
    public MDialog showCustomPanelLine() {
        mDialogCustomPanelLine.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置myDialog宽度
     */
    public MDialog setWidth(double i) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mDialog.getLayoutParams();
        lp.width = (int) (mWidth * i);
        mDialog.setLayoutParams(lp);
        return this;
    }

    /**
     * 设置最大高度
     */
    public MDialog setMaxHeight(final View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int cHeight = view.getHeight();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (cHeight >= mHeight / 3) {
                    lp.height = mHeight / 3;
                }
                view.setLayoutParams(lp);
            }
        });
        return this;
    }

    /**
     * 设置标题
     */
    public MDialog withTitie(String dialogTitle) {
        mDialogTitle.setVisibility(View.VISIBLE);
        mDialogTitle.setText(dialogTitle);
        return this;
    }

    /**
     * 设置内容
     */
    public MDialog withContene(String dialogContent) {
        mDialogContent.setVisibility(View.VISIBLE);
        mDialogContent.setText(dialogContent);
        mDialogContent.setGravity(Gravity.CENTER);
        return this;
    }

    /**
     * 设置内容
     */
    public MDialog withContene(String dialogContent, int gravity) {
        mDialogContent.setVisibility(View.VISIBLE);
        mDialogContent.setText(dialogContent);
        mDialogContent.setGravity(gravity);
        return this;
    }

    /**
     * 定制layout
     *
     * @param resId 定制layout的ID
     */
    public MDialog setCustomView(int resId, CustomInter customInter) {
        View customView = View.inflate(context, resId, null);
        if (null != customInter) {
            customInter.custom(customView);
        }
        mDialogCustomPanel.addView(customView);
        return this;
    }

    /**
     * 一个按钮点击事件
     */
    public MDialog setBtn1(View.OnClickListener click) {
        btnAll.setVisibility(View.VISIBLE);
        btnAll.setOnClickListener(click);
        return this;
    }

    /**
     * 一个按钮字体
     */
    public MDialog setBtn1Text(String btn1) {
        btnAll.setText(btn1);
        return this;
    }

    /**
     * 一个按钮背景样式
     */
    public MDialog setBtn1Bg(int drawable) {
        btnAll.setBackgroundResource(drawable);
        btnAll.setTextColor(getContext().getResources().getColor(R.color.cl_white));
        return this;
    }

    /**
     * 两个按钮点击事件
     */
    public MDialog setBtn2(View.OnClickListener clickLeft, View.OnClickListener clickRight) {
        btnAll.setVisibility(View.GONE);
        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(clickLeft);
        btnRight.setOnClickListener(clickRight);
        return this;
    }

    /**
     * 两个按钮点击事件
     */
    public MDialog setBtn2Text(String btn1, String btn2) {
        btnLeft.setText(btn1);
        btnRight.setText(btn2);
        return this;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        isDismissClickEveryWhere = false;
        mDialogTitle.setVisibility(View.GONE);
        mDialogContent.setVisibility(View.GONE);
        mDialogCustomPanelLine.setVisibility(View.GONE);
        btnLeft.setVisibility(View.GONE);
        btnRight.setVisibility(View.GONE);
        btnAll.setVisibility(View.GONE);
        mDialogCustomPanel.removeAllViews();
    }

    // 获得屏幕高度
    private int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    // 获得屏幕宽度
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    public interface CustomInter {
        /**
         * 自定义界面回调
         *
         * @param customView view
         */
        void custom(View customView);
    }
}
