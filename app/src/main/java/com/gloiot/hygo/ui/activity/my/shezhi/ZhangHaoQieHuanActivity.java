package com.gloiot.hygo.ui.activity.my.shezhi;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.login.LoginActivity;
import com.gloiot.hygo.ui.activity.my.shezhi.guanlianshouji.TianxieYanzhengmaActivity;
import com.gloiot.hygo.ui.activity.web.WebFragment;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.ConstantUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.JSONUtlis;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.MD5Utlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.utils.PictureUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

public class ZhangHaoQieHuanActivity extends BaseActivity implements BaseActivity.RequestErrorCallback {

    @Bind(R.id.lv_zhanghao_qiehuan)
    ListView lvZhanghaoQiehuan;

    private CommonAdapter commonAdapter;
    private List<ZhangHaoQieHuanBean> listDatas = new ArrayList<>();

    private SQLiteDatabase sqLiteDatabase;
    private String zhanghao;
    private String mima;

    @Override
    public int initResource() {
        return R.layout.activity_zhanghao_qiehuan;
    }

    @Override
    public void initData() {
        setRequestErrorCallback(this);

        //数据库
        sqLiteDatabase = openOrCreateDatabase("shang_cheng.db", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE if not exists qiehuan_zhanghao (zhanghao VARCHAR PRIMARY KEY, mima VARCHAR, imgUrl VARCHAR)");

        //listView脚布局
        View footer = View.inflate(mContext, R.layout.item_zhanghao_footer, null);
        lvZhanghaoQiehuan.addFooterView(footer);
        //换个新账号登录点击事件
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.activity_open, 0);
                finish();
            }
        });
        //listView适配器
        commonAdapter = new CommonAdapter<ZhangHaoQieHuanBean>(mContext, R.layout.item_zhanghao_qiehuan, listDatas) {
            @Override
            public void convert(ViewHolder holder, final ZhangHaoQieHuanBean zhangHaoQieHuanBean) {
                PictureUtlis.loadCircularImageViewHolder(mContext, zhangHaoQieHuanBean.Str_ImageUrl,
                        R.mipmap.ic_portrait_default, (ImageView) holder.getView(R.id.item_zhanghao_qiehuan_touxiang));

                if (zhangHaoQieHuanBean.Str_ZhangHao.length() <= 5) {
                    holder.setText(R.id.item_zhanghao_qiehuan_zhanghao, zhangHaoQieHuanBean.Str_ZhangHao);
                } else {
                    holder.setText(R.id.item_zhanghao_qiehuan_zhanghao, zhangHaoQieHuanBean.Str_ZhangHao.substring(0, 2) +
                            "****" + zhangHaoQieHuanBean.Str_ZhangHao.substring(zhangHaoQieHuanBean.Str_ZhangHao.length() - 3, zhangHaoQieHuanBean.Str_ZhangHao.length()));
                }


                if (zhangHaoQieHuanBean.isVisible) {
                    holder.getView(R.id.item_zhanghao_qiehuan_delete).setVisibility(View.VISIBLE);
                    holder.getView(R.id.item_zhanghao_qiehuan_currentAccount).setVisibility(View.GONE);
                    holder.getView(R.id.item_zhanghao_qiehuan_touxiang).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.item_zhanghao_qiehuan_delete).setVisibility(View.GONE);
                    holder.getView(R.id.item_zhanghao_qiehuan_currentAccount).setVisibility(View.VISIBLE);
                    holder.getView(R.id.item_zhanghao_qiehuan_touxiang).setVisibility(View.VISIBLE);
                }
                //是否当前账号
                if (zhangHaoQieHuanBean.isCurrentAccount) {
                    holder.setImageResource(R.id.item_zhanghao_qiehuan_currentAccount, R.mipmap.ic_radio);
                } else {
                    holder.setImageResource(R.id.item_zhanghao_qiehuan_currentAccount, R.mipmap.ic_quan);
                }
                //删除
                holder.getView(R.id.item_zhanghao_qiehuan_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.twoBtnNormal(getmDialog(), "提示", "是否删除该账号？", true, "取消", "确认",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                        sqLiteDatabase.delete("qiehuan_zhanghao", "zhanghao=?",
                                                new String[]{zhangHaoQieHuanBean.Str_ZhangHao});
                                        //查询数据库，更新界面
                                        chaXunSQLite();
                                    }
                                });
                    }
                });

                holder.getView(R.id.item_zhanghao_qiehuan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (zhangHaoQieHuanBean.isCurrentAccount) {
                            MyToast.getInstance().showToast(mContext, "当前登录账号已是该账号");
                        } else {
                            checkPermission(new CheckPermListener() {
                                @Override
                                public void superPermission() {
                                    String phoneId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                    if (phoneId != null) {
                                        editor.putString(ConstantUtlis.SP_PHENEID, phoneId);
                                    } else {
                                        phoneId = "";
                                        editor.putString(ConstantUtlis.SP_PHENEID, "");
                                    }
                                    editor.commit();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("手机ID", phoneId);
                                    CommonUtlis.updataMap(ConstantUtlis.SP_PHONEINFO_JSON, hashMap);

                                    zhanghao = zhangHaoQieHuanBean.Str_ZhangHao;
                                    mima = zhangHaoQieHuanBean.Str_MiMa;

                                    requestHandleArrayList.add(requestAction.numAccountLogin(
                                            ZhangHaoQieHuanActivity.this, zhanghao,
                                            MD5Utlis.Md5(mima), phoneId));
                                }
                            }, R.string.perm_phoneinfo, Manifest.permission.READ_PHONE_STATE);
                        }
                    }
                });
            }
        };
        lvZhanghaoQiehuan.setAdapter(commonAdapter);

        //这里的顺序谨慎修改，需要从到commonAdapter对象，小心对象报null
        CommonUtlis.setTitleBar(this, "账号切换", "编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listDatas.size(); i++) {
                    if (listDatas.get(i).isVisible) {
                        listDatas.get(i).isVisible = false;
                    } else {
                        listDatas.get(i).isVisible = true;
                    }
                }
                commonAdapter.notifyDataSetChanged();
            }
        });

        //查询数据库，更新界面
        chaXunSQLite();
    }

    //查询数据库，更新界面
    private void chaXunSQLite() {
        //查询获得游标
        Cursor cursor = sqLiteDatabase.rawQuery("select * from qiehuan_zhanghao", null);

        listDatas.clear();
        ZhangHaoQieHuanBean zhangHaoQieHuanBean;
        while (cursor.moveToNext()) {
            zhangHaoQieHuanBean = new ZhangHaoQieHuanBean();
            //获得记录
            zhangHaoQieHuanBean.Str_ZhangHao = cursor.getString(0);
            zhangHaoQieHuanBean.Str_MiMa = cursor.getString(1);
            zhangHaoQieHuanBean.Str_ImageUrl = cursor.getString(2);
            if (zhangHaoQieHuanBean.Str_ZhangHao.equals(preferences.getString(ConstantUtlis.SP_LOGINZHANHAO, ""))) {
                zhangHaoQieHuanBean.isCurrentAccount = true;
            } else {
                zhangHaoQieHuanBean.isCurrentAccount = false;
            }
            zhangHaoQieHuanBean.isVisible = false;
            listDatas.add(zhangHaoQieHuanBean);
        }
        cursor.close();
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess :" + requestTag, response.toString());
        switch (requestTag) {
            case RequestAction.TAG_NUMACCOUNT:
                CommonUtlis.clearPersonalData();//清除个人数据，重置登录状态等
                SocketListener.getInstance().signoutRenZheng();
                IMDBManager.getInstance(mContext, preferences.getString(ConstantUtlis.SP_USERPHONE, "")).ClearnData();


                editor.putString(ConstantUtlis.SP_BANGDINGPHONE, JSONUtlis.getString(response, "手机号"));
                editor.putString(ConstantUtlis.SP_HAIWAIZHUCE, JSONUtlis.getString(response, "是否海外注册"));
                editor.putString(ConstantUtlis.SP_LOGINTYPE, "成功");
                editor.putString(ConstantUtlis.SP_USERPHONE, JSONUtlis.getString(response, "账号"));
                editor.putString(ConstantUtlis.SP_USERSMRZ, JSONUtlis.getString(response, "实名认证"));
                editor.putString(ConstantUtlis.SP_RANDOMCODE, JSONUtlis.getString(response, "随机码"));
                editor.putString(ConstantUtlis.SP_USERIMG, JSONUtlis.getString(response, "头像"));
                editor.putString(ConstantUtlis.SP_NICKNAME, JSONUtlis.getString(response, "昵称"));
                editor.putString(ConstantUtlis.SP_GENDER, JSONUtlis.getString(response, "性别"));
                editor.putString(ConstantUtlis.SP_LOCATION, JSONUtlis.getString(response, "地址"));
                editor.putString(ConstantUtlis.SP_MYPWD, JSONUtlis.getString(response, "是否设置过支付密码"));
                editor.putString(ConstantUtlis.SP_ONLYID, JSONUtlis.getString(response, "唯一id"));
                editor.putString(ConstantUtlis.SP_SUPERMERCHANT, JSONUtlis.getString(response, "是否显示超级商家"));
                editor.putString(ConstantUtlis.SP_KEFUCENTER, JSONUtlis.getString(response, "是否显示客服中心"));
                editor.putString(ConstantUtlis.SP_TRUENAME, JSONUtlis.getString(response, "真实名"));
                editor.putString(ConstantUtlis.SP_LOGINZHANHAO, JSONUtlis.getString(response, "账号"));
                editor.putString(ConstantUtlis.SP_SHIFOUBANGDINGWX, JSONUtlis.getString(response, "是否绑定微信"));

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("账号", JSONUtlis.getString(response, "账号"));
                hashMap.put("随机码", JSONUtlis.getString(response, "随机码"));
                hashMap.putAll(CommonUtlis.loadMap(ConstantUtlis.SP_PHONEINFO_JSON));
                CommonUtlis.saveMap(ConstantUtlis.SP_REQUESTINFO_JSON, hashMap);
                editor.commit();
                imDB(JSONUtlis.getString(response, "账号"));
                imSocket();
                // 获取图片清单
//                InfoRequestUtils.getInstance().getPictureList();

                //SQLite
                ContentValues contentValues = new ContentValues();
                contentValues.put("zhanghao", zhanghao);
                contentValues.put("mima", mima);
                contentValues.put("imgUrl", JSONUtlis.getString(response, "头像"));
                sqLiteDatabase.replace("qiehuan_zhanghao", null, contentValues);


                Intent it0 = new Intent();
                it0.putExtra("onlyid", JSONUtlis.getString(response, "唯一id"));
                setResult(WebFragment.TAG_LOGIN_BACK, it0);  // 网页跳进登录页返回唯一id

                Intent intent0 = new Intent();
                intent0.putExtra("type", "成功");
                intent0.putExtra("onlyid", JSONUtlis.getString(response, "唯一id"));
                setResult(0x100, intent0);
                finish();
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, final JSONObject response) throws Exception {
        if (response.getString("状态").equals("当前账户登录过于频繁已被锁定,请24小时之后重试")) {
            DialogUtlis.oneBtnNormal(getmDialog(), "当前账户登录过于频繁已被锁定,请24小时之后重试");
        } else if (response.getString("状态").equals("与上次登录的设备不同")) {
            String phone = response.getString("手机号");
            final String finalPhone = phone;
            Log.e("phoneeee1", finalPhone);
            if (phone.length() != 11) {
                phone = "****" + phone.substring(phone.length() - 4, phone.length());
            } else {
                phone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
            }
            DialogUtlis.twoBtnNormal(getmDialog(), "提示", "由于你在新设备登录，我们需要验证你的身份，是否发送验证码到该账号绑定的手机号" + phone, true, "取消", "确定",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("phoneeee2", finalPhone);
                            dismissDialog();
                            Intent it = new Intent(ZhangHaoQieHuanActivity.this, TianxieYanzhengmaActivity.class);
                            //表示是从登录验证是否常用手机处进入的
                            it.putExtra("type", "common_phone");
                            it.putExtra("newPhoneNum", finalPhone);
                            //表示是否要对账号密码进行存储
                            it.putExtra("storage", true);
                            it.putExtra("zhanghao", zhanghao);
                            it.putExtra("mima", mima);
                            startActivityForResult(it, 0x100);
                        }
                    });
        } else {
            MyToast.getInstance().showToast(mContext, response.getString("状态"));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0x100:  // 帐号密码界面登录返回成功关闭当前页面
                if (data.getStringExtra("type").equals("成功")) {
                    Intent intent = new Intent();
                    intent.putExtra("onlyid", data.getStringExtra("onlyid"));
                    setResult(WebFragment.TAG_LOGIN_BACK, intent);  // 网页跳进登录页返回唯一id
                    finish();
                }
                break;
        }
    }

    private void imDB(String account) {
        IMDBManager.getInstance(mContext, account);
    }

    // socket认证
    private void imSocket() {
        if (!TextUtils.isEmpty(preferences.getString(ConstantUtlis.SP_USERPHONE, ""))) {
            SocketListener.getInstance().connectionRenZheng(preferences.getString(ConstantUtlis.SP_USERPHONE, ""), preferences.getString(ConstantUtlis.SP_RANDOMCODE, ""));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }

    public class ZhangHaoQieHuanBean {
        //头像图片
        private String Str_ImageUrl = "";
        //账号
        private String Str_ZhangHao = "";
        //密码
        private String Str_MiMa = "";
        //删除按钮是否可见
        private boolean isVisible = false;
        //是否当前账号
        private boolean isCurrentAccount = false;
    }
}
