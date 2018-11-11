package com.zyd.wlwsdk.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.zyd.wlwsdk.R;

/**
 * Created by hygo01 on 2016/8/11.
 * 图片加载框架 Glide
 */
public class PictureUtlis {

    /**
     * 加载有默认图片
     *
     * @param mContext   上下文
     * @param path       图片路径
     * @param resId      默认图片资源
     * @param mImageView 控件
     */
    public static void loadImageViewHolder(Context mContext, String path, int resId, ImageView mImageView) {
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(resId)
                    .centerCrop()
                    .error(resId);
            Glide.with(mContext)
                    .load(path)
                    .apply(options)
                    .into(mImageView);
        } catch (Exception e) {
            Log.e("loadCircular", "loadCircularImageViewHolder: " + e);
        }
    }

    /**
     * 加载无默认图片
     *
     * @param mContext   上下文
     * @param path       图片路径
     * @param mImageView 控件
     */
    public static void loadImageViewHolder(Context mContext, String path, ImageView mImageView) {
        try {
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(mContext)
                    .load(path)
                    .apply(options)
                    .into(mImageView);
        } catch (Exception e) {
            Log.e("loadCircular", "loadCircularImageViewHolder: " + e);
        }
    }

    /**
     * 加载无默认图片(加载完成回调)
     *
     * @param mContext   上下文
     * @param path       图片路径
     * @param mImageView 控件
     * @param callback   加载完成回调
     */
    public static void loadImageViewHolder(Context mContext, String path, ImageView mImageView, RequestListener callback) {
        try {
            Glide.with(mContext)
                    .load(path)
                    .listener(callback)
                    .into(mImageView);
        } catch (Exception e) {
            Log.e("loadCircular", "loadCircularImageViewHolder: " + e);
        }
    }

    /**
     * 加载有默认图片(圆形)
     *
     * @param mContext   上下文
     * @param path       图片路径
     * @param resId      默认图片资源
     * @param mImageView 控件
     */
    public static void loadCircularImageViewHolder(Context mContext, String path, int resId, ImageView mImageView) {
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(resId)
                    .centerCrop()
                    .error(resId);
            options.apply(RequestOptions.circleCropTransform());
            Glide.with(mContext)
                    .load(path)
                    .apply(options)
                    .into(mImageView);
        } catch (Exception e) {
            Log.e("loadCircular", "loadCircularImageViewHolder: " + e);
        }
    }

    /**
     * 加载无默认图片（圆形）
     *
     * @param mContext   上下文
     * @param path       图片路径
     * @param mImageView 控件
     */
    public static void loadCircularImageViewHolder(Context mContext, int path, ImageView mImageView) {
        try {
            RequestOptions options = RequestOptions.circleCropTransform();
            Glide.with(mContext)
                    .load(path)
                    .apply(options)
                    .into(mImageView);
        } catch (Exception e) {
            Log.e("loadCircular", "loadCircularImageViewHolder: " + e);
        }
    }


    /**
     * 加载有默认图片(圆角)
     *
     * @param mContext   上下文
     * @param path       图片路径
     * @param resId      默认图片资源
     * @param mImageView 控件
     * @param round      圆角
     */
    public static void loadRoundImageViewHolder(Context mContext, String path, int resId, ImageView mImageView, int round) {
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(resId)
                    .error(resId)
                    .transform(new GlideRoundTransform(mContext, round));
            Glide.with(mContext)
                    .load(path)
                    .apply(options)
                    .into(mImageView);
        } catch (Exception e) {
            Log.e("loadCircular", "loadCircularImageViewHolder: " + e);
        }
    }
}
