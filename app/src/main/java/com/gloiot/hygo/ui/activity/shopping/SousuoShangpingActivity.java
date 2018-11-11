package com.gloiot.hygo.ui.activity.shopping;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygo.R;
import com.gloiot.hygo.server.network.RequestAction;
import com.gloiot.hygo.ui.BaseActivity;
import com.gloiot.hygo.ui.activity.shopping.Bean.ShangPingInfoBean;
import com.gloiot.hygo.ui.activity.shopping.fenlei.FenLeiSonActivity;
import com.gloiot.hygo.utlis.CommonUtlis;
import com.gloiot.hygo.utlis.dialog.DialogUtlis;
import com.zyd.wlwsdk.utils.MyToast;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utils.L;
import com.zyd.wlwsdk.utils.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hygo03 on 2016/10/20.
 * 商品搜索
 */
public class SousuoShangpingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.lv_sousuo_tishi_listview)
    ListView lv_sousuo_tishi_listview;
    @Bind(R.id.et_sousuo_info)
    EditText et_sousuo_info;
    @Bind(R.id.img_cancel)
    ImageView img_cancel;

    private Context mContext;
    //ListView
    private List<String> listDatas = new ArrayList<>(5);
    private CommonAdapter<String> commonAdapter;
    private View viewHeard, viewFooter;
    private LinearLayout tv_item_list_sousuo;

    //SQLite
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public int initResource() {
        return R.layout.activity_sousuo_shangping;
    }

    @Override
    public void initData() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 40);
        initComponent();

        mContext = this;
        CommonUtlis.setSousuoBar(this, true);

        //查询获得游标
        Cursor cursor = sqLiteDatabase.rawQuery("select * from sousuo_jilu", null);

        for (int i = cursor.getCount(); i > 0; i--) {
            cursor.moveToPosition(i - 1);
            //获得记录
            String str = cursor.getString(0);
            L.e("-----", str + "----");
            listDatas.add(str);
        }
        cursor.close();
        commonAdapter.notifyDataSetChanged();
        if (listDatas.size() == 0)
            lv_sousuo_tishi_listview.removeFooterView(viewFooter);
    }

    public void initComponent() {
        img_cancel = (ImageView) findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(this);
        et_sousuo_info = (EditText) findViewById(R.id.et_sousuo_info);
        et_sousuo_info.addTextChangedListener(textWatcher);
        et_sousuo_info.requestFocus();

        //ListView
        lv_sousuo_tishi_listview = (ListView) findViewById(R.id.lv_sousuo_tishi_listview);
        commonAdapter = new CommonAdapter<String>(this, R.layout.item_list_sousuo, listDatas) {
            @Override
            public void convert(ViewHolder holder, String str) {
                holder.setText(R.id.tv_item_list_sousuo, str);
            }
        };

        lv_sousuo_tishi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MToastUtils.showToast(mContext, position + "");
//                MToastUtils.showToast(mContext, parent.getAdapter().getItemId(position) + "");
//                MToastUtils.showToast(mContext, listDatas.get(position - 1));

                if (parent.getAdapter().getItemId(position) > -1) {
//                    sqLiteDatabase.execSQL("insert OR IGNORE into sousuo_jilu(jilu) values('"
//                            + listDatas.get((int) parent.getAdapter().getItemId(position)) + "')");

//                    sqLiteDatabase.rawQuery()

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("jilu", listDatas.get((int) parent.getAdapter().getItemId(position)));
                    sqLiteDatabase.replace("sousuo_jilu", null, contentValues);

                    Intent intent = new Intent(mContext, FenLeiSonActivity.class);
                    intent.putExtra("关键字", listDatas.get((int) parent.getAdapter().getItemId(position)));
                    intent.putExtra("类型", "搜索");
                    startActivity(intent);

                    finish();
                }
            }
        });

        /**
         * 软键盘搜索监听
         * */
        et_sousuo_info.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (et_sousuo_info.getText().toString().trim().length() != 0) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("jilu", et_sousuo_info.getText().toString().replace(" ", ""));

                        sqLiteDatabase.replace("sousuo_jilu", null, contentValues);

                        Intent intent = new Intent(mContext, FenLeiSonActivity.class);
                        intent.putExtra("关键字", et_sousuo_info.getText().toString().replace(" ", ""));
                        intent.putExtra("类型", "搜索");
                        startActivity(intent);
                        finish();
                    } else {
                        et_sousuo_info.setText("");
                        DialogUtlis.oneBtnNormal(getmDialog(), "请输入关键字!");
                    }
                    return true;
                }
                return false;
            }
        });

        //头尾（历史搜索、清空历史记录）
        viewHeard = View.inflate(this, R.layout.item_list_sousuo_heard, null);
        viewFooter = View.inflate(this, R.layout.item_list_sousuo_footer, null);
        tv_item_list_sousuo = (LinearLayout) viewFooter.findViewById(R.id.tv_item_list_sousuo);
        lv_sousuo_tishi_listview.removeHeaderView(viewHeard);
        lv_sousuo_tishi_listview.removeFooterView(viewFooter);
        lv_sousuo_tishi_listview.addHeaderView(viewHeard);
        lv_sousuo_tishi_listview.addFooterView(viewFooter);

        lv_sousuo_tishi_listview.setAdapter(commonAdapter);

        //清空事件监听
        tv_item_list_sousuo.setOnClickListener(this);

        //SQLite
        sqLiteDatabase = openOrCreateDatabase("shang_cheng.db", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE if not exists sousuo_jilu (jilu VARCHAR PRIMARY KEY)");

    }

    //控制历史搜索、清空历史记录是否显示
    private void changeType(boolean flag) {
        if (flag) {
            lv_sousuo_tishi_listview.removeHeaderView(viewHeard);
            lv_sousuo_tishi_listview.removeFooterView(viewFooter);
            lv_sousuo_tishi_listview.addHeaderView(viewHeard);
            lv_sousuo_tishi_listview.addFooterView(viewFooter);
        } else {
            lv_sousuo_tishi_listview.removeHeaderView(viewHeard);
            lv_sousuo_tishi_listview.removeFooterView(viewFooter);
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                changeType(false);
                getShangPinInfo(s.toString());
            } else {
                changeType(true);

                //查询获得游标
                Cursor cursor = sqLiteDatabase.rawQuery("select * from sousuo_jilu", null);
                listDatas.clear();
                while (cursor.moveToNext()) {
                    //获得记录
                    String str = cursor.getString(0);
                    listDatas.add(str);
                }
                cursor.close();
                commonAdapter.notifyDataSetChanged();

                if (listDatas.size() == 0)
                    lv_sousuo_tishi_listview.removeFooterView(viewFooter);

            }
        }
    };

    private void getShangPinInfo(String str) {
        requestHandleArrayList.add(requestAction.shop_hp_atuohint(this, str));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_sousuo:
//                if (et_sousuo_info.getText().toString().trim().length() != 0) {
//
////                    sqLiteDatabase.execSQL("insert OR IGNORE into sousuo_jilu(jilu) values('"
////                            + et_sousuo_info.getText().toString().replace(" ", "") + "')");
//
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("jilu", et_sousuo_info.getText().toString().replace(" ", ""));
//                    sqLiteDatabase.replace("sousuo_jilu", null, contentValues);
//
//                    Intent intent = new Intent(mContext, FenLeiSonActivity.class);
//                    intent.putExtra("关键字", et_sousuo_info.getText().toString().replace(" ", ""));
//                    intent.putExtra("类型", "搜索");
//                    startActivity(intent);
//
//                    finish();
//                } else {
//                    et_sousuo_info.setText("");
//
//                    DialogUtlis.oneBtnNormal(getmDialog(), "请输入关键字!");
////                    MyDialog.showDialogwithpic(mContext, "请输入关键字!", R.mipmap.gantanhao);
//                }
//
//
////                if (et_sousuo_info.getText().toString().trim().length() != 0) {
////                    shangPingInfoList.clear();
////                    String info = et_sousuo_info.getText().toString();
////                    getData(info, "0");
////                } else {
////                    et_sousuo_info.setText("");
////                    MyDialog.showDialogwithpic(mContext, "请输入关键字!", R.mipmap.gantanhao);
////                }
//                break;

            case R.id.img_cancel:
                MyToast.getInstance().showToast(mContext, "11111");
                break;
            case R.id.tv_item_list_sousuo:
                sqLiteDatabase.execSQL("DELETE FROM sousuo_jilu");
                MyToast.getInstance().showToast(mContext, "清空历史记录");
                listDatas.clear();
                commonAdapter.notifyDataSetChanged();
                lv_sousuo_tishi_listview.removeFooterView(viewFooter);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭数据库
        sqLiteDatabase.close();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_SHOP_HP_ATUOHINT:
                if ("成功".equals(response.getString("状态"))) {
                    listDatas.clear();
                    JSONArray jsonArray = response.getJSONArray("数据");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listDatas.add(jsonArray.getString(i));
                    }
                    commonAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
