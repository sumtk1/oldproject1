package com.gloiot.hygo.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gloiot.hygo.R;
import com.loopj.android.http.RequestHandle;
import com.zyd.wlwsdk.utils.PictureUtlis;

import java.util.ArrayList;

/**
 * Created by dwj on 2017/6/1.
 */

public class RebateDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "RebateDialog";
    private static ArrayList<RequestHandle> requestHandle;
    private static RebateDialog rebateDialog;
    private Context mContext;
    private static ImageView imageView;


    public RebateDialog(final Context ctx) {
        super(ctx);
        mContext = ctx;

        this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.dialog_rebate);

        imageView = (ImageView) findViewById(R.id.rebate_imv);
        imageView.setOnClickListener(this);
        // 必须放在加载布局后
        setparams();
    }

    private static ImageButton imageButton;

    public RebateDialog(final Context ctx, String str, View.OnClickListener onClickListener) {
        super(ctx);
        mContext = ctx;

        this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.dialog_rebate1);

        imageView = (ImageView) findViewById(R.id.rebate_imv);
        imageButton = (ImageButton) findViewById(R.id.dialog_imageButton);
        imageButton.setOnClickListener(onClickListener);
        ImageView imageView = (ImageView) findViewById(R.id.dialog_guanbi);
        imageView.setOnClickListener(this);
        // 必须放在加载布局后
        setparams();
    }

    private void setparams() {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);

        try {
            window.getDecorView().getBackground().setAlpha(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Context context, String path) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (rebateDialog != null && rebateDialog.isShowing()) {
            return;
        }
        rebateDialog = new RebateDialog(context);
        PictureUtlis.loadImageViewHolder(context, path, imageView);
        rebateDialog.show();
    }

    public static void show(Context context, String path, String str, View.OnClickListener onClickListener) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (rebateDialog != null && rebateDialog.isShowing()) {
            return;
        }
        rebateDialog = new RebateDialog(context, str, onClickListener);
        PictureUtlis.loadImageViewHolder(context, path, imageView, new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                imageButton.setVisibility(View.VISIBLE);
                return false;
            }
//            @Override
//            public void onSuccess() {
//                imageButton.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onError() {
//
//            }
        });
        rebateDialog.show();
    }

    public static void dismiss(Context context) {
        try {
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    rebateDialog = null;
                    return;
                }
            }

            if (rebateDialog != null && rebateDialog.isShowing()) {
                Context rebateContext = rebateDialog.getContext();
                if (rebateContext != null && rebateContext instanceof Activity) {
                    if (((Activity) rebateContext).isFinishing()) {
                        rebateDialog = null;
                        return;
                    }
                }
                rebateDialog.dismiss();
                rebateDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            rebateDialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        //    Toast.makeText(mContext,"RebateDialog",Toast.LENGTH_SHORT).show();
        dismiss(mContext);
    }
}
