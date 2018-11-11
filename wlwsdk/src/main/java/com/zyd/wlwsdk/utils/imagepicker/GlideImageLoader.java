package com.zyd.wlwsdk.utils.imagepicker;

import android.app.Activity;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.loader.ImageLoader;
import com.zyd.wlwsdk.R;

import java.io.File;

/**
 * @author ljz.
 * @date 2017/12/6.
 * 描述：
 */

public class GlideImageLoader implements ImageLoader{
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ip_default_image)     //设置占位图片
                .error(R.mipmap.ip_default_image)           //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.ALL);  //缓存全尺寸

        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ip_default_image)     //设置占位图片
                .error(R.mipmap.ip_default_image)           //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.ALL);  //缓存全尺寸
//
//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .apply(options)
//                .into(imageView);

        if (new File(path).exists()) {
            Glide.with(activity)
                    .asBitmap() // bitmap转化，如果是gif，则会显示第一帧
                    .load(Uri.fromFile(new File(path)))
                    .apply(options)
                    .into(imageView); // 显示图片
        } else if (path.startsWith("http")){
            Glide.with(activity)                             //配置上下文
                    .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .apply(options)
                    .into(imageView);
        }else {
            byte[] decode = Base64.decode(path, Base64.DEFAULT);
            Glide.with(activity)
                    .asBitmap() // bitmap转化，如果是gif，则会显示第一帧
                    .load(decode)
                    .apply(options)
                    .into(imageView); // 显示图片
        }
    }

    @Override
    public void clearMemoryCache() {
    }
}
