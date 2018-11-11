package com.gloiot.hygo.im;

import android.content.Context;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo00 on 2017/8/10.
 */

public class AppsManager {

    public static AppsManager instence;
    private Context context;

    public AppsManager(Context context){
        this.context = context;
    }

    public static AppsManager getInstence(Context context){
        if (instence == null){
            instence = new AppsManager(context);
        }
        return instence;
    }

    public List<ExtendBean> addApps(){
        List<ExtendBean> list = new ArrayList<>();
//        list.add(null);
//        list.add(new ExtendBean(com.gloiot.chatsdk.R.drawable.chat_image_bg, "图片2"));
        return list;
    }

    public void setAppsOnClick(int position){
//        switch (position) {
//            case 2:
//                MToast.showToast(context, "222222");
//                break;
//            case 3:
//
//                break;
//        }
    }

}
