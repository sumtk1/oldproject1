package com.gloiot.hygo.utlis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.ui.activity.login.WangjimimaActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.SharedPreferencesUtils;
import com.zyd.wlwsdk.widge.MDialog;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by hygo01 on 2016/9/13.
 * 常用方法工具类
 */

public class CommonUtlis {

    private static SharedPreferences preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
    private static SharedPreferences.Editor editor = preferences.edit();

    public static boolean isWXAppInstalledAndSupported(Context context) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(ConstantUtlis.WX_APP_ID);

        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        if (!sIsWXAppInstalledAndSupported) {
            MyToast.getInstance().showToast(context, "未安装微信");
        }

        return sIsWXAppInstalledAndSupported;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin(Context context) {
        if (!preferences.getString(ConstantUtlis.SP_LOGINTYPE, "").equals("成功")) {
            context.startActivity(new Intent(context, LoginActivity.class));
            ((Activity) context).overridePendingTransition(R.anim.activity_open, 0);
            return false;
        }
        return true;
    }

    /**
     * 设置标题栏
     *
     * @param context
     * @param titleString
     */
    public static void setTitleBar(final Activity context, String titleString) {
        ImageView back = (ImageView) context.findViewById(R.id.toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.toptitle_tv);
        title.setText(titleString);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View view = context.getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                context.finish();
            }
        });
    }

    /**
     * 设置标题栏 更多
     *
     * @param context
     * @param titleString
     * @param tvmore
     */
    public static void setTitleBar(final Activity context, String titleString, String tvmore) {
        ImageView back = (ImageView) context.findViewById(R.id.toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.toptitle_tv);
        TextView more = (TextView) context.findViewById(R.id.toptitle_more);
        title.setText(titleString);
        more.setText(tvmore);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View view = context.getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                context.finish();
            }
        });
    }

    /**
     * 设置标题栏 更多监听
     *
     * @param context
     * @param titleString
     * @param tvmore
     * @param onClickListener
     */
    public static void setTitleBar(final Activity context, String titleString, String tvmore, View.OnClickListener onClickListener) {
        ImageView back = (ImageView) context.findViewById(R.id.toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.toptitle_tv);
        TextView more = (TextView) context.findViewById(R.id.toptitle_more);
        title.setText(titleString);
        more.setText(tvmore);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View view = context.getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                context.finish();
            }
        });
        more.setOnClickListener(onClickListener);
    }

    /**
     * 设置标题栏
     *
     * @param context
     * @param titleString
     * @param tvmore
     */
    public static void setTitleBar(final Activity context, String titleString, String tvmore, boolean isBack) {
        ImageView back = (ImageView) context.findViewById(R.id.toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.toptitle_tv);
        TextView more = (TextView) context.findViewById(R.id.toptitle_more);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    View view = context.getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    context.finish();
                }
            });
        } else {
            back.setVisibility(View.GONE);
        }

    }

    /**
     * 设置标题栏
     *
     * @param context
     * @param titleString
     * @param tvmore
     */
    public static void setTitleBar(final Activity context, boolean isBack, String titleString, String tvmore) {
        ImageView back = (ImageView) context.findViewById(R.id.toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.toptitle_right);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    View view = context.getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    context.finish();
                }
            });
        } else {
            back.setVisibility(View.GONE);
        }

    }

    /**
     * 带按钮头部的设置文字与点击事件
     *
     * @param str1             按钮文字（左边）
     * @param str2             按钮文字（右边）
     * @param context
     * @param str1
     * @param str2
     * @param onClickListener1
     * @param onClickListener2
     */
    public static void setTitleButtonText(final Activity context, String str1, String str2,
                                          View.OnClickListener onClickListener1, View.OnClickListener onClickListener2) {
        ImageView back = (ImageView) context.findViewById(R.id.toptitle_back);
        TextView bt_apply_shouhou = (TextView) context.findViewById(R.id.bt_apply_shouhou);
        TextView bt_apply_jilu = (TextView) context.findViewById(R.id.bt_apply_jilu);
        bt_apply_shouhou.setText(str1);
        bt_apply_jilu.setText(str2);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View view = context.getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                context.finish();
            }
        });
        bt_apply_shouhou.setOnClickListener(onClickListener1);
        bt_apply_jilu.setOnClickListener(onClickListener2);
    }


    /**
     * 获取Top更多
     *
     * @param context
     * @return
     */
    public static TextView getTitleMore(final Activity context) {
        TextView more = (TextView) context.findViewById(R.id.toptitle_right);
        return more;
    }


    public static void setSousuoBar(final Activity context, boolean isBack) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        if (isBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    View view = context.getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    context.finish();
                }
            });
        } else {
            back.setVisibility(View.GONE);
        }

    }

    /**
     * 根据当前的ListView的列表项计算列表的尺寸
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 被ScrollView包含的GridView高度设置为wrap_content时只显示一行
     * 此方法用于动态计算GridView的高度(根据item的个数)
     */
    public static void reMesureGridViewHeight(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            // 利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);

            // 利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
        gridView.setLayoutParams(params);
    }


    /**
     * 保存HashMap
     *
     * @param status
     * @param hashMap
     * @return
     */
    public static boolean saveMap(String status, HashMap<String, Object> hashMap) {
        editor.putInt(status, hashMap.size());
        int i = 0;
        for (HashMap.Entry<String, Object> entry : hashMap.entrySet()) {
            editor.remove(status + "_key_" + i);
            editor.putString(status + "_key_" + i, entry.getKey());
            editor.remove(status + "_value_" + i);
            editor.putString(status + "_value_" + i, (String) entry.getValue());
            i++;
        }
        return editor.commit();
    }

    /**
     * 取出HashMap
     *
     * @param status
     * @return
     */
    public static HashMap<String, Object> loadMap(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int size = preferences.getInt(status, 0);
        for (int i = 0; i < size; i++) {
            String key = preferences.getString(status + "_key_" + i, null);
            String value = preferences.getString(status + "_value_" + i, null);
            hashMap.put(key, value);
        }
        return hashMap;
    }

    /**
     * 更新HashMap
     *
     * @param status
     * @param hashMap
     * @return
     */
    public static void updataMap(String status, HashMap<String, Object> hashMap) {
        int size = preferences.getInt(status, 0);
        for (int i = 0; i < size; i++) {
            Iterator iter = hashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key1 = (String) entry.getKey();
                String key2 = preferences.getString(status + "_key_" + i, null);
                if (key1.equals(key2)){
                    editor.remove(status + "_value_" + i);
                    editor.putString(status + "_value_" + i, (String) entry.getValue());
                    editor.commit();
                }
            }
        }
    }


    /**
     * encode编译URL
     *
     * @param kv
     * @return
     */
    public static String encodeUtli(String kv) {
        try {
            kv = java.net.URLEncoder.encode(kv, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return kv;
    }

    /**
     * decode编译URL
     *
     * @param kv
     * @return
     */
    public static String decodeUtli(String kv) {
        try {
            kv = java.net.URLDecoder.decode(kv, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return kv;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 未设置支付密码
     */
    public static void toSetPwd(final Context mContext, final MDialog mDialog) {
        DialogUtlis.twoBtnNormal(mDialog, "提示", "请设置支付密码保障您的账户安全！", true, "取消", "立即设置",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        Intent intent = new Intent(mContext, WangjimimaActivity.class);
                        intent.putExtra("forgetpwd", "设置支付密码");
                        mContext.startActivity(intent);
                    }
                });
    }

    public static void setNumPoint(EditText editText, int length) {
        final int DECIMAL_DIGITS = length;
        InputFilter lengthFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // source:当前输入的字符
                // start:输入字符的开始位置
                // end:输入字符的结束位置
                // dest：当前已显示的内容
                // dstart:当前光标开始位置
                // dent:当前光标结束位置
                Log.i("", "source=" + source + ",start=" + start + ",end=" + end
                        + ",dest=" + dest.toString() + ",dstart=" + dstart
                        + ",dend=" + dend);
                if (dest.length() == 0 && source.equals(".")) {
                    return "0.";
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    if (dotValue.length() == DECIMAL_DIGITS) {
                        return "";
                    }
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{lengthFilter});
    }

    /**
     * (大小比例还需要调整)
     * 设置商品名称前面图标（自营/ 全球购物）
     * @param context   上下文
     * @param type      类型
     * @param title     商品名称
     * @param textView  控件
     */
    public static void setImageTitle(Context context, String type, String title, TextView textView){
        Drawable drawable1 = context.getResources().getDrawable(R.mipmap.icon_quanqiugou_tubiao);
        Drawable drawable2 = context.getResources().getDrawable(R.mipmap.icon_ziyin_tubiao);
        SpannableString spannableString = null;
        switch (type) {
            case "全球购-自营":
                spannableString = new SpannableString("1 2 " + title);
                drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
                ImageSpan imageSpan = new ImageSpan(drawable1);
                drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
                ImageSpan imageSpan0 = new ImageSpan(drawable2);
                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(imageSpan0, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
                break;
            case "全球购":
                spannableString = new SpannableString("1 " + title);
                drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
                ImageSpan imageSpan1 = new ImageSpan(drawable1);
                spannableString.setSpan(imageSpan1, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
                break;
            case "自营":
                spannableString = new SpannableString("1 " + title);
                drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
                ImageSpan imageSpan2 = new ImageSpan(drawable2);
                spannableString.setSpan(imageSpan2, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
                break;
            default:
                textView.setText(title);
                break;
        }
    }


    /**
     * 清除账号个人数据
     */
    public static void clearPersonalData() {
        // 头像
        editor.putString(ConstantUtlis.SP_USERIMG, "");
        // 昵称
        editor.putString(ConstantUtlis.SP_NICKNAME, "");
        // 账号(手机号)
        editor.putString(ConstantUtlis.SP_USERPHONE, "");
        // 性别
        editor.putString(ConstantUtlis.SP_GENDER, "");
        // 生日
        editor.putString(ConstantUtlis.SP_BIRTHDAY, "");
        // 认证状态
        editor.putString(ConstantUtlis.SP_USERSMRZ, "");
        // 位置
        editor.putString(ConstantUtlis.SP_LOCATION, "");
        // 个性签名
        editor.putString(ConstantUtlis.SP_PERSONALIZEDSIGNATURE, "");
        // 唯一id
        editor.putString(ConstantUtlis.SP_ONLYID, "");
        // 随机码
        editor.putString(ConstantUtlis.SP_RANDOMCODE, "");
        // 绑定手机号
        editor.putString(ConstantUtlis.SP_BANGDINGPHONE, "");
        // 超级商家
        editor.putString(ConstantUtlis.SP_SUPERMERCHANT, "");
        // 登录请求数据
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", "");
        hashMap.put("随机码", "");
        hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
        CommonUtlis.saveMap(ConstantUtlis.SP_REQUESTINFO_JSON, hashMap);

        //商城的
        ConstantUtlis.CHECK_ADD_GOUWUCHE = true;
        ConstantUtlis.CHECK_CHANGE_SHOUCANG = true;

        editor.putString(ConstantUtlis.SP_LOGINTYPE, "退出");
        editor.putString(ConstantUtlis.SP_LOGINTJIEMIANYPE, "退出");//用于判断在登录页返回MyFragment还是进入登录页前的页面
        editor.putString(ConstantUtlis.SP_GESTURELOCK_RESULT, "");
        editor.commit();

    }
}
